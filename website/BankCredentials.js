/*
 Creates a new BankCredentials object.
 First argument must be one of the following strings
 SEB
 
 Second argument must be a string with the username
 Third argument must be a string with the password
 */
function BankCredentials(bank, username, password) {
	this.bank = bank;
	this.username = username;
	this.password = password;
}