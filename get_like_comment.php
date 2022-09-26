<?php



include 'connection.php';

$comment_id = $_GET['comment_id'];

$query = "SELECT l.*, u.username, u.verified, u.photo_profile
FROM like_comment l, users u WHERE comment_id = '$comment_id' AND l.user_id = u.user_id";

$result = mysqli_query($koneksi, $query);

$array_data = array();

while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}
echo json_encode($array_data);
