import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;

import que_me_pongo.prenda.Material;
import que_me_pongo.prenda.Prenda;
import que_me_pongo.proveedorClima.PronosticoClima;
import que_me_pongo.sugeridor.Sugeridor;
import que_me_pongo.usuario.Usuario;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import que_me_pongo.atuendo.Atuendo;

@RunWith(MockitoJUnitRunner.class)
public class SugeridorTest {
	static Prenda remeraMangaCorta = PrendaFactory.remeraMangaCorta(Material.ALGODON, Color.black, null),
				 remeraMangaLarga = PrendaFactory.remeraMangaLarga(Material.ALGODON, Color.black, null),
				 buzo = PrendaFactory.buzo(Material.ALGODON, Color.black, null),
				 zapatosDeTacon = PrendaFactory.zapatosDeTacon(Material.CUERO, Color.black, null),
				 shorts = PrendaFactory.shorts(Material.ALGODON, Color.black, null),
				 pantalon = PrendaFactory.pantalon(Material.ALGODON, Color.black, null),
				 guantes = PrendaFactory.guantes(Material.ALGODON, Color.black, null);

	static Atuendo atuendo1 = new Atuendo(Arrays.asList(remeraMangaCorta, shorts, zapatosDeTacon)), //3NA - 18°
							 atuendo2 = new Atuendo(Arrays.asList(remeraMangaCorta, shorts, zapatosDeTacon, guantes)), //5NA - 15°
							 atuendo3 = new Atuendo(Arrays.asList(remeraMangaCorta, shorts, zapatosDeTacon, buzo)), //6NA - 13,5°
							 atuendo4 = new Atuendo(Arrays.asList(remeraMangaCorta, shorts, zapatosDeTacon, buzo, guantes)), //8NA - 10,5°
							 atuendo5 = new Atuendo(Arrays.asList(remeraMangaCorta, pantalon, zapatosDeTacon)), //4NA - 16.5°
							 atuendo6 = new Atuendo(Arrays.asList(remeraMangaCorta, pantalon, zapatosDeTacon, guantes)), //6NA - 13,5°
							 atuendo7 = new Atuendo(Arrays.asList(remeraMangaCorta, pantalon, zapatosDeTacon, buzo)), //7NA - 12°
							 atuendo8 = new Atuendo(Arrays.asList(remeraMangaCorta, pantalon, zapatosDeTacon, buzo, guantes)), //9NA - 9°
							 atuendo9 = new Atuendo(Arrays.asList(remeraMangaLarga, shorts, zapatosDeTacon)), //3.5NA - 17.25°
							 atuendo10 = new Atuendo(Arrays.asList(remeraMangaLarga, shorts, zapatosDeTacon, guantes)), //5.5NA - 14.25°
							 atuendo11 = new Atuendo(Arrays.asList(remeraMangaLarga, shorts, zapatosDeTacon, buzo)), //6.5NA - 12.75
							 atuendo12 = new Atuendo(Arrays.asList(remeraMangaLarga, shorts, zapatosDeTacon, buzo, guantes)), //8.5NA - 9.75°
							 atuendo13 = new Atuendo(Arrays.asList(remeraMangaLarga, pantalon, zapatosDeTacon)), //4.5NA - 15.75°
							 atuendo14 = new Atuendo(Arrays.asList(remeraMangaLarga, pantalon, zapatosDeTacon, guantes)), //6.5NA - 12.75°
							 atuendo15 = new Atuendo(Arrays.asList(remeraMangaLarga, pantalon, zapatosDeTacon, buzo)), //7.5NA - 11.25°
							 atuendo16 = new Atuendo(Arrays.asList(remeraMangaLarga, pantalon, zapatosDeTacon, buzo, guantes)); //9.5NA - 8.25° 
	
	static Set<Atuendo> atuendos = new HashSet<>(Arrays.asList(atuendo1, atuendo2, atuendo3, atuendo4,
																														atuendo5, atuendo6, atuendo7, atuendo8,
																														atuendo9, atuendo10, atuendo11, atuendo12,
																														atuendo13, atuendo14, atuendo15, atuendo16));
	
	@Mock
	Usuario usuario;
	
	public static <T1, T2> boolean listContainsIgnoreOrder(Collection<List<T1>> list1, List<T2> list2) {
		return list1.stream().anyMatch(element1 -> new HashSet<>(element1).equals(new HashSet<>(list2)));
	}
	
	@Test
	public void funcionaRangoBasicoA18Grados() {
		LocalDate fecha = LocalDate.now();
		Sugeridor sug = new Sugeridor(2, 4, 1);
		Set<Atuendo> atuendosFinales = new HashSet<>(
																						Arrays.asList(atuendo1, atuendo5, atuendo9));
		testDeSugerencia(atuendosFinales, sug, fecha, 18.);
	}
	
	@Test
	public void usaRangoExtendidoA18Grados() {
		LocalDate fecha = LocalDate.now();
		Sugeridor sug = new Sugeridor(0.5, 2, 2);
		Set<Atuendo> atuendosFinales = new HashSet<>(
																						Arrays.asList(atuendo1, atuendo5, atuendo9));
		testDeSugerencia(atuendosFinales, sug, fecha, 18.);
	}
	
	@Test
	public void funcionaRangoBasicoAGradosNoRedondos() {
		LocalDate fecha = LocalDate.now();
		Sugeridor sug = new Sugeridor(2, 4, 1);
		Set<Atuendo> atuendosFinales = new HashSet<>(
																						Arrays.asList(atuendo3, atuendo4, atuendo6, atuendo7,
																								atuendo10, atuendo11, atuendo14, atuendo15));
		testDeSugerencia(atuendosFinales, sug, fecha, 12.37);
	}
	
	@Test
	public void usaRangoExtendidoAGradosNoRedondos() {
		LocalDate fecha = LocalDate.now();
		Sugeridor sug = new Sugeridor(0.5, 2, 3);
		Set<Atuendo> atuendosFinales = new HashSet<>(
																						Arrays.asList(atuendo3, atuendo4, atuendo6, atuendo7, 
																								atuendo10, atuendo11, atuendo14, atuendo15));
		testDeSugerencia(atuendosFinales, sug, fecha, 12.37);
	}
	
	@Test 
	public void retornaLoQuePuedeSinoAlcanza() {
		LocalDate fecha = LocalDate.now();
		Sugeridor sug = new Sugeridor(0.5, 2, 10);
		Set<Atuendo> atuendosFinales = new HashSet<Atuendo>(
				Arrays.asList(atuendo1, atuendo5, atuendo9));
		
		testDeSugerencia(atuendosFinales, sug, fecha, 18.);
	}
	
	public void testDeSugerencia(Set<Atuendo> esperados, Sugeridor sugeridor, LocalDate fecha, Double temp) {
		Set<Atuendo> resultado = sugeridor.sugerir(atuendos, new PronosticoClima(LocalDateTime.now(), temp), usuario);
		
		Assert.assertEquals(esperados.size(), resultado.size());
		esperados.forEach(esperado -> Assert.assertTrue(resultado.stream().anyMatch(result -> result.mismoAtuendo(esperado))));
	}

}
