<?php
include '../connection.php';
class usr
{
}

$status = $_GET['status'];

$query = "
select r.*,
u.username as username1, u.photo_profile as photo_profile1, u.email as email1, u.verified as verified1,

u2.username as username2, u2.photo_profile as photo_profile2, u2.email as email2, u2.verified as verified2
from report_user r
inner join users u on r.user_id = u.user_id
inner join users u2 on r.user_id_report = u2.user_id
where r.user_id = u.user_id
and status = '$status' order by r.report_id desc


";


$result = mysqli_query($koneksi, $query);
if ($result) {
    $arraydata = array();
    while ($baris = mysqli_fetch_assoc($result)) {
        $arraydata[] = $baris;
    }
    echo json_encode($arraydata);
} else {
    $response = new usr();
    $response->success = 0;
    $response->message = "Error load data";
    die(json_encode($response));
}
