$.getScript("/ginkou.js");

// Point jQuery UI to the tabs
$(function() {
	$( "#maintabs" ).tabs({
		cookie: {
			expires: 365
		}
	});
});

// Point DataTable to the history
$(document).ready(function() {
	historyTable = $('#DThistory').dataTable( {
			"bJQueryUI": true,
			"bProcessing": true,
	        "bServerSide": true,
	        "sAjaxSource": "http://localhost:38602/datatables",
	        "sServerMethod": "POST", 
			"aoColumns": [ 
				{ "mDataProp": "account", 	"sTitle": "Konto", 	"sClass": "account" },
				{ "mDataProp": "date", 		"sTitle": "Datum", 	"sClass": "date" },
				{ "mDataProp": "notice", 	"sTitle": "Notis", 	"sClass": "notice" },
				{ "mDataProp": "amount", 		"sTitle": "Summa", 	"sClass": "amount" }
			]		
	} );
	historyTable.fnSort( [ [1,'desc'] ] );	
} );

