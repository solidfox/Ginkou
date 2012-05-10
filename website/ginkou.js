function GINServer(url, port) {
	this.url = "http://" + url + (port == undefined ? ":38602" : ":" + port);
}

function GINFilter() {
	this.accounts = [ "all" ];
	this.dateRange = { "startDate": (moment().subtract('months', 3)), "endDate": moment() }
}

function GINLoginModule(loginModule) {
	this.module = loginModule["module"];
	this.bankName = loginModule["bankName"];
	this.fields = [];
	for (var key in loginModule) {

	}
}

/* 	
	Sends a command to the server 
*/
GINServer.prototype.post = function (path, data, success, error) {
	$.ajax({
		type: "POST",
		url: this.url+"/"+path,
		data: data,
		timeout: 10000,
		success: success,
		error: error
	});
}


GINServer.prototype.get = function (path, success, error) {
	$.ajax({
		type: "GET",
		url: this.url + "/" + path,
		timeout: 10000,
		success: success,
		error: error
	})
}