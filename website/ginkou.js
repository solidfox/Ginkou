function GINServer(url, port) {
	this.url = "http://" + url + (port == undefined ? ":38602" : ":" + port);
}

function GINFilter() {
	this.accounts = [ "all" ];
	this.dateRange = { "startDate": (moment().subtract('months', 3)), "endDate": moment() }
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

function GINLoginModule(jsonLoginModule) {
	this.module = jsonLoginModule["module"];
	this.bankName = jsonLoginModule["bankName"];
	this.element = document.createElement('div');
	this.form = document.createElement("form");
	
	this.element.className = "loginModule";
	this.element.innerHTML = "<header><h1>" + this.bankName + "</h1><header>";
	this.element.appendChild(this.form);
	for (var key in jsonLoginModule) {
		if (key.match(/^key_/)) {
			var field = document.createElement('input');
			field.type = "text";
			field.name = key;
			field.placeholder = jsonLoginModule[key];
			this.form.appendChild(field);
		}
	}
	var submitButton = document.createElement('input');
	submitButton.type = "submit";
	submitButton.value = "Hämta transaktioner";
	this.form.appendChild(submitButton);
}

GINLoginModule.prototype.login = function () {
	
}