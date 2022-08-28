<?php
include_once "connection.php";
class usr
{
}

$email = $_POST["email"];
$username = $_POST["username"];
$password = password_hash($_POST["password"], PASSWORD_DEFAULT);
$password2 = $_POST["password"];
$date = $_POST["date"];
$time = $_POST["time"];


$check_username = "select * from users where username = '$username'";
$result = mysqli_query($koneksi, $check_username);

$array_data = array();
if ($line = mysqli_fetch_assoc($result)) {

    $response = new usr();
    $response->success = 0;
    $response->message = "Username already exist";
    die(json_encode($response));


    # code...
} else {





    $query = mysqli_query($koneksi, "INSERT INTO users(`email`, `username`, `password`, `date`, `time`) 
    values('$email', '$username', '$password', '$date', '$time')");


    if ($query) {
        $response = new usr();
        $response->success = 1;
        $response->message = "Welcome " . $username;
        $response->username = $username;
        die(json_encode($response));
    } else {
        $response = new usr();
        $response->success = 0;
        $response->message = "Failed";
        die(json_encode($response));
    }
}
