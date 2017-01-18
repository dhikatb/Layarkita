<?PHP
    include_once("connection.php");

if(isset($_POST['pid']) && 
   isset($_POST['mobile']) && $_POST['mobile'] == "android"){
    
    $pid = $_POST['pid'];
    $query = "DELETE FROM video WHERE pid=$pid";
    
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