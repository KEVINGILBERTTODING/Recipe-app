<?php

include 'connection.php';


class emp
{
}


date_default_timezone_set('Asia/Jakarta');
$image = $_POST['image'];
$user_id = $_POST['user_id'];
$chat = $_POST['chat'];

// get max id from database
$get_max_id = "SELECT MAX(id) FROM message";
$result = mysqli_query($koneksi, $get_max_id);
$row = mysqli_fetch_array($result);
$id = $row[0] + 1;


$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");

$nama_file = $user_id . "-" . $upload_date . "-" . $id . ".png";

$path = "img_chat/" . $nama_file;

$query = "INSERT INTO message (user_id, chat, date, time, image) 
VALUES (? , ? , ? , ? , ?)";
$stmt = $koneksi->prepare($query);
$stmt->bind_param("sssss", $user_id, $chat, $upload_date, $upload_time, $nama_file);

if ($stmt->execute()) {
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
