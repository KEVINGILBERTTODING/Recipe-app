<?php

include '../../connection.php';

class usr
{
}

date_default_timezone_set('Asia/Jakarta');

$user_id = $_POST['user_id'];
$query = "UPDATE users SET verified = 1 where user_id = '$user_id'";
$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");

$result = mysqli_query($koneksi, $query);

if ($result) {

    $notification = "INSERT INTO notification
    (user_id, user_id_notif, type, date, time) VALUES 
    (6, '$user_id', 'verified', '$upload_date', '$upload_time')";

    $execute2 = mysqli_query($koneksi, $notification);

    $req_verif = "UPDATE req_verification SET status = 2 where user_id = '$user_id'";
    $execute = mysqli_query($koneksi, $req_verif);


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
