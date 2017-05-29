import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

	public void fillrainbowTable(List<String> passwords, MD5 md5,
			Reduktionsfunktion reduktionsfunktion) {

		// TODOdone Fehler finden (wahrscheinlich liegt es daran, dass der Wert, der in
		// die methode Integer.parseInt(endWert, 16)
		// reinkommt, keine Hexzahl ist.
		// Doch doch ist einer :) Aber zu lange / gross -> BigInteger
		fillHashTable(passwords, md5);
		System.out.println("HashTable build finished");
		for (String startWert : hashTable.keySet()) { // KeySet = passw√∂rter
			String endWert = startWert;
			for (int i = 0; i < 2000; i++) {
				try {
					endWert = reduktionsfunktion.reduktionsfunktion(md5.makeMD5Hash(endWert), i);
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			rainbowTable.put(startWert, endWert);
		}
		System.out.println("RainbowTable build finished");
	}

	/**@param reduktionsfunktion
	 * @param hashOfPassword : A HexaDecimal hash like md5
	 * @return
	 */
	public String findPassword(final Reduktionsfunktion reduktionsfunktion, final String hashOfPassword) {
		System.out.println("Began searching for password with hash "+hashOfPassword);
		String password = "No password found for hash "+hashOfPassword;
		String gefundenerEndWert = null;
		final MD5 md5 = new MD5();
		// Finde den Endwert f¸r gegebenen hash
		for(int i = 2000-1; i >= 0 && gefundenerEndWert == null; i--) {
			String tmpHash = hashOfPassword;
			String reduktion = null;
			// Die Kette neu aufbauen von i bis 2000
			for(int j = i; j < 2000; j++) {
				reduktion = reduktionsfunktion.reduktionsfunktion(tmpHash, j);
				tmpHash = md5.makeMD5Hash(reduktion);
			}
			if(reduktion != null && rainbowTable.values().contains(reduktion))
				gefundenerEndWert = reduktion;
		}
		
		System.out.println("Found end value: "+gefundenerEndWert);
		
		// TODO Fehler: Die Liste mit mˆglichen start und end werten ist empty
		final String finalizedEndWert = gefundenerEndWert;
		List<Entry<String, String>> list = rainbowTable.entrySet().stream()
				.filter(e -> e.getValue().equals(finalizedEndWert))
				.collect(Collectors.toList());
		
		System.out.println("Start-End-value pairs have been found ? " + (!list.isEmpty()));
		
		if(!list.isEmpty()) {
			// Finde den startWert zum gefundenen Endwert
			String startWert = list.get(0).getKey();
			
			// Baue die Kette neu auf
			List<String> kette = new ArrayList<>(200);
			kette.add(startWert);
			for(int i = 0; i < 2000; i++) {
				kette.add(md5.makeMD5Hash(kette.get(kette.size()-1)));
				kette.add(reduktionsfunktion.reduktionsfunktion(kette.get(kette.size()-1), i));
			}
			
			System.out.println("Rebuild the chain");
			
			// Suche nach dem passenden Eintrag
			int lastIndexOf = kette.lastIndexOf(hashOfPassword);
			// Wenn gefunden w‰hle den Wert vor diesem Index
			if(lastIndexOf > -1) {
				password = kette.get(lastIndexOf-1);
				System.out.println("Passord "+password+" found");
			}
		}
		
		return password;
	}

	public static void main(String[] args) {
		String hashValueGiven = "1d56a37fb6b08aa709fe90e12ca59e12";

		Rainbow_Table rt = new Rainbow_Table();
		Reduktionsfunktion r = new Reduktionsfunktion();
		rt.fillrainbowTable(
				new PasswordGenerator(2000, 7).getGeneratedPasswords(),
				new MD5(), // md5,
				r);
		
		System.out.println(rt.findPassword(r, "1d56a37fb6b08aa709fe90e12ca59e12"));
	}

}
