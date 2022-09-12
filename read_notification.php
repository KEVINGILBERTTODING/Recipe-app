<?php

include 'connection.php';

class notification
{
}

$user_id = $_POST['user_id'];

$query = "UPDATE notification SET status ='0' where status = '1' and user_id_notif = '$user_id'";

$result = mysqli_query($koneksi, $query);

if ($result) {
    $response = new notification();
    $response->code = 200;
    $response->message = "Successfully";
    $response->status = 1;
} else {
    $response = new notification();
    $response->code = 200;
    $response->message = "Failed";
    $response->status = 0;
}
