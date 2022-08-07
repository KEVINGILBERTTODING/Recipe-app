<?php
include_once("connection.php");

class emp
{
}
date_default_timezone_set('Asia/Jakarta');
$comment_id = $_POST['comment_id'];
$comment  = $_POST['comment'];
$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");

$query = "UPDATE comments SET comment = '$comment', comment_date = '$upload_date', comment_time = '$upload_time' WHERE comment_id = '$comment_id'";
$result = mysqli_query($koneksi, $query);
if ($result) {
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



// $query = "UPDATE comments SET comment='$comment' WHERE comment_id='$comment_id'";
// $result = mysqli_query($koneksi, $query);
// if ($result) {
//     $response = new emp();
//     $response->success = 1;
//     $response->message = "Successfully updated";
//     die(json_encode($response));
// } else {
//     $response = new emp();
//     $response->success = 0;
//     $response->message = "Error while updating";
//     die(json_encode($response));
// }
