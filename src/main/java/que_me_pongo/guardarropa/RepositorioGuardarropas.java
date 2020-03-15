package que_me_pongo.guardarropa;

import com.google.common.base.Optional;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import que_me_pongo.QueriesInterfaces;
import que_me_pongo.usuario.RepositorioUsuarios;
import que_me_pongo.usuario.Usuario;

import java.util.Set;


public class RepositorioGuardarropas implements QueriesInterfaces {
	static private RepositorioGuardarropas instancia;
	
	static public RepositorioGuardarropas getInstance() {
		if(instancia == null)
			instancia = new RepositorioGuardarropas();
		return instancia;
	}
	
	private RepositorioGuardarropas(){}
	
	public Guardarropa createGuardarropas(Guardarropa guardarropa) {
		entityManager().persist(guardarropa);
		return guardarropa;
	}

	public Set<Guardarropa> buscarPorUsuario(String nombreUsuario){

		Optional<Usuario> usuario = RepositorioUsuarios.getInstance().buscarPorNombre(nombreUsuario);

		return usuario.get().getGuardarropas();
	}

	public Optional<Guardarropa> buscarPorId(int id){
		return buscarUno("FROM Guardarropa WHERE id = :id", "id", id);
	}
}
