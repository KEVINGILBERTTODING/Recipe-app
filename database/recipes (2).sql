-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 05, 2022 at 12:29 AM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `recipes`
--

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `comment_id` int(6) NOT NULL,
  `recipe_id` int(10) NOT NULL,
  `user_id` int(6) NOT NULL,
  `comment` text NOT NULL,
  `comment_date` varchar(15) NOT NULL,
  `comment_time` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `favorites`
--

CREATE TABLE `favorites` (
  `id` int(6) NOT NULL,
  `user_id` int(6) NOT NULL,
  `recipe_id` int(10) NOT NULL,
  `datetime` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `recipe`
--

CREATE TABLE `recipe` (
  `recipe_id` int(10) NOT NULL,
  `user_id` int(6) NOT NULL,
  `title` varchar(150) NOT NULL,
  `description` text NOT NULL,
  `category` varchar(80) NOT NULL,
  `servings` varchar(80) NOT NULL,
  `duration` varchar(80) NOT NULL,
  `ingredients` text NOT NULL,
  `steps` text NOT NULL,
  `note` text NOT NULL,
  `upload_date` varchar(50) NOT NULL,
  `upload_time` varchar(50) NOT NULL,
  `image` varchar(300) NOT NULL DEFAULT 'default.png',
  `status` int(1) NOT NULL,
  `ratings` varchar(80) NOT NULL,
  `likes` int(20) NOT NULL,
  `qrcode_img` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `recipe`
--

INSERT INTO `recipe` (`recipe_id`, `user_id`, `title`, `description`, `category`, `servings`, `duration`, `ingredients`, `steps`, `note`, `upload_date`, `upload_time`, `image`, `status`, `ratings`, `likes`, `qrcode_img`) VALUES
(1, 4, 'Rendang Daging Sapi Sederhana', 'Rendang sapi menjadi salah satu kuliner khas Indonesia yang paling terkenal di dunia. Bahkan dinobatkan menjadi makanan terlezat di dunia versi cnn.\r\n\r\n', 'Meat', '10 people', '3 hour 24 minutes', '1 kg daging sapi\r\n2 batang serai\r\n6 lembar daun jeruk\r\n4 lembar daun salam\r\n2 cm kayu manis\r\n1 sdm gula merah\r\n1 sdm kaldu bubuk rasa sapi\r\n1 sdt jintan bubuk\r\n1 sdt merica\r\n1 buah jeruk lemon\r\n1000 ml santan dari 1 butir kelapa\r\nsecukupnya Minyak goreng\r\nBumbu yang dihaluskan:\r\n8 siung bawang putih\r\n12 siung bawang merah\r\n4 buah kemiri\r\n1 sdt ketumbar\r\n6 buah cabe merah besar (buang bijinya)\r\n50 gram cabe keriting merah\r\n4 cm kunyit\r\n6 cm jahe', '1. Cuci bersih daging, potong sesuai selera beri perasan jeruk lemon, lalu cuci lagi.\r\n2. Haluskan bumbu halus\r\n3. Tumis bumbu halus dengan serai, daun jeruk, daun salam, kayu manis dan lengkuas sampai matang.\r\n4. Masukkan daging, aduk rata lalu masukkan santan. Masak dengan api kecil sambil sesekali diaduk.\r\n5. Kemudian masukkan jintan, garam, kaldu bubuk, merica, dan gula merah, diamkan hingga bumbu meresap dan airnya menyusut. Koreksi rasa, angkat dan hidangkan.', '', '2022-07-22', '12.30.30', '1.png', 1, '8/10', 23, ''),
(2, 4, 'Sate sapi ketumbar', 'Sate pada umumnya hanya berbalur bumbu kecap atau pun bumbu kacang. Nah, bagaimana bila ditambahkan ketumbar?\r\nDengan menggunakan ketumbar, sate sapi biasa pasti jadi lebih istimewa karena rasanya yang lebih berkarakter. Dengan tambahan Bango Kecap Manis, paduan aromatic ketumbar serta manis legit kecap akan membuat sate ini disukai siapa pun yang melahapnya.', 'Meat', '4 People', '2 hour 54 minutes', '1. 300 g daging sapi has luar, potong dadu \r\n   3 cm\r\n2. 160 ml Bango Kecap Manis\r\n3. 1 sdm ketumbar bubuk\r\n4. 2 sdm minyak\r\n5. 1 sdm air asam jawa\r\n6. Sekitar 10 tusuk sate\r\n7. 3 batang daun ketumbar, petik daunnya\r\n ', '1. Dengan menggunakan blender, haluskan bumbu sate.\r\n2. Lumuri daging sapi dengan bumbu halus, Bango Kecap Manis, ketumbar bubuk, minyak, dan air asam jawa. Diamkan selama 30 menit.\r\n3. Tusukkan daging dengan tusuk sate.\r\nBakar di atas bara api hingga matang sambil sesekali diolesi bumbu. Angkat.\r\n4. Tata di atas piring saji. Taburi dengan daun ketumbar.\r\n ', '', '2022-07-22', '12.30.30', '2.png', 1, '9/10', 12, ''),
(3, 4, 'Mie Ayam Solo', 'Mie ayam dari daerah ini menggunakan lebih banyak bumbu, terutama untuk ayam kecapnya. Selain kunyit, rempah-rempah yang digunakan adalah jahe, kemiri, dan ketumbar.', 'Noodle', '3-4 people', '1 hour 45 minutes', '1. 500 gram mie telur/mie basah\r\n2. 250 gram daging ayam, rebus dan potong 3. kotak-kotak\r\n4. 100 ml kecap asin\r\n5. 10 ml kecap manis\r\n6. 15 ml kecap inggris\r\n7. 2 batang daun bawang, iris halus\r\n8. 1 ikat sawi hijau/caisim, potong 5 cm', '1. Persiapkan kaldu ayam terlebih dahulu. Rebus tulang ayam dan 250 gram daging ayam yang akan dipakai untuk mie dengan 2000 ml air. Tambahkan merica dan garam. Masak hingga kuah mendidih dan ayam matang.\r\n2. Ambil daging ayam rebus yang akan digunakan untuk mie, kemudian potong dadu.\r\n3. Haluskan semua bahan-bahan bumbu. Panaskan sedikit minyak goreng, kemudian tumis bumbu hingga harum.\r\n4. Masukkan daging ayam yang sudah direbus dan dipotong dadu, kemudian bumbui lagi dengan sedikit gula pasir.\r\n5. Selanjutnya kita akan membuat minyak ayam. Tuang minyak sayur di wajan. Masukkan kulit dan lemak ayam. Masak dengan api kecil hingga kulit dan lemak ayam kering. Angkat dari penggorengan, kemudian masukkan bawang putih cincang. Masak sampai bawang putih garing dan minyak ayam siap digunakan.\r\n6. Rebus mie telur/mie basah bersama sawi, kemudian tiriskan airnya.\r\n7. Siapkan mangkuk, kemudian masukkan 1 sendok minyak ayam. Aduk bersama mie dan sawi hijau. Tambahkan air rebusan kaldu ke dalam mangkuk.\r\n8. Tuangkan tumisan ayam di atasnya, lalu taburi bawang goreng dan irisan daun bawang.\r\n9. Sajikan mie ayam Solo selagi hangat. Lengkapi dengan saus sambal dan saus tomat di wadah terpisah.', '', '2022-07-25', '11:40:20', '3.png', 1, '9.5/10', 56, ''),
(4, 4, 'Ramen Special', 'Ramen telah menjadi salah satu makanan penghibur utama di Jepang. Ramen menjadi populer berkat masakan Cina di restoran Tokyo yang menyajikan shina soba (Shina yang berarti Cina dan soba yang berarti mie soba). Selama bertahun-tahun, shina soba menjadi salah satu masakan Cina paling populer di Jepang.', 'Noodle', '1-2 people', '1 hour 23 minutes', '1. 100 gram mie ramen\r\n150 gram udang kupas\r\n2. Daun sawi hijau (secukupnya, potong sesuai selera)\r\n3. Kacang polong (secukupnya)\r\n4. 5 iris daging yang sudah direbus dengan kaldu ayam dan bumbu garam\r\n5. 1 buah telur (iris jadi dua bagian)\r\n6. Minyak goreng (secukupnya, untuk menumis bumbu)\r\n7. Air (secukupnya)', '1. Rebus mie ramen hingga matang menggunakan air kaldu ayam lalu tiriskan dan sisihkan sebentar.\r\n2. Panaskan minyak, tumis bawang putih dan bawang bombay hingga harum.\r\n3. Tambahkan daun sawi, kacang polong dan bumbu halus ke dalamnya.\r\n4. Tambahkan air dan didihkan.\r\n5. Jika sudah mendidih, masukkan mie dan masak hingga kembali mendidih.\r\n6. Tambahkan tepung sagu yang sudah dilarutkan ke dalam mie ramen dan masak hingga meletup-letup.\r\n7. Sajikan di mangkok saji dan tambahkan irisan daging juga telur di atasnya.', '', '2022-07-25', '11:40:20', '4.png', 1, '7/10', 78, ''),
(5, 2, 'Ayam Bakar Kecap Manis', 'Kali ini, kita akan mencoba cara membuat ayam bakar gurih nikmat yang diungkep dengan bumbu halus, santan, gula merah, dan kecap manis. Ungkep, berasal dari bahasa Jawa, adalah proses memasak bahan makanan yang sudah dibumbui dengan api kecil dan dalam waktu yang lama. Setelah ayam diungkep hingga empuk, panggang ayam sambil diolesi bumbu dan kecap.', 'Meat', '1-2 people', '2 hour 4 minutes', '1. 800 g (1 ekor) ayam pejantan, potong jadi 4 bagian\r\n2. 4 lembar daun salam\r\n3. 2 batang serai, memarkan\r\n4. 20 g asam Jawa, larutkan dengan 3 sdm air panas \r\n5. 500 ml santan\r\n6. 2 sdm minyak, untuk menumis\r\n7. 10 butir bawang merah\r\n8. 5 siung bawang putih\r\n9. 1 sdm ketumbar, sangrai\r\n10. 5 butir kemiri\r\n11. ½ sdt merica bubuk\r\n12. 3 cm kunyit\r\n13. 50 g gula merah, sisir halus\r\n14. 1 sdt garam\r\n15. 1 sdt gula pasir', '1. Panaskan minyak, tumis bumbu halus, daun salam, dan serai hingga harum.\r\n2. Masukkan air larutan asam Jawa dan ayam, aduk rata.\r\n3. Tuangkan santan, aduk perlahan agar santan tidak pecah.\r\n4. Masak hingga santan meresap dan ayam empuk. Angkat dan sisihkan.\r\n5. Panaskan wajan, panggang ayam sambil diolesi bumbu halus dan kecap. Panggang hingga berwarna kecokelatan.\r\n6. Angkat dan sajikan.', '', '2022-07-25', '12.30.30', '5.png', 1, '6/10', 12, ''),
(6, 2, 'Capcay Goreng Bakso', 'Capcay goreng dengan isian lengkap dan bumbu mantap ini bisa jadi pilihan lauk enak yang menyegarkan.', 'Vegetables', '3-4 people', '1 hour 34 minutes', '100 g wortel, potong-potong\r\n5 buah jagung muda, iris kasar\r\n100 g kembang kol, potong-potong\r\n3 sdm minyak sayur\r\n1 sdt minyak wijen\r\n2 siung bawang putih, cincang halus\r\n20 g bawang bombay, cincang\r\n5 buah bakso sapi, iris kasar\r\n100 g fillet ayam, potong-potong\r\n200 ml air\r\n2 tangkai sawi, potong-potong\r\n2 buah jamur hioko, iris kasar\r\n3 sdm saus tiram\r\n1 sdm kecap manis\r\n2 sdm kecap asin\r\n1/2 sdt merica bubuk\r\n1 sdt garam\r\n1 sdt tepung kanji, larutkan dengan air', 'Didihkan sedikit air dalam panci, rebus sebentar jagung muda, kembang kol dan wortel hingga hampir lunak. \r\n\r\nAngkat dan rendam air hingga dingin lalu tiriskan.\r\n\r\nPanaskan minyak, tumis bawang putih dan bawang bombay hingga layu dan kuning.\r\n\r\nMasukkan irisan bakso dan ayam, aduk hingga kaku.\r\n\r\nTambahkan jamur, aduk hingga layu.\r\n\r\nTambahkan kembang kol, wortel, jagung muda dan sawi. Aduk hingga layu.\r\n\r\nTuangi air dan didihkan sebentar.\r\n\r\nBumbui dengan saus tiram, kecap asin, kecap manis, merica dan garam. Aduk hingga mendidih.\r\n\r\nTuangi larutan kanji, aduk hingga kental lalu angkat.', '', '2022-07-25', '11:40:20', '6.png', 1, '6/10', 45, ''),
(7, 2, 'Sayur Bayam Bening', 'Sayur bayam bukan hanya kaya gizi tapi juga enak diolah menjadi menu makanan apa saja, mulai dari sayur bening hingga goreng sebagai keripik. Ini sekian resep sayur bayam enak yang bisa dicaba di rumah.', 'Vegetables', '2-3 people', '35 minutes', '1 ikat bayam\r\n1 buah jagung manis\r\n2 siung bawang putih\r\n3 siung bawang merah\r\n1 ruas kunci\r\nGaram, gula dan kaldu bubuk secukupnya', 'Siangi bayam, cuci bersih lalu sisihkan.\r\n\r\nPotong jagung manis menjadi 3 bagian, sisihkan.\r\n\r\nIris halus bawang merah dan bawang putih, geprek kunci.\r\n\r\nDidihkan air, masukkan jagung manis dan bawang merah, bawang putih dan kunci.\r\n\r\nMasak hingga setengah matang.\r\n\r\nMasukkan bayam, lalu tambahkan garam, gula dan kaldu bubuk secukupnya. Tes rasa.\r\n\r\nSayur bayam yang segar dan enak siap disajikan.', '', '2022-07-25', '11:40:20', '7.png', 1, '4/10', 5, ''),
(8, 2, 'Jus Jeruk', 'Resep jus jeruk super segar dan mudah untuk dibuat', 'Drinks', '1 people', '15 minutes', '3 Buah Jeruk\r\n1 cup susu kental manis\r\nEs Batu\r\nAir gula (jika diinginkan)', 'Masukan jeruk kedalam blender\r\n\r\nMasukan air,es batu dengan air gula, lalu diaduk\r\n\r\nLalu sajikan dengan gelas dan diberi hiasan kulit jeruk.', 'Jika di Blender Jeruknya berbusa dan cepat bau.', '2022-07-25', '12.30.30', '8.png', 1, '7/10', 72, ''),
(9, 4, 'Bakso Sapi Istimewa', 'Bakso istimewa dijamin enakk', 'Meat', '2-3 people', '1 hour 42 minutes', '3 ons daging sapi giling\r\n4 sendok tepung kanji (jangan sampai menggunung)\r\n3 sendok tepung terigu (jangan sampai menggunung\r\n2 butir telur\r\n1 siung bawang putih\r\nsecukupnya Garam\r\nsecukupnya Kaldu jamur\r\nsecukupnya Merica\r\nsecukupnya Air untuk merebus', 'Siapkan daging, tepung kanji, tepung terigu, garam, kaldu jamur, dan merica pada mangkuk besar atau baskom.\r\n\r\nDidihkan air untuk merebus bakso. \r\n\r\nKemudian blender bawang putih dengan 1 butir telur.\r\n\r\nSetelah itu tuangkan telur yg sudah di blender pada adonan bakso tadi. Aduk sampai rata. Lalu, masukkan telur 1 butir. Aduk sampai kalis. Jangan lupa koreksi rasa.\r\n\r\nSetelah air mendidih dan adonan sudah siap, buatlah bakso dengan menggunakan sendok. Dan ketika bakso sudah mengapung, angkat dan tiriskan.\r\n\r\nBakso sapi siap untuk diolah untuk berbagai macam masakan.', 'Gunakan garam secukupnya', '2022-07-25', '11:40:20', '9.png', 1, '9/10', 102, ''),
(10, 4, 'Jus strawberry', 'Resep jus strawberry sederhana yang nikmat dan menyegarkan', 'Drinks', '1 people', '15 minutes', '10 buah strawberry\n5 sdm kental manis putih\n2 sdm gula pasir\n200 ml air matang\n6 kotak kecil es batu', 'Cuci buah strawberry sampai bersih, kemudian buang bagian tangkainya\r\n\r\nPotong2 buah strawberry, masukkan kedalam blender tambahkan air, gula pasir, kental manis dan es batu\r\n\r\nBlender sampai tercampur rata dan halus\r\n\r\nSiap disajikan', 'Gunakan strawberry yang benar benar matang agar rasanya tidak asam, serta tambahkan susu bubuk atau madu agar menambah kelezatannya', '2022-07-25', '12.30.30', '10.png', 1, '10/10', 109, '');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(6) NOT NULL,
  `username` varchar(80) NOT NULL,
  `email` varchar(80) NOT NULL,
  `password` varchar(150) NOT NULL,
  `date` varchar(80) NOT NULL,
  `time` varchar(80) NOT NULL,
  `photo_profile` varchar(255) NOT NULL DEFAULT 'default.png',
  `biography` text NOT NULL,
  `job` varchar(255) NOT NULL,
  `gender` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `date`, `time`, `photo_profile`, `biography`, `job`, `gender`) VALUES
(2, 'Eve Ichwan', 'eve@gmail.com', '38adbdb825dbf4601a6fc697515c88c4', '23-07-2022', '15:16:23', '2.png', '', '', ''),
(4, 'kevin toding', 'gwareshop@gmail.com', 'ddcc77b15e9ff9a3c2a6f60344df9062', '23-07-2022', '15:20:03', 'default.png', '', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`comment_id`);

--
-- Indexes for table `recipe`
--
ALTER TABLE `recipe`
  ADD PRIMARY KEY (`recipe_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `comment_id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `recipe`
--
ALTER TABLE `recipe`
  MODIFY `recipe_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
