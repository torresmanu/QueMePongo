package que_me_pongo.webApp;

import que_me_pongo.usuario.Usuario;
import spark.Request;
import spark.Response;
import spark.Spark;

public class ChequeoPermisos {
	public void configurar() {
		Spark.before("/eventos", this::chequearLogin);
		Spark.before("/evento/*", this::chequearLogin);
		Spark.before("/guardarropas", this::chequearLogin);
		Spark.before("/guardarropas/*", this::chequearLogin);
	}
	
	private void chequearLogin(Request req, Response res) {
		Usuario user = req.session().attribute("usuario");
		if(user == null) {
			res.redirect("/login?redirect_to=" + req.uri());
			Spark.halt();
		}
	}
}
