package que_me_pongo.guardarropa;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import que_me_pongo.atuendo.Atuendo;
import que_me_pongo.prenda.Categoria;
import que_me_pongo.prenda.Prenda;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.*;


import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

@Entity
public class Guardarropa implements WithGlobalEntityManager{

	@Id @GeneratedValue
	private int id;
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER) @JoinColumn(name = "guardarropa_id")
	private Set<Prenda> prendas;

	public Guardarropa() {
		prendas = new HashSet<Prenda>();
	}

	public Set<Prenda> getPrendas() {
		return entityManager().
				createQuery("FROM Prenda WHERE guardarropa_id = :id", Prenda.class).
				setParameter("id", id).
				getResultList().
				stream().
				collect(Collectors.toSet());
	}

	public void agregarPrenda(Prenda prenda) {
		Objects.requireNonNull(prenda);
		if(prendas.contains(prenda))
			throw new PrendaYaEnGuardarropasException();
		prendas.add(prenda);
	}

	public int getId() {
		return id;
	}

	public Set<Prenda> getPrendasEn(Categoria categoria){
		
		return entityManager().
				createQuery("FROM Prenda WHERE guardarropa_id = :id AND tipo.categoria = :categoria", Prenda.class).
				setParameter("id", id).
				setParameter("categoria", categoria).
				getResultList().
				stream().
				collect(Collectors.toSet());
	}

	public int cantidadPrendasEn(Categoria categoria) {
		return getPrendasEn(categoria).size();
	}

	public int cantidadPrendas() {
		return prendas.size();
	}

	public boolean estaLleno(int cantidadMaxima){
		return this.cantidadPrendas() >= cantidadMaxima;
	}

	public void reservarAtuendo(LocalDate fecha, Atuendo atuendo) {
		atuendo.getPrendas().forEach(prenda -> prenda.addReserva(fecha));
	}

	public void liberarAtuendo(LocalDate fecha, Atuendo atuendo) {
		atuendo.getPrendas().forEach(prenda -> prenda.removeReserva(fecha));
	}

	public Set<Atuendo> atuendos(LocalDate fecha){
		return atuendos().stream().filter(atuendo -> atuendoDisponibleEnFecha(fecha, atuendo)).collect(Collectors.toSet());

	}

	public Set<Atuendo> atuendos(){
		Set<List<Prenda>> atuendosMinimos = Sets.cartesianProduct(ImmutableList.of(
				filtrarPorCapa(Categoria.SUPERIOR, 0),
				getPrendasEn(Categoria.INFERIOR),
				getPrendasEn(Categoria.CALZADO)
		));

		Set<List<List<Prenda>>> paresCombinacionAccesorio = Sets.cartesianProduct(
				ImmutableList.of(atuendosMinimos, subConjuntos(getPrendasEn(Categoria.ACCESORIO))));

		Set<List<Prenda>> atuendosConAccesorios = aplanarAtuendos(paresCombinacionAccesorio);

		Set<List<Prenda>> atuendosConSuperiores = agregarCapas(Categoria.SUPERIOR, atuendosConAccesorios);

		return atuendosConSuperiores.stream().map(prendas -> new Atuendo(prendas)).collect(Collectors.toSet());
	}

	private Set<List<Prenda>> subConjuntos(Set<Prenda> prendas){
		return Sets.powerSet(prendas).stream()
				.map(set -> new ArrayList<Prenda>(set)).collect(Collectors.toSet());
	}

	private Set<List<Prenda>> aplanarAtuendos(Set<List<List<Prenda>>> paresCombinacionAccesorio){
		return paresCombinacionAccesorio.stream()
				.map(par -> par.stream().flatMap(List::stream).collect(Collectors.toList()))
				.collect(Collectors.toSet());
	}

	private Set<List<Prenda>> agregarCapas(Categoria categoria, Set<List<Prenda>> atuendosBase) {
		int maximaCapa = this.getCapaMaxima(categoria);

		Stream<Set<List<Prenda>>> subConjuntosPorCapa = IntStream.rangeClosed(1, maximaCapa).
				<Set<List<Prenda>>>mapToObj(capa -> this.subConjuntos(this.filtrarPorCapa(categoria, capa)));

		return subConjuntosPorCapa.reduce(atuendosBase, this::accumulator);
	}

	private Set<List<Prenda>> accumulator(Set<List<Prenda>> atuendosBase, Set<List<Prenda>> subConjuntos) {
		Set<List<List<Prenda>>> combinaciones = Sets.cartesianProduct(
				ImmutableList.of(atuendosBase, subConjuntos));
		return this.aplanarAtuendos(combinaciones);
	}

	private Set<Prenda> filtrarPorCapa(Categoria categoria, int capa) {
		return getPrendasEn(categoria).stream().filter(prenda -> prenda.getCapa() == capa).collect(Collectors.toSet());
	}

	private int getCapaMaxima(Categoria categoria) {
		Optional<Prenda> max = getPrendasEn(categoria).stream().max(Comparator.comparing(prenda-> prenda.getCapa()));
		if(max.isPresent())
			return max.get().getCapa();
		else
			return 0;
	}

	private boolean atuendoDisponibleEnFecha(LocalDate fecha, Atuendo atuendo) {
		return atuendo.getPrendas().stream().noneMatch(prenda -> prenda.getReserva(fecha));
	}



}
