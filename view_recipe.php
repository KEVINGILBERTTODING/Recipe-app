<?php
include_once("connection.php");
error_reporting(0);
$status = $_GET['status'];
$category = $_GET['category'];
$likes = $_GET['likes'];

// Api untuk get recipes by category
if (!empty($category)) {
    $query = "SELECT r.recipe_id, r.user_id, r.title, r.description, r.category, r.duration, r.ingredients, r.steps, r.servings,
    r.note, r.upload_date, r.upload_time, r.image, r.status, r.ratings, r.likes,
    u.username, u.photo_profile, u.email from recipe r, users u where r.user_id= u.user_id and r.status='$status' and category='$category' order by recipe_id";
}

//Api untuk get popular recipes
if (!empty($likes)) {
    $query = "SELECT r.recipe_id, r.user_id, r.title, r.description, r.category, r.duration, r.ingredients, r.steps, r.servings,
    r.note, r.upload_date, r.upload_time, r.image, r.status, r.ratings, r.likes,
    u.username, u.photo_profile, u.email from recipe r, users u where r.user_id= u.user_id and r.status='$status' and likes > 50 order by likes desc";
}
// Api untuk get recipes by status
if (is_null($category) && is_null($likes)) {
    $query = "SELECT r.recipe_id, r.user_id, r.title, r.description, r.category, r.duration, r.ingredients, r.steps, r.servings,
r.note, r.upload_date, r.upload_time, r.image, r.status, r.ratings, r.likes,
u.username, u.photo_profile, u.email from recipe r, users u where r.user_id= u.user_id and r.status='$status' order by recipe_id desc";
}
// $query = "SELECT * FROM recipe WHERE status = '$status'";
$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
