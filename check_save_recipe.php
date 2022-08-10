<?php
include 'connection.php';
class emp
{
}

$user_id = $_GET['user_id'];
$recipe_id = $_GET['recipe_id'];

$query = "SELECT code FROM saved WHERE recipe_id = '$recipe_id' and user_id = '$user_id'";
$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
