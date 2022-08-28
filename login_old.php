<?php
include_once "connection.php";
class usr
{
}

$username = $_POST["username"];
$password = md5($_POST["password"]);

if ((empty($username)) || (empty($password))) {
    $response = new usr();
    $response->success = 0;
    $response->message = "Column cannot be empty";
    die(json_encode($response));
}

$query = mysqli_query($koneksi, "SELECT * FROM users WHERE username='$username' AND password='$password'");

$row = mysqli_fetch_array($query);

if (!empty($row)) {
    $response = new usr();
    $response->success = 1;
    $response->message = "Welcome back " . $row['username'];
    $response->username = $row['username'];
    $response->email = $row['email'];
    $response->role = $row['role'];
    $response->active = $row['active'];
    $response->user_id = $row['user_id'];
    die(json_encode($response));
} else if (!empty($row2)) {
    $response = new usr();
    $response->success = 0;
    $response->message = "Password wrong";
    die(json_encode($response));
} else {
    $response = new usr();
    $response->success = 0;
    $response->message = "Unregistered Username";
    die(json_encode($response));
}
