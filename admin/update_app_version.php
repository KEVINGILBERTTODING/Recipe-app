<?php
include '../connection.php';

class usr
{
}

$app_version = $_POST['app_version'];


$query = "UPDATE app SET app_version ='$app_version' WHERE id=1";
$result = mysqli_query($koneksi, $query);
if ($result) {
    $response = new usr();
    $response->success = 1;
    $response->message = "About us successfully updated";
    die(json_encode($response));
} else {
    $response = new usr();
    $response->success = 0;
    $response->message = "About us failed to update";
    die(json_encode($response));
}
