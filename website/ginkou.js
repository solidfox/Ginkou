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
	return $.ajax({
		type: "POST",
		url: this.url+"/"+path,
		data: data,
		timeout: 20000,
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
			var fieldName = jsonLoginModule[key];
			var field = document.createElement('input');
			if (fieldName.match(/(Pas.*)|(Lös.*)|(.*kod.*)/)) {
				field.type = "password";
			} else {
				field.type = "text";
			}
			field.name = key;
			field.placeholder = jsonLoginModule[key];
			this.form.appendChild(field);
		}
	}
	var submitButton = document.createElement('input');
	submitButton.type = "submit";
	submitButton.value = "Hämta transaktioner";
	this.form.appendChild(submitButton);
	this.form.action = "/";
	this.element.id = this.module;
	this.form.loginModule = this;
	$(this.form).submit( function (event) {
			event.preventDefault();
			this.loginModule.login();
			return false;
		});
}

GINLoginModule.prototype.login = function () {
	$(this.form).fadeOut();
	$(this.element).append("<p class='loading'>Laddar...</p>");
	var formData = "module=" + encodeURIComponent(this.module) + "&" + $(this.form).serialize();
	gin.server.post("login", formData, 
			function (response) {
				var element = document.getElementById(response["module"]);
				var form = element.getElementsByTagName("form")[0];
				$(element.getElementsByClassName("loading")).remove();
				
				if (response["accessGranted"]) {
					$(element).append("Transaktioner laddade");
					gin.historyTable.fnSort( [ [1,'desc'] ] );
				} else {
					$(form).fadeIn();
					$(element).append("<p class='loading'>Fel inloggningsuppgifter.</p>")
//					$(element).effect("shake", {times:2}, 100);
				}
			}
		);
}