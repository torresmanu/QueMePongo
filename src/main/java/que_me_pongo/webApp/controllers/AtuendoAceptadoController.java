package que_me_pongo.webApp.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;

import que_me_pongo.atuendo.Atuendo;
import que_me_pongo.evento.Evento;
import que_me_pongo.evento.RepositorioEventos;
import que_me_pongo.prenda.Categoria;
import que_me_pongo.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class AtuendoAceptadoController extends ControllerInterface {
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
		
		Atuendo aceptado = evento.getAceptado();
		Map<String, Object> mapa = new HashMap();
		mapa.put("prendas", aceptado.getPrendas());
		mapa.put("categorias", Categoria.values());
		mapa.put("clasifico", evento.opinado());
		ModelAndView modelAndView = new ModelAndView(mapa, "SugerenciaAceptada.hbs");
		return new HandlebarsTemplateEngine().render(modelAndView);
	}
	
	public String edit(Request req, Response res) {
		String stringId = req.params("id");
		Long id = Long.valueOf(stringId);
		Optional<Evento> talVezEvento = RepositorioEventos.getInstance().getEvento(id);
		if(!talVezEvento.isPresent()) {
			res.status(404);
			return null;
		}
		Evento evento = talVezEvento.get();
		
		String eleccion;
		Set<Categoria> aumentos = new HashSet(),
										reducciones = new HashSet();
		for(Categoria categoria : Categoria.values()) {
			eleccion = req.queryParams(categoria.toString()); 
			if(eleccion.equals("aumentar"))
				aumentos.add(categoria);
			else if(eleccion.equals("disminuir"))
				reducciones.add(categoria);
		}
		evento.setOpiniones(aumentos, reducciones);
		
		res.redirect(req.url());
		return null;
	}
}
