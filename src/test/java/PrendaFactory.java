import que_me_pongo.*;
import que_me_pongo.prenda.Material;
import que_me_pongo.prenda.Prenda;
import que_me_pongo.prenda.RepositorioPrendas;
import que_me_pongo.prenda.TipoDePrenda;
import que_me_pongo.prenda.TipoDePrendaFactory;

import java.awt.Color;


public class PrendaFactory {
    
    static private Prenda crearPrenda(TipoDePrenda tipo, Material material, Color colorPrimario, Color colorSecundario) {
    	return RepositorioPrendas.getInstance().createPrenda(new Prenda(tipo, material, colorPrimario, colorSecundario, null));	
    }
    
    static public Prenda anteojos(Material material, Color colorPrimario, Color colorSecundario) {
    	return crearPrenda(TipoDePrendaFactory.getInstance().anteojos(), material, colorPrimario, colorSecundario);
    }
    
    static public Prenda remeraMangaCorta(Material material, Color colorPrimario, Color colorSecundario) {	
    	return crearPrenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(), material, colorPrimario, colorSecundario);
    }
    
    static public Prenda remeraMangaLarga(Material material, Color colorPrimario, Color colorSecundario) {	
    	return crearPrenda(TipoDePrendaFactory.getInstance().remeraMangaLarga(), material, colorPrimario, colorSecundario);
    }
    
    static public Prenda buzo(Material material, Color colorPrimario, Color colorSecundario) {
    	return crearPrenda(TipoDePrendaFactory.getInstance().buzo(), material, colorPrimario, colorSecundario);
    }
    
    static public Prenda shorts(Material material, Color colorPrimario, Color colorSecundario) {
    	return crearPrenda(TipoDePrendaFactory.getInstance().shorts(), material, colorPrimario, colorSecundario);
    }
    
    static public Prenda pantalon(Material material, Color colorPrimario, Color colorSecundario) {
    	return crearPrenda(TipoDePrendaFactory.getInstance().pantalon(), material, colorPrimario, colorSecundario);
    }
    
    static public Prenda zapatosDeTacon(Material material, Color colorPrimario, Color colorSecundario) {
    	return crearPrenda(TipoDePrendaFactory.getInstance().zapatosDeTacon(), material, colorPrimario, colorSecundario);
    }

    static public Prenda chaleco(Material material, Color colorPrimario, Color colorSecundario) {
    	return crearPrenda(TipoDePrendaFactory.getInstance().chaleco(), material, colorPrimario, colorSecundario);
    }
    
    static public Prenda guantes(Material material, Color colorPrimario, Color colorSecundario) {	
    	return crearPrenda(TipoDePrendaFactory.getInstance().guantes(), material, colorPrimario, colorSecundario);
    }
}
