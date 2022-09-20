<?php

include_once("connection.php");

$recipe_id = $_GET['recipe_id'];


$query = "SELECT c.comment_id, c.recipe_id, c.user_id, c.comment, c.comment_date, c.comment_time, c.edited,
    u.username, u.photo_profile, u.email, u.verified from comments c, users u where c.user_id= u.user_id and c.recipe_id='$recipe_id' order by comment_id desc";

$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
