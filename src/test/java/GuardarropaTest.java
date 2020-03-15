import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import que_me_pongo.atuendo.Atuendo;
import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.guardarropa.PrendaYaEnGuardarropasException;
import que_me_pongo.guardarropa.RepositorioGuardarropas;
import que_me_pongo.prenda.Categoria;
import que_me_pongo.prenda.Material;
import que_me_pongo.prenda.Prenda;
import que_me_pongo.prenda.RepositorioPrendas;
import que_me_pongo.prenda.TipoDePrendaFactory;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;



public class GuardarropaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
	RepositorioPrendas repoPrenda = RepositorioPrendas.getInstance();
	Prenda remera = repoPrenda.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(),Material.SEDA, Color.BLACK, null,null));
	Prenda remeraB = repoPrenda.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(),Material.ALGODON, Color.WHITE, null,null));
	Prenda pantalonA = repoPrenda.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().shorts(),Material.ALGODON, Color.BLACK, null,null));
	Prenda pantalonB = repoPrenda.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().shorts(),Material.ALGODON, Color.PINK, null,null));
	Prenda accesorioA = repoPrenda.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().anteojos(),Material.PLASTICO, Color.ORANGE, null,null));
	Prenda zapatoA = repoPrenda.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().zapatosDeTacon(),Material.CUERO, Color.BLUE, null,null));
	Prenda zapatoB = repoPrenda.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().zapatosDeTacon(),Material.CUERO, Color.GREEN, null,null));

	Atuendo atuendoA = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA, accesorioA));
	Atuendo atuendoB = new Atuendo(Arrays.asList(remera, pantalonA, zapatoB, accesorioA));
	Atuendo atuendoC = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA));
	Atuendo atuendoD = new Atuendo(Arrays.asList(remera, pantalonA, zapatoB));
	Atuendo atuendoE = new Atuendo(Arrays.asList(remera, pantalonB, zapatoA, accesorioA));
	Atuendo atuendoF = new Atuendo(Arrays.asList(remera, pantalonB, zapatoB, accesorioA));
	Atuendo atuendoG = new Atuendo(Arrays.asList(remera, pantalonB, zapatoA));
	Atuendo atuendoH = new Atuendo(Arrays.asList(remera, pantalonB, zapatoB));
	Atuendo atuendoI = new Atuendo(Arrays.asList(remeraB, pantalonA, zapatoA, accesorioA));
	Atuendo atuendoJ = new Atuendo(Arrays.asList(remeraB, pantalonA, zapatoB, accesorioA));
	Atuendo atuendoK = new Atuendo(Arrays.asList(remeraB, pantalonA, zapatoA));
	Atuendo atuendoL = new Atuendo(Arrays.asList(remeraB, pantalonA, zapatoB));
	Atuendo atuendoM = new Atuendo(Arrays.asList(remeraB, pantalonB, zapatoA, accesorioA));
	Atuendo atuendoN = new Atuendo(Arrays.asList(remeraB, pantalonB, zapatoB, accesorioA));
	Atuendo atuendoO = new Atuendo(Arrays.asList(remeraB, pantalonB, zapatoA));
	Atuendo atuendoP = new Atuendo(Arrays.asList(remeraB, pantalonB, zapatoB));

	@Test
	public void cargaRemeraEnGuardarropa() {
		EntityManager em = entityManager();

		Guardarropa guardarropa = new Guardarropa();
		em.persist(guardarropa);

		Assert.assertEquals(0, guardarropa.cantidadPrendasEn(Categoria.SUPERIOR));
		guardarropa.agregarPrenda(remera);
		Assert.assertEquals(1, guardarropa.cantidadPrendasEn(Categoria.SUPERIOR));
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	public void noCargaDosVecesLoMismo() throws Exception {
		expectedEx.expect(PrendaYaEnGuardarropasException.class);
		Guardarropa guardarropa = new Guardarropa();
		guardarropa.agregarPrenda(remera);
		guardarropa.agregarPrenda(remera);
	}

	@Test
	public void cargarPrendaConNullGeneraNullPointerException() throws Exception {
		expectedEx.expect(NullPointerException.class);
		(new Guardarropa()).agregarPrenda(null);
	}

	@Test
	public void generaCorrectamenteLosAtuendos() {
		Guardarropa guardarropa = RepositorioGuardarropas.getInstance().createGuardarropas(new Guardarropa());

		guardarropa.agregarPrenda(remera);
		guardarropa.agregarPrenda(remeraB);
		guardarropa.agregarPrenda(pantalonA);
		guardarropa.agregarPrenda(pantalonB);
		guardarropa.agregarPrenda(accesorioA);
		guardarropa.agregarPrenda(zapatoA);
		guardarropa.agregarPrenda(zapatoB);
		
		withTransaction(() -> entityManager().flush());

		Set<Atuendo> setAtuendos = new HashSet<>();
		setAtuendos.addAll(Arrays.asList(atuendoA, atuendoB, atuendoC, atuendoD,
				atuendoE, atuendoF, atuendoG, atuendoH, atuendoI, atuendoJ, atuendoK,
				atuendoL, atuendoM, atuendoN, atuendoO, atuendoP));

		Set<Atuendo> atuendos = guardarropa.atuendos();

		Assert.assertEquals(setAtuendos.size(), atuendos.size());
		setAtuendos.forEach(atuendoEsperado -> Assert.assertTrue(atuendos.stream().anyMatch(atuendoGenerado -> atuendoGenerado.mismoAtuendo(atuendoEsperado))));
	}

	@Test
	public void sinNoTieneSuficientesPrendasGeneraUnaColeccionVacia() {
		Guardarropa guardarropa = RepositorioGuardarropas.getInstance().createGuardarropas(new Guardarropa());

		guardarropa.agregarPrenda(remera);
		guardarropa.agregarPrenda(remeraB);
		guardarropa.agregarPrenda(pantalonA);

		Assert.assertTrue(guardarropa.atuendos().isEmpty());
	}

	@Test
	public void generaCorrectamenteAtuendosDeVariosAccesorios() {
		

		Prenda accesorioB = new Prenda(TipoDePrendaFactory.getInstance().aros(), Material.PLASTICO, Color.blue, null,null);
		repoPrenda.createPrenda(accesorioB);

		Guardarropa guardarropa = RepositorioGuardarropas.getInstance().createGuardarropas(new Guardarropa());

		guardarropa.agregarPrenda(remera);
		guardarropa.agregarPrenda(pantalonA);
		guardarropa.agregarPrenda(zapatoA);
		guardarropa.agregarPrenda(accesorioA);
		guardarropa.agregarPrenda(accesorioB);
		
		withTransaction(() -> entityManager().flush());

		Atuendo atuendoConB1 = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA, accesorioB));
		Atuendo atuendoConB2 = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA, accesorioB, accesorioA));

		Set<Atuendo> atuendos = guardarropa.atuendos();
		List<Atuendo> setAtuendos = Arrays.asList(atuendoA, atuendoC, atuendoConB1, atuendoConB2);

		Assert.assertEquals(setAtuendos.size(), atuendos.size());
		setAtuendos.forEach(atuendoEsperado -> Assert.assertTrue(atuendos.stream().anyMatch(atuendoGenerado -> atuendoGenerado.mismoAtuendo(atuendoEsperado))));
	}

	@Test
	public void generaAtuendosDeDosCapas() {
		Guardarropa guardarropa = RepositorioGuardarropas.getInstance().createGuardarropas(new Guardarropa());
		Prenda buzo = new Prenda(TipoDePrendaFactory.getInstance().buzo(),Material.ALGODON, Color.black, null,null);
		repoPrenda.createPrenda(buzo);

		guardarropa.agregarPrenda(remera);
		guardarropa.agregarPrenda(pantalonA);
		guardarropa.agregarPrenda(zapatoA);
		guardarropa.agregarPrenda(buzo);
		
		withTransaction(() -> entityManager().flush());
		
		Atuendo atuendoConBuzo = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA, buzo));
		Atuendo atuendoSinBuzo = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA));

		Set<Atuendo> atuendosGenerados = guardarropa.atuendos();
		List<Atuendo> atuendosEsperados = Arrays.asList(atuendoConBuzo, atuendoSinBuzo);

		Assert.assertEquals(atuendosGenerados.size(), atuendosEsperados.size());
		atuendosEsperados.forEach(atuendoEsperado -> Assert.assertTrue(atuendosGenerados.stream().anyMatch(atuendoGenerado -> atuendoGenerado.mismoAtuendo(atuendoEsperado))));
	}

	@Test
	public void generaAtuendosDeVariasCapas() {

		Guardarropa guardarropa = RepositorioGuardarropas.getInstance().createGuardarropas(new Guardarropa());
		Prenda buzo = new Prenda(TipoDePrendaFactory.getInstance().buzo(),Material.ALGODON, Color.black, null,null);
		Prenda chaleco = new Prenda(TipoDePrendaFactory.getInstance().chaleco(),Material.CUERO, Color.black, null,null);

		repoPrenda.createPrenda(buzo);
		repoPrenda.createPrenda(chaleco);

		guardarropa.agregarPrenda(remera);
		guardarropa.agregarPrenda(pantalonA);
		guardarropa.agregarPrenda(zapatoA);
		guardarropa.agregarPrenda(buzo);
		guardarropa.agregarPrenda(chaleco);
		
		withTransaction(() -> entityManager().flush());

		Atuendo atuendoConBuzo = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA, buzo));
		Atuendo atuendoConBuzoYChaleco = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA, buzo, chaleco));
		Atuendo atuendoConChaleco = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA, chaleco));
		Atuendo atuendoSimple = new Atuendo(Arrays.asList(remera, pantalonA, zapatoA));

		Set<Atuendo> atuendosGenerados = guardarropa.atuendos();
		List<Atuendo> atuendosEsperados = Arrays.asList(atuendoConBuzo, atuendoSimple, atuendoConChaleco, atuendoConBuzoYChaleco);

		Assert.assertEquals(atuendosGenerados.size(), atuendosEsperados.size());
		atuendosEsperados.forEach(atuendoEsperado -> Assert.assertTrue(atuendosEsperados.stream().anyMatch(atuendoGenerado -> atuendoGenerado.mismoAtuendo(atuendoEsperado))));
	}

}
