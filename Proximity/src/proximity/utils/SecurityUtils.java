package proximity.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SecurityUtils {

	public static String md5Hash(String pwd) {
		String ret = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			ret = new BigInteger(1, md.digest(pwd.getBytes("utf-8")))
					.toString(16);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return ret;
	}

}
