<?php

include '../../connection.php';

class usr
{
}


$user_id = $_POST['user_id'];

$query = "UPDATE users SET verified = 0 where user_id = '$user_id'";

$result = mysqli_query($koneksi, $query);

if ($result) {
    $response  = new usr();
    $response->status = 1;
    $response->message = "Success";

    die(json_encode($response));
} else {
    $response  = new usr();
    $response->status = 0;
    $response->message = "Failed";

    die(json_encode($response));
}
