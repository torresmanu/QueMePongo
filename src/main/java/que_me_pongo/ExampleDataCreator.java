package que_me_pongo;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import que_me_pongo.evento.RepositorioEventos;
import que_me_pongo.evento.listeners.EventoListener;
import que_me_pongo.evento.repetidores.RepeticionDeEvento;
import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.guardarropa.RepositorioGuardarropas;
import que_me_pongo.prenda.Material;
import que_me_pongo.prenda.Prenda;
import que_me_pongo.prenda.RepositorioPrendas;
import que_me_pongo.prenda.TipoDePrendaFactory;
import que_me_pongo.proveedorClima.ClimaOpenWeather;
import que_me_pongo.proveedorClima.InstanciaProveedorClima;
import que_me_pongo.usuario.RepositorioUsuarios;
import que_me_pongo.usuario.TipoUsuario;
import que_me_pongo.usuario.Usuario;

public class ExampleDataCreator implements WithGlobalEntityManager, TransactionalOps{ 

	public void createData() {
		beginTransaction();
		TipoDePrendaFactory.getInstance().remeraMangaCorta();
		TipoDePrendaFactory.getInstance().shorts();
		TipoDePrendaFactory.getInstance().zapatosDeTacon();
		commitTransaction();
  	
		entityManager().clear();
		
		beginTransaction();
    Usuario usuario = RepositorioUsuarios.getInstance().createUsuario(new Usuario("Julian",null, TipoUsuario.PREMIUM, "password"));
    
    RepositorioPrendas repoPrendas = RepositorioPrendas.getInstance();

    Prenda remera = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(), Material.SEDA, Color.BLACK, null,null));
    Prenda remeraB = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(),Material.ALGODON, Color.WHITE, null,null));
    Prenda pantalonA = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().shorts(),Material.ALGODON, Color.BLACK, null,null));
    Prenda pantalonB = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().shorts(),Material.ALGODON, Color.PINK, null,null));
    Prenda accesorioA = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().anteojos(),Material.PLASTICO, Color.ORANGE, null,null));
    Prenda zapatoA = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().zapatosDeTacon(),Material.CUERO, Color.BLUE, null,null));
    Prenda zapatoB = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().zapatosDeTacon(),Material.CUERO, Color.GREEN, null,null));

    Guardarropa guardarropa = RepositorioGuardarropas.getInstance().createGuardarropas(new Guardarropa());
    guardarropa.agregarPrenda(remera);
    guardarropa.agregarPrenda(remeraB);
    guardarropa.agregarPrenda(pantalonA);
    guardarropa.agregarPrenda(pantalonB);
    guardarropa.agregarPrenda(accesorioA);
    guardarropa.agregarPrenda(zapatoA);
    guardarropa.agregarPrenda(zapatoB);
    LocalDateTime ahora = LocalDateTime.now();

    RepositorioEventos.getInstance().crearEvento(ahora.plusDays(1), usuario, guardarropa,"Ir al campo", new ArrayList<EventoListener>(), RepeticionDeEvento.NOREPITE);
    RepositorioEventos.getInstance().crearEvento(ahora.plusDays(1), usuario, guardarropa,"Cumpleaños", new ArrayList<EventoListener>(), RepeticionDeEvento.DIARIO);
    RepositorioEventos.getInstance().crearEvento(ahora.plusDays(4), usuario, guardarropa,"Casamiento", new ArrayList<EventoListener>(), RepeticionDeEvento.NOREPITE);
    RepositorioEventos.getInstance().crearEvento(ahora.plusDays(5), usuario, guardarropa,"Bautismo", new ArrayList<EventoListener>(), RepeticionDeEvento.NOREPITE);
    commitTransaction();
    entityManager().clear();
	}
}
