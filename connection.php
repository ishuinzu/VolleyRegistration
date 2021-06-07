<?php
	$DB_SERVER = "localhost";
	$DB_USER   = "root";  
	$DB_PASS   = "";  
	$DB_NAME   = "registration";
	
	$conn = new mysqli($DB_SERVER, $DB_USER, $DB_PASS, $DB_NAME);
	if($conn->connect_error) {
		die("Connection Error" . $conn->connect_error);
	}
?>