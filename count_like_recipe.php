<?php

include 'connection.php';

class emp
{
}

$recipe_id = $_POST['recipe_id'];
$code = $_POST['code'];


if ($code == 1) {
    $query = "UPDATE recipe SET likes = likes + 1 WHERE recipe_id = $recipe_id";
} else {
    $query = "UPDATE recipe SET likes = likes - 1 WHERE recipe_id = $recipe_id";
}

$result = mysqli_query($koneksi, $query);
if ($result) {
    $response = new emp();
    $response->success = 1;
    $response->message = "Recipe liked";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while saving";
    die(json_encode($response));
}
