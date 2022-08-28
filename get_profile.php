<?php

include 'connection.php';

$user_id = $_GET['user_id'];

$query = "SELECT * FROM users WHERE user_id = '$user_id' and active = 1";
$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
