<%@ page language="java" contentType="text/html; charset=ISO-8859-1" isELIgnored="false"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>AJAX TEST PAGE</title>
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.1.min.js"></script>
</head>
<body>
<script type="text/javascript">
	$(window.documemt).ready(function() {
		//alert('jQuery is ready.');
		$("#id_get_time").click(function() {
			//alert('Button Clicked');
			$.ajax({
				url : 'get_time',
				success : function(data) {
					$("#id_time").html(data);
				}
			});
		})
	});
</script>
<button id="id_get_time">Get Time</button><br>
<p id="id_time"></p>
</body>
</html>