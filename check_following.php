<?php

include 'connection.php';

class usr
{
}


$user_id = $_GET['user_id'];
$following_id = $_GET['following_id'];

$query = "SELECT * FROM following where user_id = '$user_id' and following_id = '$following_id'";

$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
