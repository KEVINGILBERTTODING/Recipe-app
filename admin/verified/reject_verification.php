<?php

include '../../connection.php';

class usr
{
}


$id = $_POST['id'];
$user_id = $_POST['user_id'];
date_default_timezone_set('Asia/Jakarta');
$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");


$query = "DELETE FROM req_verification where id = '$id'";
$result = mysqli_query($koneksi, $query);

if ($result) {


    $notification3 = "INSERT INTO notification
        (user_id, user_id_notif, type, date, time) VALUES 
        (6, '$user_id', 'reject_verified', '$upload_date', '$upload_time')";
    $execute3 = mysqli_query($koneksi, $notification3);



    $query2 = "UPDATE users SET verified = 0 where user_id = '$user_id'";
    $execute = mysqli_query($koneksi, $query2);


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
