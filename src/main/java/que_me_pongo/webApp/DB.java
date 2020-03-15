package que_me_pongo.webApp;

import javax.persistence.RollbackException;
import javax.transaction.TransactionalException;

import org.hibernate.TransactionException;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import spark.Request;
import spark.Response;
import spark.Spark;

public class DB implements WithGlobalEntityManager, TransactionalOps{
	public void configurarTransacciones() {
		Spark.before(this::before);
		
		
		Spark.after(this::after);
	}
	
	public void before(Request req, Response res) {
		if(req.requestMethod() != "GET")
			beginTransaction();
	}
	
	public void after(Request req, Response res) {
		if(req.requestMethod() != "GET")
		{
			try{
				commitTransaction();
			}
			catch(RollbackException e) {
			}
			finally {
				entityManager().clear();
				PerThreadEntityManagers.getEntityManager(); 
			  PerThreadEntityManagers.closeEntityManager();
			}
		}
	}
}
