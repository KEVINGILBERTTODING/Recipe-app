<?php

include '../../connection.php';

class user
{
}



$query = "SELECT v.*, u.username, u.photo_profile, u.verified, u.email, u.verified
FROM req_verification v, users u
            where v.user_id = u.user_id and v.status = 2 order by id desc";


$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
