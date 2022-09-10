<?php

include 'connection.php';

class usr
{
}


$user_id = $_GET['user_id'];
$followers_id = $_GET['followers_id'];

$query = "SELECT * FROM followers where user_id = '$user_id' and followers_id = '$followers_id'";

$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
