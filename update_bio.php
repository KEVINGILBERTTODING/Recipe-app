<?php
include 'connection.php';
class emp
{
}

$user_id = $_POST['user_id'];
$bio = $_POST['bio'];


$query = "UPDATE users SET biography = '$bio' WHERE user_id = $user_id";

$result = mysqli_query($koneksi, $query);
if ($result) {
    $response = new emp();
    $response->success = 1;
    $response->message = "success";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "failed";
    die(json_encode($response));
}
