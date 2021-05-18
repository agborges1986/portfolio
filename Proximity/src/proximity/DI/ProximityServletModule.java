package proximity.DI;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ProximityServletModule extends ServletModule {
	@Override
	protected void configureServlets() {

		// install the JPA module
		install(new JpaPersistModule("Proximity_persistence"));
		filter("/*").through(PersistFilter.class);

		// map the servlet to serve all requests
		Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.config.property.packages",
				"proximity.webService");
		serve("/*").with(GuiceContainer.class, params);
	}

}
