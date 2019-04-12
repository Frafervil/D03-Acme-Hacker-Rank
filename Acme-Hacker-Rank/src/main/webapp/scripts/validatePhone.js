function validatePhone(message, countryCode) {
	var pattern = /^(\+[0-9]{1,3}[ ]{0,1}(\([0-9]{1,3}\)[ ]{0,1}){0,1}){0,1}[0-9]{1}[0-9 ]{3,}$/;
	if (document.getElementById("phone").value != "") {
		if (document.getElementById("phone").value.match(pattern)) {
			document.getElementById("form").submit();

		} else {
			if (confirm(message)) {
				// document.getElementById("phone").value = countryCode + " " + document.getElementById("phone").value;
				document.getElementById("form").submit();

			} else {
				return false;
			}
		}
	} else {
		document.getElementById("phone").removeAttribute('value');
		document.getElementById("form").submit();
	}
};