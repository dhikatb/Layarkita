<?php
define('HOST','localhost');
define('USER','root');
define('PASS','');
define('DB','layarkita');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
 
$sql = "select * from komentar ORDER BY id DESC";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,
array('cid'=>$row[1],
'commentname'=>$row[2],
'comment'=>$row[3]
));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
 
?>