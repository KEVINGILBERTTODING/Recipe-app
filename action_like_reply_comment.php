<?php

include 'connection.php';

class comment
{
}
date_default_timezone_set('Asia/Jakarta');

$comment_id = $_POST['comment_id'];
$reply_id = $_POST['reply_id'];
$user_id = $_POST['user_id'];
$code = $_POST['code'];
$user_id_notif = $_POST['user_id_notif'];
$recipe_id = $_POST['recipe_id'];
$comment = $_POST['comment'];


$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");

// if code == 1 than + 1 total comment like
// if code == 0 than - total comment like
if ($code == 1) {
    $query = "UPDATE comment_reply set likes = likes + 1 WHERE reply_id = '$reply_id'";
    $execute1 = mysqli_query($koneksi, $query);


    if ($execute1) {
        $query2 = "INSERT INTO like_comment_reply (reply_id, user_id) values ('$reply_id', '$user_id')";
        $execute2 = mysqli_query($koneksi, $query2);


        // IF USERID EQUALS USER_ID NOTIF THAN DONT PUT INTO NOTIFICATION
        if ($user_id == $user_id_notif) {

            // Do something here
        } else {

            // insert notification
            $notification1 = "INSERT INTO notification (user_id,user_id_notif, type, comment_id, reply_id, recipe_id, comment, date, time) 
        values 
        ('$user_id', '$user_id_notif', 'like_comment_reply', '$comment_id', '$reply_id','$recipe_id', '$comment', '$upload_date', '$upload_time')";

            $hasil  = mysqli_query($koneksi, $notification1);
        }



        // set request
        $response = new comment();
        $response->status = 1;
        $response->message = "Success";

        die(json_encode($response));
    } else {
        // set request
        $response = new comment();
        $response->status = 0;
        $response->message = "Failed";
        die(json_encode($response));
    }
} else {
    $query3 = "UPDATE comment_reply SET likes = likes - 1  WHERE comment_id = '$comment_id'";
    $execute3 = mysqli_query($koneksi, $query3);

    if ($execute3) {
        $query4 = "DELETE FROM like_comment_reply where user_id = '$user_id' and reply_id = '$reply_id'";
        $execute4 = mysqli_query($koneksi, $query4);

        // DELETE NOTIFICATION
        $notification2 = "DELETE FROM notification where reply_id = '$reply_id' and user_id = '$user_id' and type ='like_comment_reply'";
        $hasil2 = mysqli_query($koneksi, $notification2);
        // set request
        $response = new comment();
        $response->status = 1;
        $response->message = "success";

        die(json_encode($response));
    } else {
        // set request
        $response = new comment();
        $response->status = 0;
        $response->message = "Failed";

        die(json_encode($response));
    }
}
