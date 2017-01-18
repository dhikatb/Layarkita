<?PHP
include_once("connection.php");

if(isset($_POST['mobile']) && $_POST['mobile'] == "android" && 
   isset($_POST['txtPid']) && isset($_POST['txtName']) && 
   isset($_POST['txtDeskripsi']) && 
   isset($_POST['txtYoutubeUrl']) && isset($_POST['txtYoutubeCode'])){
    
    $pid = $_POST['txtPid'];
    $name = $_POST['txtName'];
    $deskripsi = $_POST['txtDeskripsi'];
    $youtube_url = $_POST['txtYoutubeUrl'];
    $youtube_code = $_POST['txtYoutubeCode'];
	$publish = $_POST['txtPublish'];

    if(isset($_POST['mobile']) && $_POST['mobile'] == "android" && (!empty($name) && !empty($deskripsi) && !empty($youtube_url) && !empty($youtube_code))){
		$delete = "DELETE FROM video WHERE pid=$pid";
		
		$query = "INSERT INTO video (name, deskripsi, youtube_url, youtube_code, publish) 
		VALUES ('$name', '$deskripsi', '$youtube_url', '$youtube_code', '$publish')";
    
		$results = mysqli_query($conn, $delete);
		$result = mysqli_query($conn, $query);	}
		if($results > 0 && $result > 0){
        echo "success";
        exit;
    }
    else{
        echo "failed";
        exit;
    }
}    
?>