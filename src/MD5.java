import java.security.MessageDigest;

public class MD5 {

	public String makeMD5Hash(String s) {

		String returnString = new String();
		try {
			byte[] bytesOfMessage = s.getBytes("UTF-8");

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(bytesOfMessage);
			// returnString= new String(digest);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digest.length; i++) {
				sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (Exception e) {
			System.err.println("MD5 hashing fails");
		}
		return returnString;
	}

}
