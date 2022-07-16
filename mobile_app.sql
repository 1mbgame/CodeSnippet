-- phpMyAdmin SQL Dump
-- version 4.4.11
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 14, 2022 at 08:28 AM
-- Server version: 5.6.25
-- PHP Version: 5.3.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mobile_app`
--

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_role` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `name`, `password`, `token`, `user_role`, `updated_at`, `created_at`, `is_deleted`) VALUES
(1, 'andy1@gmail.com', '', '$2a$10$S8vrmuDVUcjSycBXVddY.eEQPaJWENjzqNVhK..ZtSVPDiuivH0Sy', 'D1656368063165_VLYya3u6NmjD', 'USER', '2022-06-28 06:14:23.026000', '2022-06-28 06:14:23.026000', b'0'),
(2, 'andy2@gmail.com', '', '123', 'D1656803657020_ZNqA0zrBVBK1', 'ADMIN', '2022-07-03 07:14:17.017000', '2022-07-03 07:14:17.017000', b'0'),
(3, 'andy3@gmail.com', '', '$2a$10$PaqPc0OwtbjjKyqYyBpqrO94YEGHFrYmG/h6xiFgZ5r8IcRnY6pvO', 'D1656803756273_ZuWNYs3euiQu', 'USER', '2022-07-03 07:15:56.047000', '2022-07-03 07:15:56.047000', b'0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_username` (`username`(191)) USING BTREE;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
