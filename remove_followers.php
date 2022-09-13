<?php


include 'connection.php';

class followers
{
}

$followers_id = $_POST['followers_id'];
$user_id = $_POST['user_id'];

$query = "DELETE FROM followers where user_id = '$user_id' and followers_id ='$followers_id' ";
$result = mysqli_query($koneksi, $query);

if ($result) {

    $delete_following = "DELETE FROM following where user_id = '$followers_id' and following_id = '$user_id'";
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
