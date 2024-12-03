$(function() {
	var result = {};
	rome(inline_cal, { time: false }).on('data', function(value) {
		result.value = value;
		console.log(result.value);
	});
});