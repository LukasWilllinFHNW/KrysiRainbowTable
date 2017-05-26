import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Rainbow_Table {

	private Map<String, String> hashTable = new HashMap<>();
	private Map<String, String> rainbowTable = new HashMap<>();

	public Map<String, String> fillHashTable(List<String> passwords, MD5 md5) {
		for (String password : passwords) {
			hashTable.put(password, md5.makeMD5Hash(password));
		}
		return new HashMap<>(hashTable);
	}

	public Map<String, String> fillrainbowTable(List<String> passwords, MD5 md5,
			Reduktionsfunktion reduktionsfunktion) {

		// TODO Fehler finden (wahrscheinlich liegt es daran, dass der Wert, der in
		// die methode Integer.parseInt(endWert, 16)
		// reinkommt, keine Hexzahl ist.
		// Doch doch ist einer :) Aber zu lange / gross
		fillHashTable(passwords, md5);
		for (String startWert : hashTable.keySet()) {
			String endWert = "";
			endWert = md5.makeMD5Hash(startWert);
			for (int i = 0; i < 7; i++) {
				//System.out.println(new BigInteger(endWert, 16));
				//System.out.println("7fffffff" );//endWert.toCharArray());
				//System.out.println(Integer.parseInt("7fffffff", 16));//Integer.parseInt(endWert, 16));
				endWert = reduktionsfunktion.reduktionsfunktion(new BigInteger(endWert, 16), i);
			}
			rainbowTable.put(startWert, endWert);
		}
		return new HashMap<>(rainbowTable);
	}

	public String findPassword(Reduktionsfunktion reduktionsfunktion, String pwd) {
		// TODO der letzte Schritt
		return null;
	}

	public static void main(String[] args) {
		// MD5 md5 = new MD5();
		String hashValueGiven = "1d56a37fb6b08aa709fe90e12ca59e12";
		// String s = md5.makeMD5Hash(hashValueGiven);
		// System.out.println(s);

		Rainbow_Table rt = new Rainbow_Table();
		Map<String, String> rainbowTable = rt.fillrainbowTable(
				new PasswordGenerator(2000, 7).getGeneratedPasswords(),
				new MD5(), // md5,
				new Reduktionsfunktion());
				
		for (String val : rainbowTable.values()) {
			System.out.println(val);
		}
	}

}
