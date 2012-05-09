var ginkou = {};

ginkou.filter = new GINFilter();

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
	ginkou.historyTable = $('#DThistory').dataTable( {
			"bJQueryUI": true,
			"bProcessing": true,
	        "bServerSide": true,
	        "sAjaxSource": "http://127.0.0.1:38602/datatables",
	        "sServerMethod": "POST", 
			"aoColumns": [ 
				{ "mDataProp": "account", 	"sTitle": "Konto", 	"sClass": "account" },
				{ "mDataProp": "date", 		"sTitle": "Datum", 	"sClass": "date" },
				{ "mDataProp": "notice", 	"sTitle": "Notis", 	"sClass": "notice" },
				{ "mDataProp": "amount", 		"sTitle": "Summa", 	"sClass": "amount" }
			],
			"fnServerParams": function ( aoData ) {
	            aoData.push( { "accounts": ginkou.filter.accounts, "dateRange": ginkou.filter.dateRange } );
	        }
	} );
	ginkou.historyTable.fnSort( [ [1,'desc'] ] );	
} );

