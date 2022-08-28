<?php

include 'connection.php';

$user_id = $_GET['user_id'];


$query = "SELECT r.recipe_id, r.user_id, r.title, r.description, r.category, r.duration, r.ingredients, r.steps, r.servings,
r.note, r.upload_date, r.upload_time, r.image, r.status, r.ratings, r.likes,
u.username, u.photo_profile, u.email, s.recipe_id from recipe r, users u, saved s 
where r.recipe_id = s.recipe_id and r.user_id= u.user_id and s.user_id = '$user_id' 
and r.status='1' and u.active = 1 order by s.save_id desc";

$result = mysqli_query($koneksi, $query);
if ($result) {
    $arraydata = array();
    while ($baris = mysqli_fetch_assoc($result)) {
        $arraydata[] = $baris;
    }
    echo json_encode($arraydata);
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while uploading";
    die(json_encode($response));
    # code...
}
