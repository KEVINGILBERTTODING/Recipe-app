<?php
include 'connection.php';

class emp
{
}

date_default_timezone_set('Asia/Jakarta');
$recipe_id = $_POST['recipe_id'];
$user_id = $_POST['user_id'];
$user_id_notif = $_POST['user_id_notif'];

$date = date("Y-m-d");
$time = date("H:i:s");


$query = "INSERT INTO liked (recipe_id, user_id) VALUES ($recipe_id, $user_id)";
$result = mysqli_query($koneksi, $query);
if ($result) {


    if ($user_id == $user_id_notif) {
    } else {
        $notification = "INSERT INTO notification (user_id, user_id_notif, type, recipe_id, date, time)
        VALUES ('$user_id', '$user_id_notif', 'like', '$recipe_id', '$date', '$time')";
        $execute2 = mysqli_query($koneksi, $notification);
    }



    $response = new emp();
    $response->success = 1;
    $response->message = "Recipe liked";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while saving";
    die(json_encode($response));
}
