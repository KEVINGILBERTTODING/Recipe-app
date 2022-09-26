<?php



include 'connection.php';

$reply_id = $_GET['reply_id'];

$query = "SELECT likes FROM comment_reply WHERE reply_id = '$reply_id'";
$result = mysqli_query($koneksi, $query);

$array_data = array();

while ($line = mysqli_fetch_assoc($result)) {
    $array_data[] = $line;
}
echo json_encode($array_data);
