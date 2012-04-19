import java.sql.Date;

class Transaction {
	private String account;
	private Date date;
	private String notice;
	private String cathegory;
	private double amount;
	private long flags;
	private String id;
	Transaction(String account, Date date, String notice, String cathegory, double amount, long flags, String id) {
		this.account = account;
		this.date = date;
		this.notice = notice;
		this.cathegory = cathegory;
		this.amount = amount;
		this.flags = flags;
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public Date getDate() {
		return date;
	}
	public String getNotice() {
		return notice;
	}
	public String getCathegory() {
		return cathegory;
	}
	public double getAmount() {
		return amount;
	}
	public long getFlags() {
		return flags;
	}
	public String getId() {
		return id;
	}
}