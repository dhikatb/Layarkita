<?PHP
    include_once("connection.php");

if(isset($_POST['txtName']) && isset($_POST['txtDeskripsi']) && 
   isset($_POST['txtYoutubeUrl']) && isset($_POST['txtYoutubeCode']) && isset($_POST['txtPublish'])){
    $name = $_POST['txtName'];
    $deskripsi = $_POST['txtDeskripsi'];
    $youtube_url = $_POST['txtYoutubeUrl'];
    $youtube_code = $_POST['txtYoutubeCode'];
	$publish = $_POST['txtPublish'];


    if(isset($_POST['mobile']) && $_POST['mobile'] == "android" && (!empty($name) && !empty($deskripsi) && !empty($youtube_url) && !empty($youtube_code))){
		$query = "INSERT INTO video (name, deskripsi, youtube_url, youtube_code, publish) 
		VALUES ('$name', '$deskripsi', '$youtube_url', '$youtube_code', '$publish')"; 
    
	$result = mysqli_query($conn, $query);}
        if($result > 0){
            echo "success";
            exit;       
    }  else{   
            echo "failed";
            exit;
        }
}
    
?>
