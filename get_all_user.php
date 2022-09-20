<?php
include 'connection.php';

class usr
{
}

$activate = $_GET['active'];

$query = "SELECT * FROM users where active = '$activate' ";
$result = mysqli_query($koneksi, $query);
if ($result) {
    $arraydata = array();
    while ($baris = mysqli_fetch_assoc($result)) {
        $arraydata[] = $baris;
    }
    echo json_encode($arraydata);
} else {
    $response = new usr();
    $response->success = 0;
    $response->message = "Error load data";
    die(json_encode($response));
}
