package se.ginkou;

/**
 * This class represents a bank account.
 * @author Daniel Schlaug
 * @version 0.1
 */
public class Account {
	
	private final long number; // Unique account number
	private String name; // Optional name of the account, can be null.
	
	public Account(long accountNumber, String accountName) {
		this.number = accountNumber;
		this.name = accountName;
	}

	/**
	 * Returns the account number.
	 * @return the account number.
	 */
	public long getNumber() {
		return number;
	}
	
	/**
	 * Returns the name of the account or the empty string if it has no name.
	 * @return the name of the account or the empty string if it has no name.
	 */
	public String getName() {
		return (name == null ? "" : name);
	}

	/**
	 * Returns a string containing information about the account.
	 */
	@Override
	public String toString() {
		return number + (name != null ? " " + name : "");
	}

	@Override
	public int hashCode() {
		return (int) number;
	}

	/**
	 * Returns true if obj is an account with the same account number
	 * as this one. Otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (number != other.number)
			return false;
		return true;
	}
}
