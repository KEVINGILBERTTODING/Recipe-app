<?php
include_once("connection.php");
error_reporting(0);
$status = $_GET['status'];
$category = $_GET['category'];

if (!empty($category)) {

    $query = "SELECT * FROM recipe WHERE status = '$status' AND category = '$category'";
} else {
    $query = "SELECT * FROM recipe where status = '$status'";
    // echo "<script>alert('Please select a category');</script>";
}
// $query = "SELECT * FROM recipe WHERE status = '$status'";
$result = mysqli_query($koneksi, $query);
$arraydata = array();

while ($baris = mysqli_fetch_assoc($result)) {
    $arraydata[] = $baris;
}
echo json_encode($arraydata);
