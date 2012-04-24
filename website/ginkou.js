/*
 Creates a new BankCredentials object.
 First argument must be one of the following strings
 SEB
 
 Second argument must be a string with the username
 Third argument must be a string with the password
 */
function GINBankCredentials(bank, username, password) {
	this.bank = bank;
	this.username = username;
	this.password = password;
}


function GINServer(url) {
	this.url = serverurl;
}

GINServer.prototype.send = function (JSONData, path) {
	$.ajax({
	  type: "POST",
	  url: this.url + path,
	  data: JSONData
	}).done(function( msg ) {
	  alert( "Data Saved: " + msg );
	});
}