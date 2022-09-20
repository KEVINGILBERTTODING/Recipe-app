<?php


include '../../connection.php';

class user
{
}
$id = $_POST['id'];

$query = "DELETE FROM req_verification where id = '$id'";

$result = mysqli_query($koneksi, $query);

if ($result) {
    $response = new user();
    $response->status = 1;
    $response->message = "Success deleted";

    die(json_encode($response));
} else {
    $response = new user();
    $response->status = 0;
    $response->message = "Failed";

    die(json_encode($response));
}
