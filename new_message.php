<?php

include 'connection.php';

$user_id1 = $_GET['user_id1'];
$user_id2 = $_GET['user_id2'];


$query = "SELECT c.*, u.username as username1, u2.username as username2
FROM room_chat c
INNER JOIN users u on c.user_id1 = u.user_id
INNER JOIN users u2 on c.user_id2 = u2.user_id
WHERE c.user_id1 = '$user_id1' AND c.user_id2 = '$user_id2' OR c.user_id1 = '$user_id2' AND c.user_id2 = '$user_id1'";


$result = mysqli_query($koneksi, $query);

if (mysqli_num_rows($result) > 0) {
    $array_data = array();
    while ($line = mysqli_fetch_assoc($result)) {
        $array_data[] = $line;
    }
    echo json_encode($array_data);
} else {
    $query = "INSERT INTO room_chat (user_id1, user_id2) VALUES ('$user_id1', '$user_id2')";
    $result = mysqli_query($koneksi, $query);
    if ($result) {
        $query = "SELECT c.*, u.username as username1, u2.username as username2
        FROM room_chat c
        INNER JOIN users u on c.user_id1 = u.user_id
        INNER JOIN users u2 on c.user_id2 = u2.user_id
        WHERE c.user_id1 = '$user_id1' AND c.user_id2 = '$user_id2' OR c.user_id1 = '$user_id2' AND c.user_id2 = '$user_id1'";
        $result = mysqli_query($koneksi, $query);
        $array_data = array();
        while ($line = mysqli_fetch_assoc($result)) {
            $array_data[] = $line;
        }
        echo json_encode($array_data);
    }
}
