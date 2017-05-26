import java.math.BigInteger;

public class Reduktionsfunktion {

	private final char[] Z = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	// Für Testzwecke mit L=0 H = 12345 => R(12345,0)=dea
	// private final char[] Z = { 'a','b','c','d','e' };

	public String reduktionsfunktion(BigInteger hash, int stufe) {
		String result = "";
		BigInteger hash_tmp = hash.add(new BigInteger(String.valueOf(stufe), 16)); //hash + stufe
		for (int i = 0; i < 7; i++) {
			BigInteger r = hash_tmp.mod(new BigInteger(String.valueOf(Z.length))); //hash_tmp % Z.length;
			hash_tmp = hash_tmp.divide(new BigInteger(String.valueOf(Z.length))); //hash_tmp / Z.length;
			result = Z[r.intValue()] + result;
		}

		return result;
	}

}
