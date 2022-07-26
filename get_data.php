<?php
include_once("connection.php");
class usr
{
}
$query = "SELECT r.recipe_id, r.user_id, r.title, r.description, r.category, r.duration, r.ingredients, r.steps,
r.note, r.upload_date, r.upload_time, r.image, r.status, r.ratings, r.likes,
u.username, u.photo_profile from recipe r, users u where r.user_id= u.user_id and r.status='$status'";
$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
