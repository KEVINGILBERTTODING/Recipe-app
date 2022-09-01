<?php

include '../../connection.php';

class user
{
}

$recipe_id = $_POST['recipe_id'];
$status = $_POST['status'];

$query = "UPDATE recipe SET status = '$status' WHERE recipe_id = '$recipe_id'";
$result  = mysqli_query($koneksi, $query);
if ($result) {
    $response = new user();
    $response->status = 1;
    $response->message = "Successfully reported";
    die(json_encode($response));
} else {
    $response = new user();
    $response->status = 0;
    $response->message = "Error while reported";
    die(json_encode($response));
}
