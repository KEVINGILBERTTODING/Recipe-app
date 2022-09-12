<?php

include 'connection.php';
class usr
{
}

$email = $_POST['email'];
$user_id = $_POST['user_id'];
$$query = "UPDATE users SET email = '$email' WHERE user_id = '$user_id'";
$result = mysqli_query($koneksi, $query);
if ($result) {
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
