<?php

include 'connection.php';

$comment_id = $_GET['comment_id'];
$user_id = $_GET['user_id'];


$query = "SELECT id FROM like_comment where user_id = '$user_id' and comment_id = '$comment_id'";
$result = mysqli_query($koneksi, $query);

$array_data = array();

while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}
echo json_encode($array_data);
