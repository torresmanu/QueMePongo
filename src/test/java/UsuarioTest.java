import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import que_me_pongo.atuendo.Atuendo;
import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.prenda.Material;
import que_me_pongo.prenda.Prenda;
import que_me_pongo.prenda.TipoDePrendaFactory;
import que_me_pongo.usuario.TipoUsuario;
import que_me_pongo.usuario.Usuario;
import que_me_pongo.usuario.UsuarioGratuitoNoTieneLugarException;

import org.junit.Rule;
import org.junit.Test;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;


public class UsuarioTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
	Prenda remeraA = new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(),Material.SEDA, Color.BLACK, null,null);
	Prenda remeraB = new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(),Material.ALGODON, Color.WHITE, null,null);
	Prenda pantalonA = new Prenda(TipoDePrendaFactory.getInstance().shorts(),Material.ALGODON, Color.BLACK, null,null);
	Prenda pantalonB = new Prenda(TipoDePrendaFactory.getInstance().shorts(),Material.ALGODON, Color.PINK, null,null);
	Prenda accesorioA = new Prenda(TipoDePrendaFactory.getInstance().anteojos(),Material.PLASTICO, Color.ORANGE, null,null);
	Prenda zapatoA = new Prenda(TipoDePrendaFactory.getInstance().zapatosDeTacon(),Material.CUERO, Color.BLUE, null,null);
	Prenda zapatoB = new Prenda(TipoDePrendaFactory.getInstance().zapatosDeTacon(),Material.CUERO, Color.GREEN, null,null);

	Atuendo atuendoA = new Atuendo(Arrays.asList(remeraA, pantalonA, zapatoA, accesorioA));
	Atuendo atuendoB = new Atuendo(Arrays.asList(remeraA, pantalonA, zapatoB, accesorioA));
	Atuendo atuendoC = new Atuendo(Arrays.asList(remeraA, pantalonA, zapatoA));
	Atuendo atuendoD = new Atuendo(Arrays.asList(remeraA, pantalonA, zapatoB));
	Atuendo atuendoE = new Atuendo(Arrays.asList(remeraA, pantalonB, zapatoA, accesorioA));
	Atuendo atuendoF = new Atuendo(Arrays.asList(remeraA, pantalonB, zapatoB, accesorioA));
	Atuendo atuendoG = new Atuendo(Arrays.asList(remeraA, pantalonB, zapatoA));
	Atuendo atuendoH = new Atuendo(Arrays.asList(remeraA, pantalonB, zapatoB));
	Atuendo atuendoI = new Atuendo(Arrays.asList(remeraB, pantalonA, zapatoA, accesorioA));
	Atuendo atuendoJ = new Atuendo(Arrays.asList(remeraB, pantalonA, zapatoB, accesorioA));
	Atuendo atuendoK = new Atuendo(Arrays.asList(remeraB, pantalonA, zapatoA));
	Atuendo atuendoL = new Atuendo(Arrays.asList(remeraB, pantalonA, zapatoB));
	Atuendo atuendoM = new Atuendo(Arrays.asList(remeraB, pantalonB, zapatoA, accesorioA));
	Atuendo atuendoN = new Atuendo(Arrays.asList(remeraB, pantalonB, zapatoB, accesorioA));
	Atuendo atuendoO = new Atuendo(Arrays.asList(remeraB, pantalonB, zapatoA));
	Atuendo atuendoP = new Atuendo(Arrays.asList(remeraB, pantalonB, zapatoB));


	@Test
	public void usuarioDevuelveLosAtuendosCorrectamente() {
		EntityManager em = entityManager();

		Usuario usuario = new Usuario("DDS",null,TipoUsuario.PREMIUM, "");
		em.persist(usuario);
		Guardarropa guardarropa1 = new Guardarropa(),
								guardarropa2 = new Guardarropa();
		em.persist(guardarropa1);
		em.persist(guardarropa2);

		usuario.agregarGuardarropas(guardarropa1);
		usuario.agregarGuardarropas(guardarropa2);

		Set<Atuendo> conjunto1 = new HashSet<>();
		conjunto1.addAll(Arrays.asList(atuendoA, atuendoC));

		Set<Atuendo> conjunto2 = new HashSet<>();
		conjunto2.addAll(Arrays.asList(atuendoP));

		em.persist(remeraA);
		em.persist(remeraB);
		em.persist(pantalonA);
		em.persist(pantalonB);
		em.persist(zapatoA);
		em.persist(zapatoB);
		em.persist(accesorioA);

		usuario.agregarPrenda(remeraA,guardarropa1);
		usuario.agregarPrenda(pantalonA,guardarropa1);
		usuario.agregarPrenda(zapatoA,guardarropa1);
		usuario.agregarPrenda(accesorioA,guardarropa1);

		usuario.agregarPrenda(remeraB,guardarropa2);
		usuario.agregarPrenda(pantalonB,guardarropa2);
		usuario.agregarPrenda(zapatoB,guardarropa2);

		Set<Atuendo> atuendos = usuario.atuendos();

		Assert.assertEquals(conjunto1.size() + conjunto2.size(), atuendos.size());
		conjunto1.forEach(atuendo -> Assert.assertTrue(atuendos.stream().anyMatch(recibido -> recibido.mismoAtuendo(atuendo))));
		conjunto2.forEach(atuendo -> Assert.assertTrue(atuendos.stream().anyMatch(recibido -> recibido.mismoAtuendo(atuendo))));
	}




	@Test
	public void usuarioPremiumPuedeAgregarMuchasPrendas() {
		Usuario usuario = new Usuario("DDS",null,TipoUsuario.PREMIUM, "");
		Guardarropa guardarropa = new Guardarropa();
		usuario.agregarGuardarropas(guardarropa);

		usuario.agregarPrenda(remeraA, guardarropa);
		usuario.agregarPrenda(pantalonA,guardarropa);
		usuario.agregarPrenda(zapatoA,guardarropa);
		usuario.agregarPrenda(accesorioA,guardarropa);

		usuario.agregarPrenda(remeraB,guardarropa);
		usuario.agregarPrenda(pantalonB,guardarropa);
		usuario.agregarPrenda(zapatoB,guardarropa);


		Assert.assertEquals(7, guardarropa.cantidadPrendas());
	}


	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void usuarioGratuitoNoPuedeAgregarMuchasPrendas() throws Exception {

		expectedEx.expect(UsuarioGratuitoNoTieneLugarException.class);
		//expectedEx.expectMessage("Su guardarropas esta lleno, si desea tener mas lugar puede hacerse socio premium");

		Guardarropa guardarropa = new Guardarropa();
		Usuario usuario = new Usuario("DDS",null,TipoUsuario.GRATUITO, "");
		usuario.agregarGuardarropas(guardarropa);

		usuario.agregarPrenda(remeraA,guardarropa);
		usuario.agregarPrenda(pantalonA,guardarropa);
		usuario.agregarPrenda(zapatoA,guardarropa);
		usuario.agregarPrenda(accesorioA,guardarropa);
		usuario.agregarPrenda(remeraB,guardarropa);
		usuario.agregarPrenda(pantalonB,guardarropa);
		usuario.agregarPrenda(zapatoB,guardarropa);

	}

}
