package que_me_pongo.webApp.controllers;

import que_me_pongo.ExampleDataCreator;
import spark.Request;
import spark.Response;

public class ExampleDataController {
	private ExampleDataCreator dataCreator;
	
	public ExampleDataController() {
		dataCreator = new ExampleDataCreator();
	}
	
	public String crear(Request req, Response res) {
		dataCreator.createData();
		return "";
	}

}
