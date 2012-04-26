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
			"aaData": [
				/* Reduced data set */
				{ "accountID": "567", "account": "1234-323897", "date": "2011-02-12", "notice": "Gojgoj", "sum": 500 },
				{ "account": "4321-323897", "date": "2011-02-16", "notice": "dsakjh", "sum": 324 },
				{ "account": "1234-323897", "date": "2011-02-15", "notice": "Serus",  "sum": 532 },
				{ "account": "4321-323897", "date": "2011-02-12", "notice": "Minum",  "sum": 342 }
			], 
			"aoColumns": [ 
				{ "mDataProp": "account", 	"sTitle": "Konto", 	"sClass": "account" },
				{ "mDataProp": "date", 		"sTitle": "Datum", 	"sClass": "date" },
				{ "mDataProp": "notice", 	"sTitle": "Notis", 	"sClass": "notice" },
				{ "mDataProp": "sum", 		"sTitle": "Summa", 	"sClass": "sum" }
			]		
	} );
	historyTable.fnSort( [ [1,'desc'] ] );	
} );

