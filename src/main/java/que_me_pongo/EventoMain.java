package que_me_pongo;

import que_me_pongo.evento.EventoJob;
import que_me_pongo.evento.RepositorioEventos;
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

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EventoMain {

    public static void main(String[] args) throws SchedulerException{
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        // Creacion una instacia de JobDetail
        JobDetail jobDetail = JobBuilder.newJob(EventoJob.class).build();

        // Creacion de un Trigger donde indicamos que el Job se ejecutara de inmediato y a partir de ahi en lapsos de 24 horas para siempre.
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("CronTrigger")
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInMinutes(1)    //Se ejecuta cada 24 horas
                        .repeatForever())           //Se ejecuta para siempre
                .build();

        // Registro dentro del Scheduler
        scheduler.scheduleJob(jobDetail, trigger);
        
        InstanciaProveedorClima.setInstancia(new ClimaOpenWeather());
        //new ExampleDataCreator().createData();
    }

}

