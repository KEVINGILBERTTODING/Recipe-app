<?php

include 'connection.php';

class usr
{
}


$password =  $_POST['password'];
$username = $_POST['username'];





$query = "select * from users where username = '$username'";


$result = mysqli_query($koneksi, $query);

$arraydata = array();


if ($line = mysqli_fetch_assoc($result)) {
    $arraydata[] = $line;
    $password_hash = $arraydata[0]['password'];

    if (password_verify($password, $password_hash)) {
        $response = new usr();
        $response->success = 1;
        $response->message = "Welcome back " . $arraydata[0]['username'];
        $response->username = $arraydata[0]['username'];
        $response->email = $arraydata[0]['email'];
        $response->role = $arraydata[0]['role'];
        $response->active = $arraydata[0]['active'];
        $response->user_id = $arraydata[0]['user_id'];

        die(json_encode($response));
    } else {
        $response = new usr();
        $response->success = 0;
        $response->message = "Password wrong";
        die(json_encode($response));
    }
} else {
    $response = new usr();
    $response->success = 0;
    $response->message = "Unregistered Username";
    die(json_encode($response));
}
