<?php

include 'connection.php';

class emp
{
}

$recipe_id = $_POST['recipe_id'];
$user_id = $_POST['user_id'];

$query = "DELETE FROM liked WHERE recipe_id = $recipe_id and user_id = $user_id";

$result = mysqli_query($koneksi, $query);
if ($result) {
    $response = new emp();
    $response->success = 1;
    $response->message = "Recipe deleted";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while deleting";
    die(json_encode($response));
}
