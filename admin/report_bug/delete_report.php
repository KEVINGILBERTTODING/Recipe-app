<?php

include '../../connection.php';
$report_id = $_POST['report_id'];

class emp
{
}

$query = "DELETE FROM report_bug WHERE report_id = '$report_id'";
$result = mysqli_query($koneksi, $query);
if ($result) {
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
