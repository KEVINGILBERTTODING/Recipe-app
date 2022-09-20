<?php

include 'connection.php';

$user_id = $_GET['user_id'];

$query = "SELECT user_id from req_verification where user_id = '$user_id' and status = 1";


$result = mysqli_query($koneksi, $query);

$array_data = array();

while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}

echo json_encode($array_data);
