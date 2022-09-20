<?php

include '../../connection.php';


$query = "SELECT user_id, username, photo_profile, verified FROM users where role = 2 and verified =1";

$result = mysqli_query($koneksi, $query);

if ($result) {
    $array_data = array();

    while ($line = mysqli_fetch_assoc($result)) {
        $array_data[] = $line;
    }
    echo json_encode($array_data);
} else {
}
