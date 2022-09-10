<?php


include 'connection.php';

class followers
{
}

$following_id = $_POST['following_id'];
$user_id = $_POST['user_id'];

$query = "DELETE FROM following where user_id = '$user_id' and following_id ";
$result = mysqli_query($koneksi, $query);

if ($result) {

    $delete_following = "DELETE FROM followers where user_id = '$following_id' and followers_id = '$user_id'";
    $result = mysqli_query($koneksi, $delete_following);
    $response = new followers();
    $response->success = 1;
    $response->message = "Successfully remove followers";
    die(json_encode($response));
} else {
    $response = new followers();
    $response->success = 0;
    $response->message = "Failed remove followers";
    die(json_encode($response));
}
