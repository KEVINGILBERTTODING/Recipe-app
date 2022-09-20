<?php

include 'connection.php';

class user
{
}

date_default_timezone_set('Asia/Jakarta');
$user_id = $_POST['user_id'];
$username = $_POST['username'];
$fullname = $_POST['fullname'];
$doc_type = $_POST['doc_type'];
$category = $_POST['category'];
$region = $_POST['region'];
$type = $_POST['type'];
$url = $_POST['url'];
$image = $_POST['image'];

$random_number = rand(100, 5000);


$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");

$file_name =  $user_id . "-" . $random_number . "-" . $upload_date . ".png";

$path = "document/" . $file_name;

$query = "INSERT INTO req_verification (user_id, username, full_name, doc_type,
        category, region, type, url, image, date, time)
        VALUES
        ('$user_id', '$username', '$fullname', '$doc_type', '$category', '$region',
        '$type', '$url', '$file_name', '$upload_date', '$upload_time')";
$result = mysqli_query($koneksi, $query);


if ($result) {
    file_put_contents($path, base64_decode($image));
    $response = new user();
    $response->status = 1;
    $response->message = "Successfully upload";

    die(json_encode($response));
} else {

    $response = new user();
    $response->status = 0;
    $response->message = "Failed";

    die(json_encode($response));
}
