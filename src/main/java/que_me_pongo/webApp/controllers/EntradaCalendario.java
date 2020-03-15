package que_me_pongo.webApp.controllers;

import que_me_pongo.evento.Evento;

public class EntradaCalendario {
	public String title;
	public String start;
	public String url;
	
	public EntradaCalendario(Evento evento) {
		this.title = evento.getDescripcion();
		this.start = evento.getFecha().toString().substring(0, 16);
		this.url = "/evento/" + evento.getId();
	}

}
