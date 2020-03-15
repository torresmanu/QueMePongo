package que_me_pongo.webApp.controllers;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

import que_me_pongo.atuendo.Atuendo;
import que_me_pongo.evento.Evento;
import que_me_pongo.evento.RepositorioEventos;
import que_me_pongo.prenda.Categoria;
import que_me_pongo.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SugerenciasController extends ControllerInterface {
	public String show(Request req, Response res) {
		Usuario usuario = req.session().attribute("usuario");
		
		String stringId = req.params("id");
		Long id = Long.valueOf(stringId);
		Optional<Evento> talVezEvento = RepositorioEventos.getInstance().getEvento(id);
		if(!talVezEvento.isPresent()) {
			res.status(404);
			return null;
		}
		Evento evento = talVezEvento.get();
		
		requireAccess(usuario, evento.getUsuario(), res);
		
		if(evento.sugerenciaAceptada()) {
			res.redirect("/evento/" + evento.getId() + "/atuendo");
			Spark.halt();
		}
		
		Map<String, Object> mapa = new HashMap();
		ModelAndView modelAndView;
		Atuendo proximo = evento.getProximaSugerenciaPendiente();
		mapa.put("id", proximo.getId());
		mapa.put("prendas", proximo.getPrendas());
		modelAndView = new ModelAndView(mapa, "Sugerencias.hbs");
		return new HandlebarsTemplateEngine().render(modelAndView);
	}
	
	public String move(Request req, Response res) {
		String stringId = req.params("id");
		Long id = Long.valueOf(stringId);
		Optional<Evento> talVezEvento = RepositorioEventos.getInstance().getEvento(id);
		if(!talVezEvento.isPresent()) {
			res.status(404);
			return null;
		}
		Evento evento = talVezEvento.get();
		 
		boolean rechazo = req.queryParams("rechazo") != null;
		boolean acepto = req.queryParams("acepto") != null;
		
		if(rechazo) {
			evento.rechazarSugerencia();
			res.redirect(req.url());
		}
		else if(acepto) {
			evento.aceptarSugerencia();
			res.redirect("/evento/" + evento.getId() + "/atuendo");
		}
		return null;
	}

}
