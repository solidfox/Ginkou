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

function GINFilter() {
	this.accounts = [ "all" ];
	this.dateRange = { "startDate": (moment().subtract('months', 3)), "endDate": moment() }
}

/* 	
	Sends a command to the server
	@arg callback is called 
*/
GINServer.prototype.post = function (command, data, success, error) {
	$.ajax({
		type: "POST",
		url: this.url+"/"+command,
		data: data,
		timeout: 10000,
		success: success,
		error: error
	});
}