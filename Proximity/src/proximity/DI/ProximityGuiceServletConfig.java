package proximity.DI;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

public class ProximityGuiceServletConfig extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		Injector injector = Guice.createInjector(Stage.DEVELOPMENT,
				new ProximityServletModule(), new ServiceModule());
		return injector;
	}
}
