<?PHP
    include_once("connection.php");

if(isset($_POST['id']) && 
   isset($_POST['mobile']) && $_POST['mobile'] == "android"){
    
    $pid = $_POST['id'];
    $query = "DELETE FROM video WHERE id=$id";
    
    $result = mysqli_query($conn, $query);

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