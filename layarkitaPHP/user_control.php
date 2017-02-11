<?php
include_once 'regconnection.php';
	
	class User {
		
		private $db;
		private $connection;
		
		function __construct() {
			$this -> db = new DB_Connection();
			$this -> connection = $this->db->get_connection();
		}
		
		public function does_user_exist($username,$email,$password)
		{
			$query = "Select * from user where username = '$username' ";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)>0){
				$json['error'] = ' Username sudah terpakai. Coba lagi ';
				echo json_encode($json);
				mysqli_close($this -> connection);
			}else{
				$query = "insert into user (username, email, password) values ( '$username','$email','$password')";
				$inserted = mysqli_query($this -> connection, $query);
				if($inserted == 1 ){
					$json['success'] = 'Akun berhasil dibuat';
				}else{
					$json['error'] = ' Email sudah terpakai. Coba lagi';
				}
				echo json_encode($json);
				mysqli_close($this->connection);
			}
			
		}
		
	}
	
	
	$user = new User();
	if(isset($_POST['username'],$_POST['email'],$_POST['password'])) {
		$username = $_POST['username'];
		$email = $_POST['email'];
		$password = $_POST['password'];
		
		if(!empty($username) && !empty($email) ){
			
			$user-> does_user_exist($username,$email,$password);
			
		}else{
			echo json_encode("Form harus diisi");
		}
		
	}
?>