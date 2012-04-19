interface BankImporter extends Importer {
	public void setCredentials(String userName, String password);
	public void connect();
}