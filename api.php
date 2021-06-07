<?php
	include_once 'connection.php';
	
	//Response
	$response = array();
	if(isset($_GET['apicall'])) {
		switch ($_GET['apicall']) {
			//Signup
			case 'signup':
				if (areValuesExists(array('user_name', 'user_email', 'user_password', 'user_gender'))) {
					//Get Signup Values
					$username = $_POST['user_name'];
					$useremail = $_POST['user_email'];
					$userpassword = $_POST['user_password'];
					$usergender = $_POST['user_gender'];
					
					$stmt = $conn->prepare("SELECT * FROM users WHERE user_email = ?");
					$stmt->bind_param("s", $useremail);
					$stmt->execute();
					$stmt->store_result();
					
					if ($stmt->num_rows > 0) {
						$response['error'] = true;
						$response['message'] = "This Email Has Already Been Registered.";
						$stmt->close();
					} else {
						$stmt2 = $conn->prepare("INSERT INTO users (user_name, user_email, user_password, user_gender) VALUES (?, ?, ?, ?)");
						$stmt2->bind_param("ssss", $username, $useremail, $userpassword, $usergender);
						
						if ($stmt2->execute()) {
							$stmt3 = $conn->prepare("SELECT user_id, user_name, user_email, user_gender FROM users WHERE user_email = ?");
							$stmt3->bind_param("s", $useremail);
							$stmt3->execute();
							$stmt3->bind_result($user_id, $user_name, $user_email, $user_gender);
							$stmt3->fetch();
							
							$user = array('user_id'=>$user_id, 'user_name'=>$user_name, 'user_email'=>$user_email, 'user_gender'=>$user_gender);
							$stmt3->close();
							
							$response['error'] = false;
							$response['message'] = "Signup Successfull";
							$response['user'] = $user;
						}
					}
				} else {
					$response['error'] = true;
					$response['message'] = "Inavlid Parameters";
				}
				break;
				
			//Login
			case 'login':
				if (areValuesExists(array('user_email', 'user_password'))) {
					//Get Login Values
					$useremail = $_POST['user_email'];
					$userpassword = $_POST['user_password'];
					
					$stmt = $conn->prepare("SELECT * FROM users WHERE user_email = ? AND user_password = ?");
					$stmt->bind_param("ss", $useremail, $userpassword);
					$stmt->execute();
					$stmt->store_result();
					
					if ($stmt->num_rows > 0) {
						$stmt3 = $conn->prepare("SELECT user_id, user_name, user_email, user_gender FROM users WHERE user_email = ?");
						$stmt3->bind_param("s", $useremail);
						$stmt3->execute();
						$stmt3->bind_result($user_id, $user_name, $user_email, $user_gender);
						$stmt3->fetch();
						
						$user = array('user_id'=>$user_id, 'user_name'=>$user_name, 'user_email'=>$user_email, 'user_gender'=>$user_gender);
						$stmt3->close();
						
						$response['error'] = false;
						$response['message'] = "Login Successfull";
						$response['user'] = $user;
					} else {
						$response['error'] = true;
						$response['message'] = "Your Login Credentials Do Not Match";
					}
				} else {
					$response['error'] = true;
					$response['message'] = "Inavlid Parameters";
				}
				break;
			
			default:
				$response['error'] = true;
				$response['message'] = "Inavlid API Call";
		}
	} else {
		$response['error'] = true;
		$response['message'] = "Inavlid API Call";
	}
	
	echo json_encode($response);
	
	function areValuesExists($params) {
		foreach($params as $param) {
			if (!isset($_POST[$param])) {
				return false;
			}
		}
		return true;
	}
?>