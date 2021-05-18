package proximity.persistenceServices;

import java.io.File;
import java.io.InputStream;

public interface ImagePersistenceService {
	public String saveImage(InputStream f);

	public File getImage(String image);
}
