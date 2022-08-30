<?php

include 'connection.php';


class emp
{
}


date_default_timezone_set('Asia/Jakarta');
$image = $_POST['image'];
$user_id = $_POST['user_id'];
$user_id_report = $_POST['user_id_report'];
$report = $_POST['report'];
$title = $_POST['title'];


// get max id from database
$get_max_id = "SELECT MAX(report_id) FROM report_user";
$result = mysqli_query($koneksi, $get_max_id);
$row = mysqli_fetch_array($result);
$id = $row[0] + 1;


$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");

$nama_file = $user_id . "-" . $upload_date . "-" . $id . ".png";

$path = "img_report_user/" . $nama_file;

$query = "INSERT INTO report_user (user_id,user_id_report, report, date, time, image, title) 
VALUES ('$user_id', '$user_id_report', '$report', '$upload_date', '$upload_time','$nama_file', '$title')";
$result = mysqli_query($koneksi, $query);


if ($result) {
    file_put_contents($path, base64_decode($image));
    $response = new emp();
    $response->status = 1;
    $response->message = "success";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->status = 0;
    $response->message = "Error while uploading";
    die(json_encode($response));
}
