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


function GINServer(url, port) {
	this.url = "http://" + url + (port == undefined ? ":38602" : ":" + port);
}

/* 	
	Sends a command to the server
	@arg callback is called 
*/
GINServer.prototype.send = function (JSONData, callback) {
	success = function (data, textStatus, jqXHR) {
		callback(true, data);
	}
	error = function (data, textStatus, jqXHR) {
		callback(false, data);
	}
	$.ajax({
	  url: this.url,
	  data: JSONData,
	  timeout: 3000,
	  success: success,
	  error: error,
	  dataType: "json"
	});
}