<?php
include_once("connection.php");

class emp
{
}
date_default_timezone_set('Asia/Jakarta');
$comment_id = $_POST['comment_id'];
$comment  = $_POST['comment'];
$recipe_id = $_POST['recipe_id'];
$user_id = $_POST['user_id'];
$user_id_notif = $_POST['user_id_notif'];
$date = $_POST['date'];
$time = $_POST['time'];


$query = "UPDATE comments SET comment = '$comment', edited = 1 WHERE comment_id = '$comment_id'";
$result = mysqli_query($koneksi, $query);
if ($result) {

    // Update notification
    $notification = "UPDATE notification SET comment = '$comment'
    where user_id = '$user_id' and user_id_notif = '$user_id_notif'
                    and recipe_id = '$recipe_id' and date = '$date' and time = '$time' ";

    $execute2 =  mysqli_query($koneksi, $notification);
    $response = new emp();
    $response->success = 1;
    $response->message = "Successfully uploaded";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while uploading";
    die(json_encode($response));
}
