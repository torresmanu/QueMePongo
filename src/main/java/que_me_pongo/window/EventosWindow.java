package que_me_pongo.window;

import org.quartz.SchedulerException;
import org.uqbar.arena.bindings.DateTransformer;
import org.uqbar.arena.layout.HorizontalLayout;
import org.uqbar.arena.layout.VerticalLayout;
import org.uqbar.arena.widgets.*;
import org.uqbar.arena.widgets.Button;
import org.uqbar.arena.widgets.Label;
import org.uqbar.arena.widgets.Panel;
import org.uqbar.arena.widgets.tables.Column;
import org.uqbar.arena.windows.MainWindow;
import que_me_pongo.EventoMain;
import que_me_pongo.evento.Evento;
import org.uqbar.arena.widgets.tables.Table;
import que_me_pongo.evento.FiltradorDeEventos;

import java.awt.*;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class EventosWindow extends MainWindow<FiltradorDeEventos> {

    public EventosWindow() {
        super(FiltradorDeEventos.getInstance());
    }

    @Override
    public void createContents(Panel mainPanel) {
        this.setTitle("Filtro de eventos");
        mainPanel.setLayout(new HorizontalLayout());

        this.configurarInput(mainPanel);

        this.configurarOutput(mainPanel);

    }

    public void configurarInput(Panel mainPanel){

        Panel panelFechas = new Panel(mainPanel);
        panelFechas.setLayout(new VerticalLayout());

        new Label(panelFechas).setText("Fechas");

        Panel pCantidad = new Panel(panelFechas);
        pCantidad.setLayout(new HorizontalLayout());

        new Label(pCantidad).setText("Formato de fecha dd/mm/aaaa").setForeground(Color.GRAY);

        Panel pDesde = new Panel(panelFechas);
        pDesde.setLayout(new HorizontalLayout());
        new Label(pDesde).setText("Desde");
        this.ingresarFecha(pDesde,"desde");

        Panel pHasta = new Panel(panelFechas);
        pHasta.setLayout(new HorizontalLayout());
        new Label(pHasta).setText("Hasta");
        this.ingresarFecha(pHasta,"hasta");

        new Button(panelFechas)
                .setCaption("Filtrar")
                .onClick(()-> this.getModelObject().filtrarEventos());
    }

    public void configurarOutput(Panel mainPanel){
        Panel panelLista = new Panel(mainPanel);
        panelLista.setLayout(new VerticalLayout());

        new Label(panelLista).setText("Listado de eventos");

        Table<Evento> tabla = new Table<Evento>(panelLista, Evento.class);
        tabla.bindItemsToProperty("eventos");

        new Column<Evento>(tabla) //
                .setTitle("Titulo")
                .setFixedSize(150)
                .bindContentsToProperty("descripcion");

        new Column<Evento>(tabla) //
                .setTitle("Fecha")
                .setFixedSize(80)
                .bindContentsToProperty("fecha");
                //.setTransformer((LocalDateTime date) -> date.toLocalDate().toString());
        
        new Column<Evento>(tabla) //
                .setTitle("Sugirio")
                .setFixedSize(85)
                .bindContentsToProperty("sugirio").
                setTransformer((Boolean b) -> b? "Sí" : "No");
    }

    public void ingresarFecha(Panel panel, String property){

        new TextBox(panel)
                .setWidth(80)
                .bindValueToProperty(property).
                setTransformer(new DateTransformer());

    }

    public static void main(String[] args) {

        try { EventoMain.main(args); }
        catch (SchedulerException e){ }

        new EventosWindow().startApplication();
    }
}
