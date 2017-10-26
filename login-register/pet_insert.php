<?php
error_reporting(E_ALL);
ini_set('display_errors',1);

$link = mysqli_connect("13.229.34.115", "root", "rha1tpakfl", "WIPI");

if($link)
{
	echo "MySQL 접속 에러 :";
	echo mysqli_connect_error();
	exit();
}

mysqli_set_charset($link, "utf8");

$sno = isset($_POST['sno']) ? $_POST['sno'] : '';
$pet_name = isset($_POST['pet_name']) ? $_POST['pet_name'] : '';
$pet_type = isset($_POST['pet_type']) ? $_POST['pet_type'] : '';
$pet_age = isset($_POST['pet_age']) ? $_POST['pet_age'] : '';

if($sno != "" and $pet_name != "" and $pet_type != "" and $pet_age != ""){

	$sql = "insert into pet values('$sno', '$pet_name', '$pet_type', '$pet_age')";
	$result = mysqli_query($link, $sql);

	if($result){
		echo "sql success";
	}else{
		echo "error";
		echo mysqli_error($link);
	}
}
else{
	echo "no data";
}

mysqli_close($link);
?>
