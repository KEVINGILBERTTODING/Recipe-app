<?php

include 'connection.php';
class usr
{
}

$username = $_POST['username'];
$user_id = $_POST['user_id'];


$check_username = "SELECT username FROM users WHERE username='$username'";
$result = mysqli_query($koneksi, $check_username);
$arraydata = array();
if ($line = mysqli_fetch_assoc($result)) {
    $arraydata[] = $line;
    $response = new usr();
    $response->success = 0;
    $response->message = "Username already exist";
    $response->username = $arraydata[0]['username'];
    die(json_encode($response));
} else {
    $query = "UPDATE users SET username = '$username' WHERE user_id = '$user_id'";
    $result = mysqli_query($koneksi, $query);
    $response = new usr();
    $response->success = 1;
    $response->message = "Username updated";
    $response->username = $username;
    die(json_encode($response));
}
