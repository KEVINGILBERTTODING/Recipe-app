<?php

include 'connection.php';
$user_id = $_GET['user_id'];

$query = "SELECT * FROM room_chat WHERE user_id1 = '$user_id' OR user_id2 = '$user_id'";

$result = mysqli_query($koneksi, $query);

$array_data = array();

while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}
echo json_encode($array_data);
