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
	var formData = "module=" + encodeURIComponent(this.module) + "&" + $(this.form).serialize();
	gin.server.post("login", formData, 
			this.success
		);
}

GINLoginModule.prototype.success = function (jsonResponse) {
	var element = $(document.getElementById(jsonResponse["module"]));
	var form = element.getElementsByTagName("form")[0];
	alert(stringify(element));
	if (jsonResponse["accessGranted"]) {
		$(element).append("Transaktioner laddade");
	} else {
		form.fadeIn();
	}
}