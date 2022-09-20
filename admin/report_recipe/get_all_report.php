<?php

include '../../connection.php';

class user
{
}

$status = $_GET['status'];

$query = "SELECT r.*, u.username, u.photo_profile, u.email, u.verified
            FROM report_recipe r, users u where r.user_id = u.user_id and status ='$status' order by r.report_id desc";

$result =  mysqli_query($koneksi, $query);
if ($result) {
    $arraydata = array();
    while ($baris = mysqli_fetch_assoc($result)) {
        $arraydata[] = $baris;
    }
    echo json_encode($arraydata);
} else {
    $response = new user();
    $response->success = 0;
    $response->message = "Error load data";
    die(json_encode($response));
}
