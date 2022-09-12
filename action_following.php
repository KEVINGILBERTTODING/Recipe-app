<?php

include 'connection.php';

class usr
{
}


date_default_timezone_set('Asia/Jakarta');
$user_id = $_POST['user_id'];
$following_id = $_POST['following_id'];

$query = "DELETE FROM following WHERE user_id = '$user_id' and following_id = '$following_id'";
$result = mysqli_query($koneksi, $query);
if ($result) {


    // Delete followers
    $follow = "DELETE FROM followers WHERE user_id = '$following_id' and followers_id = '$user_id'";
    $execute = mysqli_query($koneksi, $follow);


    // Delete notification follow
    $notification = "DELETE FROM notification where user_id ='$user_id' and user_id_notif = '$following_id' and type = 'follow'";
    $excute2 = mysqli_query($koneksi, $notification);


    $response = new usr();
    $response->success = 1;
    $response->message = "Successfully unfollowed";
    die(json_encode($response));
} else {
    $response = new usr();
    $response->success = 0;
    $response->message = "Error while unfollowing";
    die(json_encode($response));
}
