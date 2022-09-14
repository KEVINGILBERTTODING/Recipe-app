<?php

use notif as GlobalNotif;

include 'connection.php';

class notif
{
}
$notif_id = $_POST['notif_id'];


$notification = "DELETE FROM notification where notif_id = '$notif_id'";
$execute = mysqli_query($koneksi, $notification);

if ($execute) {
    $response = new notif();
    $response->success = 1;
    $response->message = "Comment deleted";

    die(json_encode($response));
} else {
    $response = new notif();
    $response->success = 0;
    $response->message = "Failed";

    die(json_encode($response));
}
