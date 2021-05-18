package proximity.persistenceServices.file;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Random;

import javax.servlet.ServletContext;

import com.google.inject.Inject;

import proximity.persistenceServices.ImagePersistenceService;

public class FileImagePersistenceService implements ImagePersistenceService {

	private final String imageDirectory;
	private final Random random;
	private static final String extension = ".pimg";

	private String fullFileName(String image) {
		return imageDirectory + image + extension;
	}

	@Inject
	public FileImagePersistenceService(ServletContext svc) {
		String f = svc.getInitParameter("imageFolder");
		if (!f.endsWith("\\") && !f.endsWith("/"))
			imageDirectory = f + "/";
		else
			imageDirectory = f;

		random = new Random();
	}

	public String saveImage(InputStream in) {
		if(in == null){
			return "noimage";
		}
		
		try {
			String name = generateName();
			Files.copy(in, new File(fullFileName(name)).toPath());
			doResizings(name);
			return name;

		} catch (Exception ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	private void doResizings(String name) {
		// TODO in the future a different thread should resize the images to
		// predefined sizes
	}

	private String generateName() {
		String name;
		byte[] bytes = new byte[25];
		do {
			random.nextBytes(bytes);
			name = new BigInteger(1, bytes).toString(16);
		} while (new File(fullFileName(name)).exists());
		return name;
	}

	@Override
	public File getImage(String image) {
		return new File(fullFileName(image));
	}

}
