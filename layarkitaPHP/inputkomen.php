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
<html>
<head><title>Insert | KosalGeek</title></head>
    <body>
        <h1>Insert Example | <a href=”http://www.kosalgeek.com”>KosalGeek</a></h1>
        <form action="<?PHP $_PHP_SELF ?>" method="post">
            Name <input type="text" name="txtName" value=""/><br/>
            Qty <input type="text" name="txtQty" value=""/><br/>
            Price <input type="text" name="txtPrice" value=""/><br/>
            Image URL <input type="text" name="txtImageUrl" value=""/><br/>
            <input type="submit" name="btnSubmit" value="Insert"/>
        </form>
    </body>
</html>