<?php

include 'connection.php';
class emp
{
}
$recipe_id = $_POST['recipe_id'];
$title = $_POST['title'];
$description = $_POST['description'];
$category = $_POST['category'];
$servings = $_POST['servings'];
$duration = $_POST['duration'];
$ingredients = $_POST['ingredients'];
$steps = $_POST['steps'];
$note = $_POST['note'];
$status = $_POST['status'];

$query = "UPDATE recipe SET title = '$title', description = '$description', 
category = '$category', servings = '$servings', duration = '$duration', ingredients = '$ingredients', 
steps = '$steps', note = '$note', status = '$status' WHERE recipe_id = '$recipe_id'";

$result = mysqli_query($koneksi, $query);

if ($result) {

    $response = new emp();
    $response->success = 1;
    $response->message = "Successfully updating";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while updating";
    die(json_encode($response));
}
