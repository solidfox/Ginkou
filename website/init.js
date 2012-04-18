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
	$('#DThistory').dataTable({
		"bJQueryUI": true,
		"aoColumns": [
			{ "sClass": "account" },
			{ "sClass": "date" },
			{ "sClass": "notice" },
			{ "sClass": "sum" }
		]
	});
} );