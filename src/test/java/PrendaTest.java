import java.awt.Color;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import que_me_pongo.prenda.Categoria;
import que_me_pongo.prenda.ColoresIgualesException;
import que_me_pongo.prenda.Material;
import que_me_pongo.prenda.MaterialInvalidoException;
import que_me_pongo.prenda.Prenda;
import que_me_pongo.prenda.Tipo;
import que_me_pongo.prenda.TipoDePrenda;
import que_me_pongo.prenda.TipoDePrendaFactory;

public class PrendaTest {

	@Test
	public void crearRemeraMangaCortaSinSecundario() {
		Prenda remera = new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(), Material.ALGODON, Color.BLACK, null, null);

		//PrendaFactory factory = new PrendaFactory();
		//Prenda remera = factory.crearRemeraMangaCorta(Material.SEDA, Color.BLACK, null);

		Assert.assertEquals(Tipo.REMERAMANGACORTA, remera.getTipo());
		Assert.assertEquals(Categoria.SUPERIOR, remera.getCategoria());
		Assert.assertEquals(Material.ALGODON, remera.getMaterial());
		Assert.assertEquals(Color.BLACK, remera.getColorPrimario());
		Assert.assertEquals(null, remera.getColorSecundario());
	}
	
	@Test
	public void crearRemeraMangaCortaConSecundario() {
		Prenda remera = new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(), Material.ALGODON, Color.BLACK, Color.WHITE, null);
		//PrendaFactory factory = new PrendaFactory();
		//Prenda remera = factory.crearRemeraMangaCorta(Material.SEDA, Color.BLACK, Color.WHITE);

		Assert.assertEquals(Tipo.REMERAMANGACORTA, remera.getTipo());
		Assert.assertEquals(Categoria.SUPERIOR, remera.getCategoria());
		Assert.assertEquals(Material.ALGODON, remera.getMaterial());
		Assert.assertEquals(Color.BLACK, remera.getColorPrimario());
		Assert.assertEquals(Color.WHITE, remera.getColorSecundario());
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void deberiaTirarExcepcionNullPointerPorTipo() throws Exception {
		expectedEx.expect(NullPointerException.class);
		
		new Prenda(new TipoDePrenda(null, Categoria.SUPERIOR, Arrays.asList(Material.ALGODON), 1,3), Material.ALGODON, Color.BLACK, null, null);
	}
	
	@Test
	public void deberiaTirarExcepcionNullPointerPorCategoria() throws Exception {
		expectedEx.expect(NullPointerException.class);
		
		new Prenda(new TipoDePrenda(Tipo.REMERAMANGACORTA, null, Arrays.asList(Material.ALGODON), 1,1), Material.ALGODON, Color.BLACK, null, null);
	}
	
	@Test
	public void deberiaTirarExcepcionNullPointerPorMaterial() throws Exception {
		expectedEx.expect(NullPointerException.class);
		
		new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(), null, Color.BLACK, null, null);
	}
	
	@Test
	public void deberiaTirarExcepcionNullPointerPorColorPrincipal() throws Exception {
		expectedEx.expect(NullPointerException.class);
		
		new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(), Material.ALGODON, null, null, null);
	}

	@Test
	public void deberiaTirarExcepcionColoresIguales() throws Exception {
	    expectedEx.expect(ColoresIgualesException.class);
	    expectedEx.expectMessage("Los colores son iguales");
	    // Codigo que deberia tirar la excepcion
	    new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(), Material.ALGODON, Color.black, Color.black, null);
	}

	@Test
	public void deberiaTirarExcepcionMaterialInvalido() throws Exception {
	    expectedEx.expect(MaterialInvalidoException.class);
	    expectedEx.expectMessage("Material invalido");
	    // Codigo que deberia tirar la excepcion
	    new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(), Material.CUERO, Color.black, Color.black, null);
	}
	
}
