var gin = {};

gin.filter = new GINFilter();
gin.server = new GINServer("127.0.0.1");


// Point jQuery UI to the tabs
$(function() {
	$( "#maintabs" ).tabs({
		cookie: {
			expires: 365
		}
	});
});

$(document).ready(function() {

	gin.moduleSheet = document.getElementById("moduleSheet");

	gin.server.get("loginmodules",
		function (loginModules) {
			for (aModule in loginModules) {
				var loginModule = new GINLoginModule(loginModules[aModule]);
				gin.test = loginModule;
				gin.moduleSheet.appendChild(loginModule.element);
			}
		}
	);
	
	// Point DataTable to the history
	gin.historyTable = $('#DThistory').dataTable( {
			"bJQueryUI": true,
			"bProcessing": true,
	        "bServerSide": true,
	        "sAjaxSource": "http://127.0.0.1:38602/datatables",
	        "sServerMethod": "POST", 
			"aoColumns": [ 
				{ "mDataProp": "account", 	"sTitle": "Konto", 	"sClass": "account" },
				{ "mDataProp": "date", 		"sTitle": "Datum", 	"sClass": "date" },
				{ "mDataProp": "notice", 	"sTitle": "Notis", 	"sClass": "notice" },
				{ "mDataProp": "amount", 	"sTitle": "Summa", 	"sClass": "amount" }
			],
			"fnServerParams": function ( aoData ) {
	            aoData.push( { "accounts": gin.filter.accounts, "dateRange": gin.filter.dateRange } );
	        }
	} );
	gin.historyTable.fnSort( [ [1,'desc'] ] );	
} );