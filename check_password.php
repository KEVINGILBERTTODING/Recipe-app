<?php

include 'connection.php';
class usr
{
}

// $password = $_POST['password'];
$user_id = $_POST['user_id'];
$password = md5($_POST['password']);
// $password = $POST['password'];

$query = "SELECT password, user_id FROM users WHERE user_id='$user_id'";
$result = mysqli_query($koneksi, $query);
$row = mysqli_fetch_array($result);


if ($row['password'] == $password) {
    $response = new usr();
    $response->success = 1;
    $response->message = "success";
    die(json_encode($response));
} else {
    $response = new usr();
    $response->success = 0;
    $response->message = "Error while updating";
    die(json_encode($response));
}
