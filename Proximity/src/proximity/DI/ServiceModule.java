package proximity.DI;

import proximity.persistenceServices.ImagePersistenceService;
import proximity.persistenceServices.PlacePersistenceService;
import proximity.persistenceServices.PublicDataPersistenceService;
import proximity.persistenceServices.UserPersistenceService;
import proximity.persistenceServices.file.FileImagePersistenceService;
import proximity.persistenceServices.jpa.JPAPlaceService;
import proximity.persistenceServices.jpa.JPAPublicDataPersistenceService;
import proximity.persistenceServices.jpa.JPAUserService;
import proximity.webService.SessionHolder;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;

public class ServiceModule extends AbstractModule {
	@Override
	public void configure() {
		bind(UserPersistenceService.class).to(JPAUserService.class).in(
				RequestScoped.class);
		bind(PlacePersistenceService.class).to(JPAPlaceService.class).in(
				RequestScoped.class);
		bind(ImagePersistenceService.class).to(
				FileImagePersistenceService.class).in(Singleton.class); // Should
																		// be
																		// singleton
																		// because
																		// it
																		// will
																		// have
																		// a
																		// background
																		// thread
																		// to
																		// resize
		bind(PublicDataPersistenceService.class).to(
				JPAPublicDataPersistenceService.class).in(RequestScoped.class);
		bind(SessionHolder.class).in(Singleton.class);
	}
}
