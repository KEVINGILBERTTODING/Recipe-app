<?php
include 'connection.php';

class emp
{
}


$user_id = $_POST['user_id'];
$image = $_POST['image'];

$nama_file = $user_id  . ".png";

$path = "photo_profile/" . $nama_file;

$actualpath = "http://192.168.11.92/android/recipe_images/$path";


$query = "UPDATE users SET photo_profile = '$nama_file' WHERE user_id = '$user_id'";
$result = mysqli_query($koneksi, $query);


if ($result) {
    file_put_contents($path, base64_decode($image));
    $response = new emp();
    $response->success = 1;
    $response->message = "success";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while uploading";
    die(json_encode($response));
}
