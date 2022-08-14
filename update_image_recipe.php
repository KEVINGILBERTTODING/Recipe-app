<?php
include 'connection.php';

class emp
{
}


date_default_timezone_set('Asia/Jakarta');
$user_id = $_POST['user_id'];
$title = $_POST['title'];
$image = $_POST['image'];
$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");
$recipe_id = $_POST['recipe_id'];

$nama_file = $user_id . "-" . $title . "-" . $upload_date . ".png";

$path = "recipe_images/" . $nama_file;

$actualpath = "http://192.168.11.92/android/recipe_images/$path";


$query = "UPDATE recipe SET image = '$nama_file' WHERE recipe_id = '$recipe_id'";
$result = mysqli_query($koneksi, $query);


if ($result) {
    file_put_contents($path, base64_decode($image));
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
