package que_me_pongo.prenda;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public class RepositorioPrendas implements WithGlobalEntityManager {
	static private RepositorioPrendas instance;
	
	static public RepositorioPrendas getInstance() {
		if(instance == null)
			instance = new RepositorioPrendas();
		return instance;
	}
	
	public TipoDePrenda getTipoDePrenda(Tipo tipo) {
		return entityManager().
				createQuery("FROM TipoDePrenda WHERE tipo=:tipo",TipoDePrenda.class).
				setParameter("tipo", tipo).
				getSingleResult();
	}
	
	public TipoDePrenda createTipoDePrenda(TipoDePrenda tipo) {
		entityManager().persist(tipo);
		return tipo;
	}
	
	public Prenda createPrenda(Prenda prenda) {
		entityManager().persist(prenda);
		return prenda;
	}

}
