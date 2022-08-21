<?php

include 'connection.php';


class emp
{
}


date_default_timezone_set('Asia/Jakarta');

$user_id = $_POST['user_id'];
$report = $_POST['report'];
$recipe_id = $_POST['recipe_id'];



$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");

$query = "INSERT INTO report_recipe (user_id, report, recipe_id, date, time)    
VALUES (? , ? , ? , ? , ?)";
$stmt = $koneksi->prepare($query);
$stmt->bind_param("sssss", $user_id, $report, $recipe_id, $upload_date, $upload_time);

if ($stmt->execute()) {
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
