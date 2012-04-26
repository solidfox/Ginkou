package se.ginkou;

import java.sql.Date;

public class Transaction {
	private int id;
	private Account account;
	private Date date;
	private String notice;
//	private String category;
	private double amount;
	public Transaction(int id, Account account, Date date, String notice, double amount) {
		this.account = account;
		this.date = date;
		this.notice = notice;
		this.amount = amount;
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public Account getAccount() {
		return account;
	}
	public Date getDate() {
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
}
