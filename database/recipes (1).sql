-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 23, 2022 at 09:33 AM
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
  `id` int(6) NOT NULL,
  `recipe_id` int(10) NOT NULL,
  `user_id` int(6) NOT NULL,
  `body` text NOT NULL,
  `date_time` varchar(15) NOT NULL
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
  `username` varchar(30) NOT NULL,
  `title` varchar(150) NOT NULL,
  `description` text NOT NULL,
  `category` varchar(80) NOT NULL,
  `serve` varchar(80) NOT NULL,
  `duration` varchar(80) NOT NULL,
  `ingredients` text NOT NULL,
  `directions` text NOT NULL,
  `upload_date` varchar(50) NOT NULL,
  `upload_time` varchar(50) NOT NULL,
  `image` varchar(300) NOT NULL,
  `status` int(1) NOT NULL,
  `ratings` varchar(80) NOT NULL,
  `likes` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
(1, 'Jenu Ruru Pasa', 'jenirpasa@gmail.com', 'fcd56af41ace720b2585924d213574cc', '23-07-2022', '15:14:16', 'default.png', '', '', ''),
(2, 'eve ichwan', 'eve@gmail.com', '38adbdb825dbf4601a6fc697515c88c4', '23-07-2022', '15:16:23', 'default.png', '', '', ''),
(3, 'eve ichwan', 'eve@gmail.com', '38adbdb825dbf4601a6fc697515c88c4', '23-07-2022', '15:17:06', 'default.png', '', '', ''),
(4, 'kevin toding', 'gwareshop@gmail.com', 'ddcc77b15e9ff9a3c2a6f60344df9062', '23-07-2022', '15:20:03', 'default.png', '', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`);

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
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `recipe`
--
ALTER TABLE `recipe`
  MODIFY `recipe_id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
