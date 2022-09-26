<?php
include 'connection.php';

class commentReply
{
}

$reply_id = $_POST['reply_id'];

$query = "DELETE FROM comment_reply WHERE reply_id = '$reply_id'";
$result = mysqli_query($koneksi, $query);

if ($result) {
    $notification = "DELETE FROM notification where reply_id = '$reply_id'";
    $execute = mysqli_query($koneksi, $notification);

    if ($execute) {
        $response = new commentReply();
        $response->success = 1;
        $response->message = "Successfully deleted";

        die(json_encode($response));
    } else {
        $response = new commentReply();
        $response->success = 0;
        $response->message = "Failed";

        die(json_encode($response));
    }
} else {
    $response = new commentReply();
    $response->success = 0;
    $response->message = "Failed";

    die(json_encode($response));
}
