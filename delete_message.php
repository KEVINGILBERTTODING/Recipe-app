<?php

include 'connection.php';

class chat
{
}


$chat_id = $_POST['chat_id'];
$code = $_POST['code'];

// delete for everyone
if ($code == 1) {

    $query = "UPDATE chat SET remove = '1'  WHERE chat_id = '$chat_id'";
    $result = mysqli_query($koneksi, $query);
}

// delete only for nw
else {
    $query = "UPDATE chat SET remove = '2'  WHERE chat_id = '$chat_id'";
    $result = mysqli_query($koneksi, $query);
}



if ($result) {
    $response = new chat();
    $response->success = 1;
    $response->message = "Successfull deleted!";

    die(json_encode($response));
} else {
    $response = new chat();
    $response->success = 0;
    $response->message = "Failed deleted message!";

    die(json_encode($response));
}
