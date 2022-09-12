<?php

include 'connection.php';

class usr
{
}


date_default_timezone_set('Asia/Jakarta');
$user_id = $_POST['user_id'];
$following_id = $_POST['following_id'];

$date = date("Y-m-d");
$time = date("H:i:s");

$query = "INSERT INTO following (user_id, following_id, date, time) 
VALUES ('$user_id', '$following_id', '$date', '$time')";
$result = mysqli_query($koneksi, $query);
if ($result) {


    $follow = "INSERT INTO followers (user_id, followers_id, date, time)
    VALUES ('$following_id', '$user_id', '$date', '$time')";
    $execute = mysqli_query($koneksi, $follow);

    $notification = "INSERT INTO notification (user_id, user_id_notif, type, status, date, time)
                    VALUES ('$user_id', '$following_id', 'follow', '1', '$date', '$time')";
    $execute = mysqli_query($koneksi, $notification);
    $response = new usr();
    $response->success = 1;
    $response->message = "Successfully followed";
    die(json_encode($response));
} else {
    $response = new usr();
    $response->success = 0;
    $response->message = "Error while following";
    die(json_encode($response));
}
