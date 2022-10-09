<?php

include 'connection.php';

class chat
{
}

$room_id = $_POST['room_id'];
$user_id = $_POST['user_id'];
$message = $_POST['message'];
date_default_timezone_set('Asia/Jakarta');
$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");

$query = "INSERT INTO chat (room_id, user_id, messages, date, time)
VALUES
('$room_id', '$user_id', '$message', '$upload_date', '$upload_time')";

$result = mysqli_query($koneksi, $query);
if ($result) {
    $response = new chat();
    $response->success = 1;
    $response->message = 'Successfully post message';

    die(json_encode($response));
} else {
    $response = new chat();
    $response->success = 0;
    $response->message = 'Failed post message';

    die(json_encode($response));
}
