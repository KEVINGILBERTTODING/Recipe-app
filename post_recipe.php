<?php
$servername = "localhost";
$username = "root";
$dbname = "recipes";
$password = "";



try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    // set the PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    echo "Connection failed: " . $e->getMessage();
}

date_default_timezone_set('Asia/Jakarta');
$recipe_id = $_POST['recipe_id'];
$user_id = $_POST['user_id'];
$title = $_POST['title'];
$description = $_POST['description'];
$category = $_POST['category'];
$servings = $_POST['servings'];
$duration = $_POST['duration'];
$ingredients = $_POST['ingredients'];
$steps = $_POST['steps'];
$note = $_POST['note'];
$upload_date = date("Y-m-d");
$upload_time = date("H:i:s");
$status = $_POST['status'];
$ratings = $_POST['ratings'];
$likes = $_POST['likes'];


class emp
{
}


$nama_file = $recipe_id . ".png";

$path = "recipe_images/" . $nama_file;



$msql = "INSERT INTO recipe (user_id, title, description, category, servings, duration, ingredients, steps, note, upload_date, upload_time, image, status, ratings, likes, qrcode_img) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
$stat = $conn->prepare($msql);
$res = $stat->execute([$user_id, $title, $description, $category, $servings, $duration, $ingredients, $steps, $note, $upload_date, $upload_time, $nama_file, $status, $ratings, $likes, $nama_file]);

require_once('phpqrcode/qrlib.php');
$nama = $recipe_id;
$namafile = $nama . '.png';
$tempDir = 'qrcode_recipe/';
$pngAbsoluteFilePath = $tempDir . $namafile;
$content = $nama;
$urlRelativeFilePath = $pngAbsoluteFilePath;
if (!file_exists($pngAbsoluteFilePath)) {
    QRcode::png($content, $pngAbsoluteFilePath);
}

// if ($res) {
//   $data =['']
//     echo json_encode($data);
// } else {
//     echo json_encode(['error' => $stat->errorCode()]);
// }

if ($res) {
    file_put_contents($path, base64_decode($image));

    $response = new emp();
    $response->success = 1;
    $response->message = "Successfully uploaded";
    die(json_encode($response));
} else {
    $response = new emp();
    $response->success = 0;
    $response->message = "Error while uploading";
    die(json_encode($response));
}
