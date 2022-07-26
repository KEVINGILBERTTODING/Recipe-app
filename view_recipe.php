<?php
include_once("connection.php");
error_reporting(0);
$status = $_GET['status'];
$category = $_GET['category'];
$likes = $_GET['likes'];

// Api untuk get recipes by category
if (!empty($category)) {

    $query = "SELECT * FROM recipe WHERE status = '$status' AND category = '$category' order by recipe_id";
}

//Api untuk get popular recipes
if (!empty($likes)) {
    $query = "SELECT * FROM recipe WHERE status = '$status' AND likes > 100 order by likes desc";
}
// Api untuk get recipes by status
if (is_null($category) && is_null($likes)) {
    $query = "SELECT * FROM recipe where status = '$status'";
}
// $query = "SELECT * FROM recipe WHERE status = '$status'";
$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
