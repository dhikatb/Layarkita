<?PHP
include_once("connection.php"); 
if(isset($_POST['txtUsername']) && isset($_POST['txtPassword'])){
    
    $username = $_POST['txtUsername'];
    $password = $_POST['txtPassword'];
    $admin = "SELECT * FROM user WHERE username = '$username' 
        AND password = '$password' AND admin ='1'";
	$user = "SELECT * FROM user WHERE username = '$username' 
        AND password = '$password' AND admin ='0'";
	$resultuser = mysqli_query($conn, $user);
    $resultadmin = mysqli_query($conn, $admin);
    if (mysqli_num_rows($resultadmin) != 0){ 
 		echo "admin"; 
        exit;
	}
    else if (mysqli_num_rows($resultuser) != 0){ 
 		echo "success"; 
        exit;
	} else{
		echo "Wrong username or password!"; 
        exit;
	}
    
}
?>