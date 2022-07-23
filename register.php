<?php
include_once "connection.php";
class usr{}

$email = $_POST["email"];
$username = $_POST["username"];
$password = md5($_POST["password"]);
$password2 = $_POST["password"];
$date = $_POST["date"];
$time = $_POST["time"];


$query = mysqli_query($koneksi, "INSERT INTO users(`email`, `username`, `password`, `date`, `time`) values('$email', '$username', '$password', '$date', '$time')");

if ($query){
 $response = new usr();
 $response->success = 1;
 $response->message = "Welcome".$username;
 $response->username = $username;
 die(json_encode($response));

} else {
 $response = new usr();
 $response->success = 0;
 $response->message = "Gagal Mendaftar";
 die(json_encode($response));
}

mysqli_close($con);
?> 