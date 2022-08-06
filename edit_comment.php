<?php
include_once("connection.php");

class emp
{
}

$comment_id = $_POST['comment_id'];
$comment  = $_POST['comment'];


$query = "UPDATE comments SET comment='$comment' WHERE comment_id='$comment_id'";
$result = mysqli_query($koneksi, $query);
if ($result) {
    $response = new emp();
    $response->success = 1;
    $response->message = "Successfully updated";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while updating";
    die(json_encode($response));
}
