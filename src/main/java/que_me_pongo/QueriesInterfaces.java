package que_me_pongo;

import javax.persistence.NoResultException;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import com.google.common.base.Optional;

public interface QueriesInterfaces extends WithGlobalEntityManager {
	default <T, V> Optional<T> buscarUno(String query, String parameter, V value) {
		try {
			Object object = entityManager().createQuery(query)
		 			.setParameter(parameter, value)
		 			.getSingleResult();
			return Optional.of((T)object);
		}
		catch(NoResultException e) {
			return Optional.absent();
		}
	}
}
