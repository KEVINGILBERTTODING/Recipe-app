<?php

include 'connection.php';

$recipe_id = $_GET['recipe_id'];

$query = " SELECT l.user_id, l.recipe_id, u.username, u.verified, u.photo_profile
FROM liked l, users u WHERE l.recipe_id = '$recipe_id' AND l.user_id = u.useR_id";

$execute = mysqli_query($koneksi, $query);

$array_data = array();

while ($line = mysqli_fetch_assoc($execute)) {
    $array_data[] =  $line;
}
echo json_encode($array_data);
