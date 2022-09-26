<?php

include 'connection.php';

$reply_id = $_GET['reply_id'];


$query = "SELECT l.*, u.user_id, u.username, u.verified, u.photo_profile
FROM like_comment_reply l, users u WHERE reply_id = '$reply_id' AND l.user_id = u.user_id";

$result = mysqli_query($koneksi, $query);

$array_data = array();
while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}
echo json_encode($array_data);
