import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import que_me_pongo.atuendo.Atuendo;
import que_me_pongo.evento.Evento;
import que_me_pongo.evento.EventoJob;
import que_me_pongo.evento.RepositorioEventos;
import que_me_pongo.evento.repetidores.RepeticionDeEvento;
import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.guardarropa.RepositorioGuardarropas;
import que_me_pongo.prenda.Material;
import que_me_pongo.prenda.Prenda;
import que_me_pongo.prenda.RepositorioPrendas;
import que_me_pongo.prenda.TipoDePrendaFactory;
import que_me_pongo.proveedorClima.InstanciaProveedorClima;
import que_me_pongo.proveedorClima.PronosticoClima;
import que_me_pongo.proveedorClima.ProveedorClima;
import que_me_pongo.usuario.RepositorioUsuarios;
import que_me_pongo.usuario.TipoUsuario;
import que_me_pongo.usuario.Usuario;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;

import javax.persistence.EntityManager;

@RunWith(MockitoJUnitRunner.class)
public class EventoJobTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
    Usuario usuario = new Usuario("Julian", null, TipoUsuario.PREMIUM, "");
    Guardarropa guardarropa = new Guardarropa();

    @Mock
    ProveedorClima prov;

    @Before
    public void setProveedorClima() {
        InstanciaProveedorClima.setInstancia(prov);
        RepositorioUsuarios.getInstance().createUsuario(usuario);
        RepositorioGuardarropas.getInstance().createGuardarropas(guardarropa);
    }

    @Test
    public void sugiereEventosProximos() throws Exception {
        LocalDateTime ahora = LocalDateTime.now();
        Mockito.when(prov.getPronostico()).thenReturn(Arrays.asList(new PronosticoClima(ahora, 15),
                new PronosticoClima(ahora.plusDays(1), 15),
                new PronosticoClima(ahora.plusDays(2), 15),
                new PronosticoClima(ahora.plusDays(3), 15)));

        Evento evento1 = RepositorioEventos.getInstance().crearEvento(ahora.plusDays(1), usuario, guardarropa, "Ir al campo", new ArrayList(), RepeticionDeEvento.NOREPITE);
        Evento evento2 = RepositorioEventos.getInstance().crearEvento(ahora.plusDays(2), usuario, guardarropa, "Cumpleaños", new ArrayList(), RepeticionDeEvento.NOREPITE);
        Evento evento3 = RepositorioEventos.getInstance().crearEvento(ahora.plusDays(4), usuario, guardarropa, "Casamiento", new ArrayList(), RepeticionDeEvento.NOREPITE);
        Evento evento4 = RepositorioEventos.getInstance().crearEvento(ahora.plusDays(5), usuario, guardarropa, "Bautismo", new ArrayList(), RepeticionDeEvento.NOREPITE);

        EventoJob job = new EventoJob();

        job.execute(null);

        Assert.assertTrue(evento1.getSugirio());
        Assert.assertTrue(evento2.getSugirio());
        Assert.assertFalse(evento3.getSugirio());
        Assert.assertFalse(evento4.getSugirio());
    }

    @Test
    public void resugiereEventosConCambiosClimaticos() throws Exception {
        LocalDateTime ahora = LocalDateTime.now();

        Mockito.when(prov.getPronostico()).thenReturn(Arrays.asList(new PronosticoClima(ahora, 15),
                new PronosticoClima(ahora.plusDays(1), 15),
                new PronosticoClima(ahora.plusDays(2), 15),
                new PronosticoClima(ahora.plusDays(3), 15)));

        Evento evento1 = new Evento(ahora.plusDays(1), usuario, guardarropa, "Ir al campo", new ArrayList());
        Evento evento2 = new Evento(ahora.plusDays(2), usuario, guardarropa, "Cumpleaños", new ArrayList());
        Evento evento3 = new Evento(ahora.plusDays(4), usuario, guardarropa, "Casamiento", new ArrayList());
        Evento evento4 = new Evento(ahora.plusDays(5), usuario, guardarropa, "Bautismo", new ArrayList());

        RepositorioEventos.getInstance().agendar(evento1);
        RepositorioEventos.getInstance().agendar(evento2);
        RepositorioEventos.getInstance().agendar(evento3);
        RepositorioEventos.getInstance().agendar(evento4);

        EventoJob job = new EventoJob();

        job.execute(null);
        
        List<Atuendo> sugEv1 = evento1.getSugerenciasPendientes();
        List<Atuendo> sugEv2 = evento2.getSugerenciasPendientes();

        Mockito.when(prov.getPronostico()).thenReturn(Arrays.asList(new PronosticoClima(ahora, 0),
                new PronosticoClima(ahora.plusDays(1), 0),
                new PronosticoClima(ahora.plusDays(2), 0),
                new PronosticoClima(ahora.plusDays(3), 0)));

        job.execute(null);
        
        Assert.assertTrue(evento1.getSugirio());
        Assert.assertNotSame(sugEv1, evento1.getSugerenciasPendientes());
        Assert.assertTrue(evento2.getSugirio());
        Assert.assertNotSame(sugEv2, evento2.getSugerenciasPendientes());
        Assert.assertNull(evento3.getSugerenciasPendientes());
        Assert.assertNull(evento4.getSugerenciasPendientes());

    }
    
    @Test
    public void seGenerarLasRepeticiones() throws Exception {
        LocalDateTime ahora = LocalDateTime.now();

        Mockito.when(prov.getPronostico()).thenReturn(Arrays.asList(new PronosticoClima(ahora, 15),
                new PronosticoClima(ahora.plusDays(1), 15),
                new PronosticoClima(ahora.plusDays(2), 15),
                new PronosticoClima(ahora.plusDays(3), 15)));

        Evento evento = new Evento(ahora.plusDays(1), usuario, guardarropa, "Ir al campo", new ArrayList(), RepeticionDeEvento.DIARIO);

        RepositorioEventos.getInstance().agendar(evento);
        
        RepositorioPrendas repoPrendas = RepositorioPrendas.getInstance();
        
        Prenda remera = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(), Material.SEDA, Color.BLACK, null,null));
        Prenda remeraB = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().remeraMangaCorta(),Material.ALGODON, Color.WHITE, null,null));
        Prenda pantalonA = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().shorts(),Material.ALGODON, Color.BLACK, null,null));
        Prenda pantalonB = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().shorts(),Material.ALGODON, Color.PINK, null,null));
        Prenda accesorioA = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().anteojos(),Material.PLASTICO, Color.ORANGE, null,null));
        Prenda zapatoA = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().zapatosDeTacon(),Material.CUERO, Color.BLUE, null,null));
        Prenda zapatoB = repoPrendas.createPrenda(new Prenda(TipoDePrendaFactory.getInstance().zapatosDeTacon(),Material.CUERO, Color.GREEN, null,null));
        
        guardarropa.agregarPrenda(remera);
        guardarropa.agregarPrenda(remeraB);
        guardarropa.agregarPrenda(pantalonA);
        guardarropa.agregarPrenda(pantalonB);
        guardarropa.agregarPrenda(accesorioA);
        guardarropa.agregarPrenda(zapatoA);
        guardarropa.agregarPrenda(zapatoB);

        EventoJob job = new EventoJob();

        job.execute(null);
        
        Assert.assertEquals(2, RepositorioEventos.getInstance().getEventos().size());

    }
}
