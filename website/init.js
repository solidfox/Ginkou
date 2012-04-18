
$(function() {
	$( "#maintabs" ).tabs({
		ajaxOptions: {
			error: function( xhr, status, index, anchor ) {
				$( anchor.hash ).html(
					"Ojoj, ajaxfel. Illa illa dschlaug@kth.se är förmodligen skyldig." );
			}
		},
//		cookie: {
			// store cookie for a 365 days, without, it would be a session cookie
//			expires: 365
//		}
	});
});