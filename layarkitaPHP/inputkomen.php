<?PHP
    include_once("connection.php");

if(isset($_POST['cid']) && isset($_POST['txtKomenName']) && isset($_POST['txtKomenIsi'])){
    $cid = $_POST['cid'];
    $commentname = $_POST['txtKomenName'];
    $comment = $_POST['txtKomenIsi'];

    

    if(isset($_POST['mobile']) && $_POST['mobile'] == "android" && (!empty($commentname) && !empty($comment))){
		$query = "INSERT INTO komentar(cid,commentname,comment) 
		VALUES ($cid, '$commentname', '$comment')"; 
    
		$result = mysqli_query($conn, $query);
        if($result > 0){
            echo "success";
            exit;
        }
        echo "Insert Successfully";   
    }
    else{
        if(isset($_POST['mobile']) && $_POST['mobile'] == "android"){
            echo "failed";
            exit;
        }
        echo "Something Error";   
    }
}
    
?>