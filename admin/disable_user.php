<?php
include '../connection.php';

class user
{
}


$user_id = $_POST['user_id'];

$query = "UPDATE users SET active = 0 WHERE user_id = '$user_id'";

$result = mysqli_query($koneksi, $query);
if ($result) {
    $response = new user();
    $response->status = 1;
    $response->message = "Successfully deactivated";
    die(json_encode($response));
} else {
    $response = new user();
    $response->sstatus = 0;
    $response->message = "Error while deactivating";
    die(json_encode($response));
}
