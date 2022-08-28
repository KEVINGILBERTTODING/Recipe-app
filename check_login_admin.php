<?php
include 'connection.php';

class usr
{
}

$username  = $_POST['username'];
$password  = md5($_POST['password']);

$query = "SELECT * FROM users WHERE username='$username' AND password='$password'";
$result = mysqli_query($koneksi, $query);
if (mysqli_num_rows($result) > 0) {
    $row = mysqli_fetch_assoc($result);
    $response = new usr();
    $response->success = 1;
    $response->message = "Welcome back " . $row['username'];
    $response->username = $row['username'];
    $response->email = $row['email'];
    $response->user_id = $row['user_id'];
    die(json_encode($response));
} else {
    $response = new usr();
    $response->success = 0;
    $response->message = "Unregistered Username";
    die(json_encode($response));
}
