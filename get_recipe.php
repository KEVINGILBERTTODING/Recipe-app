<?php

include 'connection.php';

class user
{
}

$recipe_id = $_GET['recipe_id'];

$query = "SELECT r.*, u.username, u.photo_profile, u.email
            FROM recipe r, users u where r.user_id = u.user_id and recipe_id ='$recipe_id'";

$result =  mysqli_query($koneksi, $query);
if ($result) {
    $arraydata = array();
    while ($baris = mysqli_fetch_assoc($result)) {
        $arraydata[] = $baris;
    }
    echo json_encode($arraydata);
} else {
}
