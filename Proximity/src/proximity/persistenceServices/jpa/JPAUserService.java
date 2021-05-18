package proximity.persistenceServices.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.google.inject.servlet.RequestScoped;

import proximity.persistence.model.User;
import proximity.persistenceServices.UserPersistenceService;

@RequestScoped
public class JPAUserService implements UserPersistenceService {

	@Inject
	EntityManager entityManager;

	public JPAUserService() {
	}

	public User getUserByUserName(String userName) throws RuntimeException {

		List<User> l = (List<User>) entityManager
				.createQuery(
						"select u from User u " + "where u.userName = :name")
				.setParameter("name", userName).getResultList();

		return l.size() == 0 ? null : l.get(0);
	}

	@Transactional
	public void addNewUser(User user) throws RuntimeException {
		entityManager.persist(user);
	}

	@Transactional
	public void updateUser(User user) throws RuntimeException {

		User old = (User) entityManager
				.createQuery(
						"select u from User u " + "where u.userName = :name")
				.setParameter("name", user.getUserName()).getSingleResult();

		entityManager.persist(old);

	}

	public User getUserById(int userId) throws RuntimeException {
		return entityManager.find(User.class, userId);
	}

}
