package que_me_pongo.webApp;
import org.quartz.SchedulerException;

import que_me_pongo.EventoMain;
import spark.Spark;
import spark.debug.DebugScreen;

public class WebAppMain {

	public static void main(String[] args) throws SchedulerException  {
		Spark.port(Router.getHerokuAssignedPort());
		DebugScreen.enableDebugScreen();
		Router.configurar();
		new ChequeoPermisos().configurar();
		new DB().configurarTransacciones();
		EventoMain.main(args);
	}
}
