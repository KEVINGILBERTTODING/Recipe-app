<?php
include 'connection.php';

class notif
{
}

$user_id = $_GET['user_id'];

$query = "SELECT n.*, u.user_id, u.username, u.photo_profile
            from notification n, users u where n.user_id = u.user_id and n.user_id_notif = '$user_id' order by notif_id desc";

$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
