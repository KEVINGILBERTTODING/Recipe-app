<?php

include 'connection.php';


$query = "SELECT * FROM APP";
$result = mysqli_query($koneksi, $query);
$arraydata = array();
while ($line = mysqli_fetch_assoc($result)) {
    $arraydata[] = $line;
    die(json_encode($arraydata));
}
