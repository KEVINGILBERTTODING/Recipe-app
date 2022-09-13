<?php
include_once("connection.php");

class emp
{
}

$comment_id = $_POST['comment_id'];


$query = "DELETE FROM comments WHERE comment_id='$comment_id'";

$result = mysqli_query($koneksi, $query);
if ($result) {



    // $notification = "DELETE FROM notification where notif_id = '$notif_id' ";
    // $result = mysqli_query($koneksi, $notification);
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
