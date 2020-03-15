package que_me_pongo.webApp.controllers;

import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MenuController {
    public String show(Request req, Response res) {
        Usuario user = req.session().attribute("usuario");
        Map<String, Object> mapa = new HashMap<String, Object>();

        if(user==null) {
            res.redirect("/login");
        }

        ModelAndView modelAndView = new ModelAndView(mapa, "menu.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
}
