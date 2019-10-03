<%@page isELIgnored="false"%>
<%@taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Timeline</title>
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.1.min.js"></script>
</head>
<body>
<script type="text/javascript">
	$(window.documemt).ready(function() {
		//alert(${lastRefreshTime});
		var lastRefreshTime = ${lastRefreshTime};
		var numberOfMessage = ${numberOfMessage};
		setInterval(updateAjaxWrapper, 5000);
		function updateAjaxWrapper() {
			$.ajax({
				url : 'update',
				type: 'POST',
				data : { 'lastRefreshTime' : lastRefreshTime },
				success : function(data) {
					$("#id_update").html(data);
				}
			});
		}
		//alert(JSON.stringify(numberOfMessage, null, 4));
		$("#id_more").click(function() {
			//alert('More Button Clicked');
			$.ajax({
				url : 'more',
				type: 'POST',
				data : { 'numberOfMessage' : numberOfMessage, 'lastRefreshTime' : lastRefreshTime },
				success : function(data) {
					numberOfMessage += 3;
					var div_content = "";
					var json = JSON.parse(data)
					for (var i=0; i<json.length; ++i) {
						var tr1 = "<tr height=\"25\">";
						var td11 = "<td>" + json[i]["_username"] + "<td>";
						var td12 = "<td>" + json[i]["_ago"] + "<td><tr>";
						var tr2 = "<tr height=\"0\">";
						var td21 =  "<td colspan=\"2\">" + json[i]["_content"] + "<td><tr>";
						div_content += (tr1+td11+td12+tr2+td21);
					}
					$("#id_dynamic_div").remove();
				}
			});
		})
	});
</script>
<div style="height:700px;width:400;overflow:auto" >
	<table id="id_table" border="2" bordercolor="blue">
	<tr height="25">
		<td width="250" >Timeline</td>
		<td width ="150">
    		<button id="id_update" onclick="window.location.reload();">0 Update(s)</button>
		</td>
	</tr>
	<div id="id_dynamic_div">
		<c:forEach items="${messageList}" var="message" end="2">
	    	<tr height="25">
	    		<td>
	    			<c:out value="${message._username}"/>
	    		</td>
	    		<td>
	    			<c:out value="${message._ago}"/>
	    		</td>
	    	</tr>
	    	<tr height="0">
	    		<td colspan="2">
	    			<c:out value="${message._content}"/>
	    		</td>
	    	</tr>
		</c:forEach>
	</div>
	</table>
	<button id="id_more">More</button>
	
	
</div>
</body>
</html>