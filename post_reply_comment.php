<?php

include_once("connection.php");

class emp
{
}

date_default_timezone_set('Asia/Jakarta');
$reply_id = $_POST['reply_id'];
$recipe_id = $_POST['recipe_id'];
$user_id = $_POST['user_id'];
$comment_id = $_POST['comment_id'];
$comment = $_POST['comment'];
$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");
$user_id_notif = $_POST['user_id_notif'];



$query = "INSERT INTO comment_reply (reply_id, recipe_id, comment_id, user_id, comment, comment_date, comment_time) 
VALUES ('$reply_id','$recipe_id', '$comment_id', '$user_id', '$comment', '$upload_date', '$upload_time')";
$result = mysqli_query($koneksi, $query);

if ($result) {

    if ($user_id == $user_id_notif) {
        // Tidak menyimpan notifikasi jika user sendiri yang commentar
    } else {
        $notification = "INSERT INTO notification (reply_id, recipe_id, comment_id, user_id, comment, user_id_notif, type, date, time) 
        VALUES ('$reply_id', '$recipe_id', '$comment_id', '$user_id', '$comment', '$user_id_notif', 'comment_reply', '$upload_date', '$upload_time')";
        $result = mysqli_query($koneksi, $notification);
    }




    $response = new emp();
    $response->success = 1;
    $response->message = "Successfully replied";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while replied";
    die(json_encode($response));
}
