package se.ginkou;

public class Account {
	
	int id;
	String accountNumber;
	String accountName;

	public Account(int accountId) {
		id = accountId;
	}

	public Account(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getID() {
		return id;
	}

	@Override
	public String toString() {
		return accountNumber + (accountName != null ? " " + accountName : "");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
