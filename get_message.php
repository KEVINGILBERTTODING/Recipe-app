<?php


include 'connection.php';


$room_id = $_GET['room_id'];

$query = "SELECT * FROM chat WHERE room_id = '$room_id'";
$result = mysqli_query($koneksi, $query);

$array_data = array();

while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}
echo json_encode($array_data);
