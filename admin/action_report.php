<?php
include '../connection.php';
class user
{
}

$report_id = $_POST['report_id'];
$status = $_POST['status'];

$query = "UPDATE report_user SET status = '$status' WHERE report_id = '$report_id'";

$result = mysqli_query($koneksi, $query);
if ($result) {
    $response = new user();
    $response->status = 1;
    $response->message = "Successfully updated";
    die(json_encode($response));
} else {
    $response = new user();
    $response->status = 0;
    $response->message = "Error while updating";
    die(json_encode($response));
}
