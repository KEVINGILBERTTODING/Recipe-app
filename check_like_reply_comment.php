<?php

include 'connection.php';

$reply_id = $_GET['reply_id'];
$user_id = $_GET['user_id'];


$query = "SELECT id, user_id, reply_id FROM like_comment_reply where user_id = '$user_id' and reply_id = '$reply_id'";
$result = mysqli_query($koneksi, $query);

$array_data = array();

while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}

echo json_encode($array_data);
