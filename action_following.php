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
    // $follow = "INSERT INTO notifications (user_id, following_id, type, date, time)"

    $follow = "DELETE FROM followers WHERE user_id = '$following_id' and followers_id = '$user_id'";
    $execute = mysqli_query($koneksi, $follow);
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
