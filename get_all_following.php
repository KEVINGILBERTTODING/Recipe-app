<?php

include 'connection.php';

class emp
{
}

$user_id = $_GET['user_id'];

$query = "SELECT f.* , u.username, u.photo_profile, u.email from following f, users u where f.following_id = u.user_id and f.user_id = '$user_id' and u.active = 1 ";
$result = mysqli_query($koneksi, $query);
if ($result) {
    $arraydata = array();
    while ($baris = mysqli_fetch_assoc($result)) {
        $arraydata[] = $baris;
    }
    echo json_encode($arraydata);
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while uploading";
    die(json_encode($response));
    # code...
}
