import java.math.BigInteger;

public class Reduktionsfunktion {

	private final String s= "0123456789abcdefghijklmnopqrstuvwxyz";
	private final char[] Z = s.toCharArray();

	// Für Testzwecke mit L=3 H = 12345 => R(12345,0)=dea
	//private final char[] Z = { 'a','b','c','d','e' };

	public String reduktionsfunktion(String md5Hash, int stufe) {
		String result = "";
		BigInteger hash = new BigInteger(md5Hash, 16);
		BigInteger hash_tmp = hash.add(new BigInteger(Integer.toHexString(stufe), 16)); //hash + stufe
		for (int i = 0; i < 7; i++) { // 7 ist länge des passworts
			BigInteger r = hash_tmp.mod(new BigInteger(String.valueOf(Z.length))); //hash_tmp % Z.length;
			hash_tmp = hash_tmp.divide(new BigInteger(String.valueOf(Z.length))); //hash_tmp / Z.length;
			result = Z[r.intValue()] + result;
		}
		return result;
	}
	
	public static void main(String[] args) {
		Reduktionsfunktion reduktionsfunktion = new Reduktionsfunktion();
		String hash = reduktionsfunktion.reduktionsfunktion("12345", 0);
		System.out.println(hash);
	}

}
