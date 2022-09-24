<?php


include 'connection.php';

$comment_id = $_GET['comment_id'];

$query = "SELECT c.*,  u.username, u.photo_profile, u.email, u.verified from comment_reply c, users u where c.user_id= u.user_id
and comment_id = '$comment_id' order by date  desc";

$result = mysqli_query($koneksi, $query);


$array_data = array();

while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}

echo json_encode($array_data);
