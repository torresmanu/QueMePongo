package que_me_pongo.webApp.controllers;

import que_me_pongo.usuario.Usuario;
import spark.Response;
import spark.Spark;

public abstract class ControllerInterface {
	/*
	 * default boolean requireLogin(Usuario user, String url, Response res) {
	 * if(user == null) { res.redirect("/login?redirect_to=" + url); return false; }
	 * return true; }
	 */
	
	
	  protected void requireAccess(Usuario user, Usuario expectedUser, Response res) { 
	  	if(user.getId() != expectedUser.getId()) { 
	  		Spark.halt(403); 
	  	} 
	  }
	 
}
