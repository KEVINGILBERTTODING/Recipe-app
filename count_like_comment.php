<?php



include 'connection.php';

$comment_id = $_GET['comment_id'];

$query = "SELECT likes FROM comments WHERE comment_id = '$comment_id'";
$result = mysqli_query($koneksi, $query);

$array_data = array();

while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}
echo json_encode($array_data);
