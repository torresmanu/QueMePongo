import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import que_me_pongo.evento.Evento;
import que_me_pongo.evento.RepositorioEventos;
import que_me_pongo.evento.repetidores.RepeticionDeEvento;
import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.guardarropa.RepositorioGuardarropas;
import que_me_pongo.proveedorClima.PronosticoClima;
import que_me_pongo.sugeridor.Sugeridor;
import que_me_pongo.usuario.RepositorioUsuarios;
import que_me_pongo.usuario.TipoUsuario;
import que_me_pongo.usuario.Usuario;

public class EventoTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
    Usuario usuario = new Usuario("Julian", null, TipoUsuario.PREMIUM, "");
    Guardarropa guardarropa = new Guardarropa();


    @Before
    public void setProveedorClima() {
        RepositorioUsuarios.getInstance().createUsuario(usuario);
        RepositorioGuardarropas.getInstance().createGuardarropas(guardarropa);
    }

    @Test
    public void creaUnaRepetecionAlSugerir() throws Exception {
        LocalDateTime ahora = LocalDateTime.now();

        Evento evento = RepositorioEventos.getInstance().crearEvento(ahora.plusDays(1), usuario, guardarropa, "Ir al campo", new ArrayList(), RepeticionDeEvento.DIARIO);
        
        evento.sugerir(new Sugeridor(3, 3, 3), new PronosticoClima(ahora, 15)); 
        Assert.assertEquals(2, RepositorioEventos.getInstance().getEventos().size());
        
    }
}
