<?php
include 'connection.php';

class emp
{
}


$recipe_id = $_POST['recipe_id'];
$user_id = $_POST['user_id'];


$query = "INSERT INTO saved (recipe_id, user_id) VALUES ($recipe_id, $user_id)";
$result = mysqli_query($koneksi, $query);
if ($result) {
    $response = new emp();
    $response->success = 1;
    $response->message = "Recipe saved";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while saving";
    die(json_encode($response));
}
