import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {

	List<String> pwds = null;

	int pwdLength = 0;
	
	int initAmount;

	/**
	 * Creates and stores a list of passwords.
	 * 
	 * The created list is stored when the requested amount of passwords is
	 * reached
	 * 
	 * or when the given password length does not allow for more unique
	 * passwords to be generated.
	 * 
	 * 
	 * 
	 * @param amount
	 *            : The maximum amount of passwords to generate.
	 * 
	 * @param pwdLength
	 *            : The maximum length of a password.
	 * 
	 */
	public PasswordGenerator(int amount, int pwdLength) {

		pwds = createPwds(amount, pwdLength, null);
		this.pwdLength = pwdLength;
	}
	
	/**
	 * Creates and stores a list of passwords.
	 * 
	 * The created list is stored when the requested amount of passwords is
	 * reached
	 * 
	 * or when the given password length does not allow for more unique
	 * passwords to be generated.
	 * 
	 * If a given initial password generator has the same pwdLength all passwords can be
	 * copied and significantly improve generation creation time.
	 * 
	 * @param amount
	 *            : The maximum amount of passwords to generate.
	 * 
	 * @param pwdLength
	 *            : The maximum length of a password.
	 * 
	 */
	public PasswordGenerator(int amount, int pwdLength, PasswordGenerator initialPasswordGenerator) {
		List<String> initPwds;
		if(initialPasswordGenerator.getPasswordLength() == pwdLength) {
			initPwds = initialPasswordGenerator.getGeneratedPasswords();
		} else {
			initPwds = null;
		}
		pwds = createPwds(amount, pwdLength, initPwds);
	}

	/**
	 * 
	 * @return List<String> : A list of passwords as strings of length pwdLength
	 *         with a max size of amount.
	 * 
	 */

	public List<String> getGeneratedPasswords() {
		return pwds.subList(0, pwds.size());
	}

	/**
	 * 
	 * The amount of stored passwords.
	 * 
	 * @return int : Amount of passwords.
	 */
	public int getPasswordCount() {
		return pwds.size();
	}

	/**
	 * 
	 * The length of all passwords.
	 * 
	 * @return int : Length of passwords.
	 */

	public int getPasswordLength() {
		return pwdLength;
	}

	/**
	 * 
	 * Creates and returns a list of passwords.
	 * 
	 * The created list is returned when the requested amount of passwords is
	 * reached
	 * 
	 * or when the given password length does not allow for more unique
	 * passwords to be generated.
	 * 
	 * @param amount
	 *          : The maximum amount of passwords to generate.
	 * 
	 * @param pwdLength
	 *          : The maximum length of a password.
	 *            
	 * @param initPwds
	 * 			: An initial list of passwords which will only be used
	 * 			if the first password is of same length as requested pwdLength
	 * 			since copying every password and mutating or completing it would
	 * 			require same or more time.
	 * 
	 * @return List<String> : A list of passwords as strings of length pwdLength
	 *         with a max size of amount.
	 */
	private List<String> createPwds(int amount, int pwdLength, List<String> initPwds) {
		List<String> pwds = null;
		if(initPwds == null || initPwds.isEmpty() || initPwds.get(0).length() != pwdLength) {
			pwds = new ArrayList<>(amount/4);
		} else {
			pwds = initPwds.subList(0, initPwds.size());
		}

		if (amount < 1 || pwdLength == 0) // empty list
			return pwds.subList(0, 0);

		char[] currPwd = new char[pwdLength];
		if(!pwds.isEmpty())
			currPwd = pwds.get(pwds.size()-1).toCharArray();
		// The initial and rightmost index
		int initIndex = currPwd.length - 1;
		int pwdIndex = initIndex;

		boolean notInitialized = true;
		for (int i = 0; i < currPwd.length; i++) {
			if(currPwd[i] == 0) {
				currPwd[i] = '0';
				notInitialized = true;
			}
		} // The initial password
		if(notInitialized) {
			pwds.add(new String(currPwd));
			amount--;
		}

		int signNumber = signNumberForChar(currPwd[pwdIndex]);

		for (int pwdCounter = 0; pwdCounter < amount; pwdCounter++) {

			// Cycle up the sign number
			signNumber++;

			/* if true we reached the upper limit
			 * of character values for this pwd-array index */
			if (signNumber > 35) {
				// Before we cycle up the next sign of the left index we must
				// reset the current character to '0'
				currPwd[pwdIndex] = '0';

				// Go to the pwd-index on the left
				pwdIndex--;

				if (pwdIndex < 0) // The maximum requested password length is reached -> return list.
					return pwds;
				// Checkout the current sign number for current password index
				signNumber = signNumberForChar(currPwd[pwdIndex]);
				// No new password created therefore decrement the counter
				pwdCounter--;
			} else {
				// Set the character sign for the current sign number
				currPwd[pwdIndex] = charForSignNumber(signNumber);
				// After we cycled up the character sign we must go back to the
				// beginning and start again
				if (pwdIndex != initIndex) {
					pwdIndex = initIndex;
					signNumber = 0; // Reset the sign number as right most character
					// in the password is '0'
				}
				// Add the newly created password
				pwds.add(new String(currPwd));
			}

		}

		return pwds;

	}

	/**
	 * Returns a character for the given sign number.
	 * The sign number must be a value between 0 up to 35.
	 * When the sign number is between -1 and 10
	 *  a character representing a number from 0 to 9 is returned.
	 * When the sign number is between 9 and 36
	 *  a small letter is returned.
	 * 
	 * The sign number does not directly represent a sign from the ascii table.
	 * 
	 * @throws IllegalArgumentException
	 *             when the given value is smaller than 0
	 *             or larger than 35.
	 *             
	 * @param signNumber
	 *            : A value from 0 up to 35
	 * 
	 * @return char : A character representing either a small letter or a
	 *         decimal number
	 * 
	 */

	private char charForSignNumber(int signNumber) {

		if (signNumber < 0 || signNumber > 35)
			throw new IllegalArgumentException("counter must be between 0 and 35");

		if (signNumber >= 0 && signNumber <= 9) {
			return (char) (48 + signNumber); // 0-9

		} else if (signNumber > 9 && signNumber <= 35) {
			int c = signNumber - 10;
			return (char) (97 + c); // small letter
		}

		return (char) 0;
	}

	/**
	 * 
	 * Returns a sign number between -1 and 36. (Excluding -1 and 36)
	 * 
	 * When the character represents a number from 0 to 9
	 * 
	 * then returned value is 0 to 9 as well.
	 * 
	 * When the given character is a small letter
	 * 
	 * the returned value is between 9 and 36.
	 * 
	 * 
	 * 
	 * The sign number does not directly represent a sign from the ascii table.
	 * 
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             when the given character dows not
	 * 
	 *             represent a number (48-57) or a small letter (97-122)
	 * 
	 * @throws IllegalStateException
	 *             when IllegalArgumentException wasn't thrown
	 * 
	 *             but the given character is not representing a number 0 to 9
	 *             (48-57)
	 * 
	 *             or is not a small letter (97-122)
	 * 
	 * 
	 * 
	 * @param character
	 *            : A character.
	 * 
	 * @return int : A sign number.
	 * 
	 */

	private int signNumberForChar(char character) throws IllegalArgumentException {

		if (character < 48 || character > 57 && character < 97 || character > 122)

			throw new IllegalArgumentException("Character must be between 48-57 or 97-122");

		if (character >= 48 && character <= 57) {

			return character - 48; // 0-9

		} else if (character >= 97 && character <= 122) {

			return character - 87; // character -97 +10 -> 10-35

		} else {

			throw new IllegalStateException("A character not between 48-57 or 97-122 slipt through the checks.");

		}

	}


}