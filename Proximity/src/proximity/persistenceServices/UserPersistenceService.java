package proximity.persistenceServices;

import proximity.persistence.model.User;

public interface UserPersistenceService {
	public User getUserByUserName(String userName) throws RuntimeException;

	public User getUserById(int userId) throws RuntimeException;

	public void addNewUser(User user) throws RuntimeException;

	public void updateUser(User user) throws RuntimeException;
}
