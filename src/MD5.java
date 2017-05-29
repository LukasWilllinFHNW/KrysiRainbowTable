import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5 {

	public String makeMD5Hash(String s) {

		String returnString = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			byte[] md5Digest = md.digest(s.getBytes("utf-8"));
			return new BigInteger(1, md5Digest).toString(16);
		} catch (Exception e) {
			System.err.println("MD5 hashing fails");
		}
		return returnString;
	}
	
	/**
	 * Main method for testing
	 * @param args
	 */
	public static void main(String[] args) {
		String test = "ooo";
		String result = "7f94dd413148ff9ac9e9e4b6ff2b6ca9";
		System.out.println("Output for "+test+" should be "+ result );
		System.out.println(new MD5().makeMD5Hash(test).equals(result));
		
		test = "dues6fg";
		result = "c0e9a2f2ae2b9300b6f7ef3e63807e84";
		System.out.println("Output for "+test+" should be "+ result );
		System.out.println(new MD5().makeMD5Hash(test).equals(result));
	}
}
