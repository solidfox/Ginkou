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
	historyTable = $('#DThistory').dataTable({
		"bJQueryUI": true,
		"aoColumns": [
			{ "sClass": "account" },
			{ "sClass": "date" },
			{ "sClass": "notice" },
			{ "sClass": "sum" }
		]
	});
	historyTable.fnSort( [ [1,'desc'] ] );
} );