<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- JSTL --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%-- SPRING --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.ghostgame.util.Constants"%>

<c:set var="continue_game" value="<%=Constants.STATUS_CONTINUE_GAME%>"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    
    <meta name="description" content="The Ghost Game - Code Challenge">
    <meta name="author" content="Rafa Nocete">
    
	<title><fmt:message key="message.title"/></title>
	
	<!-- Styles -->
	<link href="resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/css/jumbotron-narrow.css" rel="stylesheet">
	
	<!-- Javascript -->
	<script type="text/javascript">
	
	// Message for Computer's turn
	var computerturn = '<fmt:message key="message.computerturn"/>';
	
	/*
		Function (Ajax) that sends a letter to the controller
		and receives the response.
		
		param 'player': 
			'H' if Human
			'C' if Computer
	*/
	function processLetter(player) {
		if (player == 'H') {
			// HUMAN
			var letter = $("#letter").val();
			$("#letter").val("");
			letter = letter.toLowerCase();
			$("#word").append(letter);
			$.ajax({
				type : "POST",
				url : "processHumanLetter.htm",
				data : {
					nextLetter : letter
				} ,
				success : function(response) {
					var letter = response.letter;
					var status = response.status;
					var message = response.message;
					$('#message').html(message);
					
					// if game stops...
					if(status != ${continue_game}) {
						// Hides form components and shows 'play again' button
						$("#formComponents").css('display', 'none');
						$("#playagain").css('display', 'inline');
						$('#playagain').focus();
					} else {
						// If game continues...
						$("#message").html(computerturn);
						// To simulate Computer is thinking
						setTimeout("processLetter('C')", 750);
					}
				}
			});
		} else if (player == 'C') {
			$.ajax({
				// COMPUTER
				type : "GET",
				url : "processComputerLetter.htm",
				success : function(response) {
					var letter = response.letter;
					var status = response.status;
					var message = response.message;
				
					$("#word").append(letter);
					$('#message').html(message);
					
					// if game stops...
					if (status != ${continue_game}) {
						// Hides form components and shows 'play again' button
						$("#formComponents").css('display', 'none');
						$("#playagain").css('display', 'inline');
						$('#playagain').focus();
					}
				}
			});
		}
	}
	</script>
</head>

<body>

	<div class="container">
      
      <!-- TITLE -->
      <div class="header clearfix">
        <nav>
          <ul class="nav nav-pills pull-right">
          	<li role="presentation"><img src="resources/img/ghost.png" style="max-height: 40px; display: inline"></li>
          </ul>
        </nav>
        <h3 class="text-muted"><fmt:message key="message.title"/></h3>
      </div>

	  <!-- DESCRIPTION -->
      <div class="jumbotron">
        <h1><fmt:message key="message.subtitle"/></h1>
        <h4><fmt:message key="message.description"/></h4>
      </div>
      
      <!-- FORM CONTAINER -->
      <div class="container" style="text-align: center;">
     	<!-- MAIN FORM FOR COMPONENTS -->
     	<form id="ghostgameForm" onsubmit="processLetter('H');return false;" class="form-inline" >
		  <div class="form-group" id="formComponents">
			<label for="letter"><fmt:message key="message.enterletter"/></label>
			<input type="text" id="letter" class="form-control" maxlength="1" style="text-transform: lowercase;width: 40px;display: inline;" required pattern="[a-zA-Z]*">
	        <button type="submit" id="submitletter" class="btn btn-success"><fmt:message key="message.submitletter"/></button>
		  </div>
		</form>
		<!-- PLAY AGAIN FORM -->
		<form id="playAgainForm" method="POST" action="${contextPath}/" class="form-inline" >
       		<button type="submit" id="playagain" class="btn btn-success" style="display: none"><fmt:message key="message.playagain"/></button>
       	</form>
      </div>
      
      <!-- MESSAGES CONTAINER -->
      <div class="container">
      	<h2 id="word"><fmt:message key="message.word"/></h2>
      	<h2 id="message"></h2>
      </div>

	  <!-- FOOTER -->
      <footer class="footer">
        <p><fmt:message key="message.footer"/></p>
      </footer>

    </div> <!-- /container -->

	<!-- JS IMPORTS FOR JQUERY AND BOOTSTRAP -->
	<script src="resources/js/jquery-2.2.4.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script> 
	
	<script type="text/javascript">
	// Setting focus on letter input text once the DOM elements are loaded.
	$(document).ready(function() {
		$('#letter').focus();
		});
	</script> 
	
</body>

</html>