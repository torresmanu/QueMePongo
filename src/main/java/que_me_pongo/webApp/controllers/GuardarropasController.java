package que_me_pongo.webApp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.guardarropa.RepositorioGuardarropas;
import que_me_pongo.usuario.RepositorioUsuarios;
import que_me_pongo.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class GuardarropasController extends ControllerInterface {

    public String index(Request req, Response res) {
        Usuario user = req.session().attribute("usuario");
        Map<String, Object> mapa = new HashMap<String, Object>();
        
        Set<Guardarropa> guardarropas = user.getGuardarropas();
        mapa.put("guardarropas", guardarropas);
        mapa.put("link", req.url());

        ModelAndView modelAndView = new ModelAndView(mapa, "ListarGuardarropas.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }

    public String show(Request req, Response res){
        Usuario user = req.session().attribute("usuario");
        Map<String, Object> mapa = new HashMap<String, Object>();
        
        
        String id = req.params("id");
        Optional<Guardarropa> optGuarda = RepositorioGuardarropas.getInstance().buscarPorId(Integer.parseInt(id));

        if(!optGuarda.isPresent()){
            Spark.halt(404);
        }
        Guardarropa guarda = optGuarda.get();
        
        List<Usuario> duenios = RepositorioUsuarios.getInstance().buscarPorGuardarropa(guarda.getId());
        
        duenios.forEach(duenio -> requireAccess(user, duenio, res));
        
        req.session().attribute("guardarropa", guarda);
        mapa.put("prendas",optGuarda.get().getPrendas());
        mapa.put("ruta",req.url());

        ModelAndView modelAndView = new ModelAndView(mapa, "ListarPrendas.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }

}
