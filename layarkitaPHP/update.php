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
		$query = "UPDATE video
        SET name='$name', deskripsi='$deskripsi', youtube_url='$youtube_url', youtube_code='$youtube_code', publish='$publish'
		WHERE pid=$pid";
    
		$result = mysqli_query($conn, $query);	}
		if($result > 0){
        echo "success";
        exit;
    }
    else{
        echo "failed";
        exit;
    }
}    
?>