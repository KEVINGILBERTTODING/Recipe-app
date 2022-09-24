<?php
include_once("connection.php");

class emp
{
}

$comment_id = $_POST['comment_id'];

$recipe_id = $_POST['recipe_id'];
$user_id = $_POST['user_id'];
$user_id_notif = $_POST['user_id_notif'];
$date = $_POST['date'];
$time = $_POST['time'];




$query = "DELETE FROM comments WHERE comment_id='$comment_id'";

$result = mysqli_query($koneksi, $query);
if ($result) {

    // Delete all comment reply when main comment is deleted
    $comment_reply = "DELETE FROM comment_reply WHERE comment_id = '$comment_id'";
    $execute2 = mysqli_query($koneksi, $comment_reply);


    // Delete all total like comment were main comment was deleted
    $like_comment = "DELETE FROM like_comment WHERE comment_id = '$comment_id' ";
    $execute3 = mysqli_query($koneksi, $like_comment);


    // Delete notification
    $notification = "DELETE FROM notification where comment_id = '$comment_id'";
    $execute = mysqli_query($koneksi, $notification);

    $response = new emp();
    $response->success = 1;
    $response->message = "Successfully deleted";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while deleting";
    die(json_encode($response));
}
