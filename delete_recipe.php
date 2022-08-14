<?php
include 'connection.php';

class emp
{
}

$recipe_id = $_POST['recipe_id'];

$query = "DELETE FROM recipe WHERE recipe_id='$recipe_id'";
$result = mysqli_query($koneksi, $query);

if ($result) {
    $response = new emp();
    $response->success = 1;
    $response->message = "Successfully deleted";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while deleting";
    die(json_encode($response));
}
