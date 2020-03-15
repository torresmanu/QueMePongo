package que_me_pongo.sugeridor;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Range;

import que_me_pongo.atuendo.Atuendo;
import que_me_pongo.prenda.Prenda;
import que_me_pongo.proveedorClima.PronosticoClima;
import que_me_pongo.usuario.Usuario;

public class Sugeridor {
	private double margenBase, margenExtendido;
	private int cantParaExtender;
	
	public Sugeridor(double margenBase, double margenExtendido, int cantParaExtender)
	{
		this.margenBase = margenBase;
		this.margenExtendido = margenExtendido;
		this.cantParaExtender = cantParaExtender;
	}
	
	public Set<Atuendo> sugerir(Set<Atuendo> atuendos, PronosticoClima pronostico, Usuario usuario) {
		Set<Atuendo> sugeridos = this.filtrar(atuendos, pronostico, usuario, margenBase);
		
		if(sugeridos.size() <= this.cantParaExtender)
			sugeridos = this.filtrar(atuendos, pronostico, usuario, margenExtendido);
		
		return sugeridos;
	}
	
	private Set<Atuendo> filtrar(Set<Atuendo> atuendos, PronosticoClima pronostico, Usuario usuario, double margen) {
		return atuendos.stream().
		 filter(atuendo -> sugerirAtuendo(atuendo, pronostico, usuario, margen)).
		 collect(Collectors.toSet());
	}
	
	private boolean sugerirAtuendo(Atuendo atuendo, PronosticoClima pronostico, Usuario usuario, double margen) {
		double nivelAbrigoTotal = atuendo.getPrendas().stream().reduce(.0, this::reducirNivelAbrigo, (n1, n2) -> n1 + n2);
		double pesoPreferencias = atuendo.getPrendas().stream().reduce(.0, (acc, prenda) -> acc - usuario.getPreferencia(prenda.getCategoria()), (n1, n2) -> n1 + n2);
		return Range.closed(pronostico.getTemperatura()-margen, pronostico.getTemperatura()+margen).
								 contains(aTemperatura(nivelAbrigoTotal + pesoPreferencias));
	}
	
	private double reducirNivelAbrigo(double nivelPrenda1, Prenda prenda2) {
		return nivelPrenda1 + prenda2.getNivelAbrigo();
	}
	
	private double aTemperatura(double nivelAbrigo)
	{
		/*
		 * Una funcion lineal que asume lo siguiente:
		 * -Con tres prendas basicas obligatorias, hay un nivel de abrigo de tres y eso sirve para dieciocho grados
		 * -Por cada tres grados que desciende la temperatura, aumento en dos el nivel de abrigo
		 */
		return -1.5 * nivelAbrigo + 22.5;
	}
}
