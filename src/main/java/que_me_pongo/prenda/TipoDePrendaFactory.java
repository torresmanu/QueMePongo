package que_me_pongo.prenda;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

public class TipoDePrendaFactory {
	static TipoDePrendaFactory instance = new TipoDePrendaFactory();
	
	public static TipoDePrendaFactory getInstance() {
		return instance;
	}
	
	public static TipoDePrenda parse(String nombre) throws NoSuchMethodException {
		Method metodoPrenda = getMethodsIgnoreCase(nombre);
		try {
			return (TipoDePrenda) metodoPrenda.invoke(TipoDePrendaFactory.getInstance());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
	}
	public static Method getMethodsIgnoreCase(String methodName) {
		return Arrays.stream(TipoDePrendaFactory.class.getMethods())
				.filter(m -> m.getName().equalsIgnoreCase(methodName))
				.collect(Collectors.toList()).get(0);
	}
	private TipoDePrenda tipoGenerico(TipoDePrenda retornoEnFallo) {
		RepositorioPrendas repo = RepositorioPrendas.getInstance();
		try {
			return repo.getTipoDePrenda(retornoEnFallo.getTipo());
		}
		catch(NoResultException noResultEx)
		{
			return repo.createTipoDePrenda(retornoEnFallo);
		}
	}
	
	public TipoDePrenda aros() {
		return tipoGenerico(new TipoDePrenda(Tipo.AROS, Categoria.ACCESORIO, Arrays.asList(Material.PLASTICO), 1, 0));
	}
	
	public TipoDePrenda anteojos() {
		List<Material> materiales = new LinkedList<Material>();
  	materiales.add(Material.PLASTICO);
  	return tipoGenerico(new TipoDePrenda(Tipo.ANTEOJOS, Categoria.ACCESORIO, materiales, 2, 0));
	}
	
	public TipoDePrenda remeraMangaCorta() {
		List<Material> materiales = new LinkedList<Material>();
  	materiales.add(Material.ALGODON);
  	materiales.add(Material.SEDA);
  	materiales.add(Material.DRIFIT);
  	return tipoGenerico(new TipoDePrenda(Tipo.REMERAMANGACORTA, Categoria.SUPERIOR, materiales, 0, 1));
	}
	
	public TipoDePrenda remeraMangaLarga() {
		List<Material> materiales = new LinkedList<Material>();
  	materiales.add(Material.ALGODON);
  	materiales.add(Material.SEDA);
  	materiales.add(Material.DRIFIT);
  	return tipoGenerico(new TipoDePrenda(Tipo.REMERAMANGALARGA, Categoria.SUPERIOR, materiales, 0, 1.5));
	}
	
	public TipoDePrenda shorts() {
		List<Material> materiales = new LinkedList<Material>();
  	materiales.add(Material.ALGODON);
  	materiales.add(Material.DRIFIT);
  	return tipoGenerico(new TipoDePrenda(Tipo.SHORTS, Categoria.INFERIOR, materiales, 0, 1));
	}
	
	public TipoDePrenda pantalon() {
		List<Material> materiales = new LinkedList<Material>();
  	materiales.add(Material.ALGODON);
  	materiales.add(Material.DRIFIT);
  	return tipoGenerico(new TipoDePrenda(Tipo.PANTALON, Categoria.INFERIOR, materiales, 0, 2));
	}
	
	public TipoDePrenda buzo() {
		List<Material> materiales = new LinkedList<Material>();
  	materiales.add(Material.ALGODON);
  	materiales.add(Material.SEDA);
  	return tipoGenerico(new TipoDePrenda(Tipo.BUZO, Categoria.SUPERIOR, materiales, 2, 3));
	}
	
	public TipoDePrenda zapatosDeTacon() {
		List<Material> materiales = new LinkedList<Material>();
  	materiales.add(Material.CUERO);
  	return tipoGenerico(new TipoDePrenda(Tipo.ZAPATOSDETACON, Categoria.CALZADO, materiales, 0, 1));
	}
	
	public TipoDePrenda chaleco() {
		List<Material> materiales = Arrays.asList(Material.ALGODON, Material.CUERO);
		return tipoGenerico(new TipoDePrenda(Tipo.CHALECO, Categoria.SUPERIOR, materiales, 1, 2));
	}
	
	public TipoDePrenda guantes() {
		List<Material> materiales = Arrays.asList(Material.ALGODON, Material.CUERO);
		return tipoGenerico(new TipoDePrenda(Tipo.GUANTES, Categoria.ACCESORIO, materiales, 3, 2));
	}
}
