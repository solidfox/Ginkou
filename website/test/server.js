module("Server");

test("Does it respond to ping?", 1, function() {
  var server = new GINServer("127.0.0.1");
  stop();
  var pingData = {
		  "caller": "test.html"
		};
  server.post("ping", pingData, 
		  function (data, textStatus) {
		  	ok(true, "Request succeded: " + JSON.stringify(data) + ". Status: " + textStatus);
		  	start();
		  },
		  function (data, textStatus) {
		  	ok(false, "Request failed: " + JSON.stringify(data) + ". Status: " + textStatus);
		  	start();
		  }
		);
  
});

test("Do we get loginmodules?", 1, function() {
	  var server = new GINServer("127.0.0.1");
	  stop();
	  server.get("loginmodules", 
			  function (data, textStatus) {
			  	ok(true, "Request succeded: " + JSON.stringify(data) + ". Status: " + textStatus);
			  	start();
			  },
			  function (data, textStatus) {
			  	ok(false, "Request failed: " + JSON.stringify(data) + ". Status: " + textStatus);
			  	start();
			  }
			);
	  
	});

test("Can we log in to dummymodule?", 1, function() {
	  var server = new GINServer("127.0.0.1");
	  stop();
	  var data = {
			  "module" = "dummybank.xml",
			  "key_0" = "dummyuser",
			  "key_1" = "dummypass"
	  };
	  server.post("login", data, 
			  function (data, textStatus) {
			  	ok(true, "Request succeded: " + JSON.stringify(data) + ". Status: " + textStatus);
			  	start();
			  },
			  function (data, textStatus) {
			  	ok(false, "Request failed: " + JSON.stringify(data) + ". Status: " + textStatus);
			  	start();
			  }
			);
	  
	});

test("Can do we get an error on wrong pass?", 1, function() {
	  var server = new GINServer("127.0.0.1");
	  stop();
	  var data = {
			  "module" = "dummybank.xml",
			  "key_0" = "dummyuser",
			  "key_1" = "wrongpass"
	  };
	  server.post("login", data, 
			  function (data, textStatus) {
			  	fail("Response syntax yet to be defined");
		  		//ok(true, "Request succeded: " + JSON.stringify(data) + ". Status: " + textStatus);
			  	start();
			  },
			  function (data, textStatus) {
				  fail("Response syntax yet to be defined");
				  ok(false, "Request failed: " + JSON.stringify(data) + ". Status: " + textStatus);
			  	start();
			  }
			);
	  
	});