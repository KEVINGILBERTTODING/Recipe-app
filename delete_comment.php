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



    // Delete notification
    $notification = "DELETE FROM notification where user_id = '$user_id' and user_id_notif = '$user_id_notif'
                    and recipe_id = '$recipe_id' and date = '$date' and time = '$time'";

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
