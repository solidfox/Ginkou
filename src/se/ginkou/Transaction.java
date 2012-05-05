package se.ginkou;

import org.joda.time.DateTime;


public class Transaction {
	
	private int id;
	private Account account;
	private DateTime date;
	private String notice;
//	private String category;
	private double amount;
	public Transaction(int id, Account account, DateTime date, String notice, double amount) {
		this.account = account;
		this.date = date;
		this.notice = notice;
		this.amount = amount;
		this.id = id;
	}
	public Transaction(Account account, DateTime date, String notice, double amount) {
		this.account = account;
		this.date = date;
		this.notice = notice;
		this.amount = amount;
		this.id = this.hashCode();
	}
	public int getId() {
		return id;
	}
	public Account getAccount() {
		return account;
	}
	public DateTime getDate() {
		return date;
	}
	public String getNotice() {
		return notice;
	}
//	public String getCategory() {
//		return category;
//	}
	public double getAmount() {
		return amount;
	}
//	public long getFlags() {
//		// TODO determine flags
//	}
	
	/**
	 * Returns a json representation of the transaction.
	 */
	public String toJSON() {
		return "{" +
					"\"id\"=\"" + id + "\", " +
					"\"account\"=\"" + account.getNumber() + "\", " +
					"\"date\"=\"" + date.toString("yyyy-MM-dd") + "\", " +
					"\"notice\"=\"" + notice + "\", " +
					"\"amount\"=\"" + amount + "\"" +
				"}";
	}
	
	
	@Override
	public String toString() {
		return this.toJSON();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + ((notice == null) ? 0 : notice.hashCode());
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
		Transaction other = (Transaction) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (Double.doubleToLongBits(amount) != Double
				.doubleToLongBits(other.amount))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (notice == null) {
			if (other.notice != null)
				return false;
		} else if (!notice.equals(other.notice))
			return false;
		return true;
	}
}
