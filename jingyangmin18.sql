/*START FILE*/

-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 20, 2020 at 08:08 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jingyangmin18`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookedmeal`
--

CREATE TABLE `bookedmeal` (
  `bookedMeal_ID` int(10) UNSIGNED NOT NULL,
  `bookedRoom_ID` int(10) UNSIGNED DEFAULT NULL,
  `chefID` tinyint(3) UNSIGNED NOT NULL,
  `dishes` varchar(60) NOT NULL,
  `orderDate` datetime NOT NULL,
  `serveDate` datetime NOT NULL,
  `count` int(10) UNSIGNED NOT NULL DEFAULT 1,
  `totalPrice` float UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `bookedroom`
--

CREATE TABLE `bookedroom` (
  `bookedRoom_ID` int(10) UNSIGNED NOT NULL,
  `userID` int(10) UNSIGNED NOT NULL,
  `roomID` int(10) UNSIGNED NOT NULL,
  `checkInDate` date NOT NULL,
  `checkOutDate` date NOT NULL,
  `operationTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `bookedroom`
--

INSERT INTO `bookedroom` (`bookedRoom_ID`, `userID`, `roomID`, `checkInDate`, `checkOutDate`, `operationTime`) VALUES
(1, 2, 113, '2020-05-16', '2020-05-18', '2020-05-12 21:36:02'),
(2, 3, 210, '2020-05-06', '2020-05-22', '2020-05-05 04:38:51'),
(3, 4, 707, '2020-04-23', '2020-05-19', '2020-04-09 07:38:59'),
(4, 5, 906, '2020-05-06', '2020-05-14', '2020-05-13 05:59:42'),
(5, 2, 906, '2020-02-06', '2020-03-12', '2020-05-12 21:36:10'),
(6, 2, 505, '2020-03-20', '2020-04-16', '2020-05-12 21:37:35'),
(7, 7, 701, '2020-05-11', '2020-05-11', '2020-05-12 21:37:36'),
(8, 8, 313, '2020-05-11', '2020-05-11', '2020-05-12 21:37:38'),
(9, 9, 113, '2020-05-12', '2020-05-14', '2020-05-13 08:12:40'),
(10, 6, 804, '2020-05-12', '2020-05-12', '2020-05-12 21:37:41'),
(11, 5, 313, '2020-05-12', '2020-05-14', '2020-05-13 07:07:20'),
(12, 7, 513, '2020-11-01', '2020-12-01', '2020-05-13 02:04:55'),
(13, 4, 113, '2020-05-15', '2020-05-15', '2020-05-13 09:07:05'),
(14, 6, 713, '2020-05-15', '2020-05-16', '2020-05-14 18:59:18'),
(15, 6, 913, '2020-11-05', '2020-11-20', '2020-05-14 19:05:30'),
(16, 6, 913, '2020-05-15', '2020-12-01', '2020-05-14 19:11:56'),
(17, 6, 607, '2020-05-16', '2020-12-01', '2020-05-14 19:20:27'),
(18, 6, 107, '2020-05-15', '2020-11-30', '2020-05-14 20:03:23'),
(19, 6, 310, '2020-05-15', '2020-11-29', '2020-05-14 20:13:43');

-- --------------------------------------------------------

--
-- Table structure for table `chef`
--

CREATE TABLE `chef` (
  `chefID` tinyint(3) UNSIGNED NOT NULL,
  `chefName` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `chef`
--

INSERT INTO `chef` (`chefID`, `chefName`) VALUES
(1, 'Karen Adam'),
(2, 'Hari Philip'),
(3, 'Thalia Hensley'),
(4, 'Nisha Moss');

-- --------------------------------------------------------

--
-- Table structure for table `guest`
--

CREATE TABLE `guest` (
  `userID` int(10) UNSIGNED NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `realName` varchar(255) NOT NULL,
  `passportID` varchar(9) NOT NULL,
  `telephoneNumber` bigint(20) UNSIGNED NOT NULL,
  `email` varchar(255) DEFAULT '',
  `operationTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `guest`
--

INSERT INTO `guest` (`userID`, `username`, `password`, `realName`, `passportID`, `telephoneNumber`, `email`, `operationTime`) VALUES
(2, 'mika', '12345', 'mmm', 'ER102897', 222555666, '2579583@student.xjtlu.edu.cn', '2020-05-04 14:12:48'),
(3, 'trigger', '123', 'mike-minjy', 'ER1252363', 18972634381, '26839586@qq.com', '2020-05-04 18:19:46'),
(4, 'mikalon', '666', 'mika', 'QS553322', 59283098, '33322_dewd@mail.com', '2020-05-07 13:44:20'),
(5, 'ayasaki', '23335', 'miuki', 'NS9365605', 1223366778, NULL, '2020-05-08 14:35:02'),
(6, 'timi', '12356', 'smalion', 'WC33567', 123563216, NULL, '2020-05-11 04:50:03'),
(7, 'minicap', '333666', 'mike simon', 'WCMM22233', 22222256, '2553755@123.com', '2020-05-11 06:58:55'),
(8, 'muniku', '2333666', 'mikunika', 'EC233366', 222369796, '2333@qq.com', '2020-05-13 12:55:34'),
(9, 'ssscc', 'w2', 'minitab', 'WEVE332', 12389795, '', '2020-05-11 03:05:49');

-- --------------------------------------------------------

--
-- Table structure for table `meal`
--

CREATE TABLE `meal` (
  `dishes` varchar(60) NOT NULL,
  `chefID` tinyint(3) UNSIGNED NOT NULL,
  `price` float UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `meal`
--

INSERT INTO `meal` (`dishes`, `chefID`, `price`) VALUES
('Shrimp soup', 1, 0),
('Cauliflower and mushroom stew', 1, 0),
('spicy chicken nuggets', 1, 0),
('steamed cod fish', 1, 0),
('turkey burger', 1, 0),
('veggie burger', 1, 0),
('fried egg', 1, 0),
('Chicken curry', 2, 0),
('Chicken masala', 2, 0),
('Mutton Korma', 2, 0),
('Keema Curry', 2, 0),
('Mushroom Tikka', 2, 0),
('fried egg', 2, 0),
('curry rice', 2, 0),
('Tofu teriyaki', 3, 0),
('Shrimp Tempura', 3, 0),
('Yaki Udon', 3, 0),
('Chicken Katsu', 3, 0),
('Salmon sashimi', 3, 0),
('fried egg', 3, 0),
('curry rice', 3, 0),
('Black pepper beef', 4, 0),
('Pork chowmein', 4, 0),
('Sweet & sour pork', 4, 0),
('Gongbao chicken', 4, 0),
('Pork Jiaozi', 4, 0),
('Soy glazed pork chops', 4, 0),
('curry rice', 4, 0);

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `roomID` int(10) UNSIGNED NOT NULL,
  `roomTypeID` tinyint(3) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`roomID`, `roomTypeID`) VALUES
(101, 1),
(102, 1),
(111, 1),
(112, 1),
(201, 1),
(202, 1),
(211, 1),
(212, 1),
(301, 1),
(302, 1),
(311, 1),
(312, 1),
(401, 1),
(402, 1),
(411, 1),
(412, 1),
(501, 1),
(502, 1),
(511, 1),
(512, 1),
(601, 1),
(602, 1),
(611, 1),
(612, 1),
(701, 1),
(702, 1),
(711, 1),
(712, 1),
(801, 1),
(802, 1),
(811, 1),
(812, 1),
(901, 1),
(902, 1),
(911, 1),
(912, 1),
(1001, 1),
(1002, 1),
(1011, 1),
(1012, 1),
(103, 2),
(104, 2),
(109, 2),
(110, 2),
(203, 2),
(204, 2),
(209, 2),
(210, 2),
(303, 2),
(304, 2),
(309, 2),
(310, 2),
(403, 2),
(404, 2),
(409, 2),
(410, 2),
(503, 2),
(504, 2),
(509, 2),
(510, 2),
(603, 2),
(604, 2),
(609, 2),
(610, 2),
(703, 2),
(704, 2),
(709, 2),
(710, 2),
(803, 2),
(804, 2),
(809, 2),
(810, 2),
(903, 2),
(904, 2),
(909, 2),
(910, 2),
(1003, 2),
(1004, 2),
(1009, 2),
(1010, 2),
(105, 3),
(106, 3),
(107, 3),
(108, 3),
(205, 3),
(206, 3),
(207, 3),
(208, 3),
(305, 3),
(306, 3),
(307, 3),
(308, 3),
(405, 3),
(406, 3),
(407, 3),
(408, 3),
(505, 3),
(506, 3),
(507, 3),
(508, 3),
(605, 3),
(606, 3),
(607, 3),
(608, 3),
(705, 3),
(706, 3),
(707, 3),
(708, 3),
(805, 3),
(806, 3),
(807, 3),
(808, 3),
(905, 3),
(906, 3),
(907, 3),
(908, 3),
(1005, 3),
(1006, 3),
(1007, 3),
(1008, 3),
(113, 4),
(213, 4),
(313, 4),
(413, 4),
(513, 4),
(613, 4),
(713, 4),
(813, 4),
(913, 4),
(1013, 4);

-- --------------------------------------------------------

--
-- Table structure for table `roomtype`
--

CREATE TABLE `roomtype` (
  `roomTypeID` tinyint(3) UNSIGNED NOT NULL,
  `roomType` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `roomtype`
--

INSERT INTO `roomtype` (`roomTypeID`, `roomType`) VALUES
(1, 'Large double bed'),
(2, 'Large single bed'),
(3, 'Small single bed'),
(4, 'VIP Room');

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE `schedule` (
  `weekday` varchar(10) NOT NULL,
  `chefID` tinyint(3) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `schedule`
--

INSERT INTO `schedule` (`weekday`, `chefID`) VALUES
('Monday', 1),
('Tuesday', 1),
('Wednesday', 1),
('Thursday', 1),
('Friday', 1),
('Wednesday', 2),
('Thursday', 2),
('Friday', 2),
('Saturday', 2),
('Sunday', 2),
('Monday', 3),
('Thursday', 3),
('Saturday', 3),
('Tuesday', 4),
('Saturday', 4),
('Sunday', 4);

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `staffID` int(10) UNSIGNED NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `telephoneNumber` bigint(20) UNSIGNED NOT NULL,
  `operationTime` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`staffID`, `username`, `password`, `telephoneNumber`, `operationTime`) VALUES
(1, 'mike', '2333666', 8615833356871, '2020-05-14 18:58:33'),
(2, 'tiger', '2333', 987658632, '2020-05-13 18:58:47'),
(3, 'scott', '111', 2123535768, '2020-05-09 02:03:31');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookedmeal`
--
ALTER TABLE `bookedmeal`
  ADD PRIMARY KEY (`bookedMeal_ID`),
  ADD KEY `fk_BookedMeal_BookedRoom` (`bookedRoom_ID`),
  ADD KEY `fk_BookedMeal_Chef` (`chefID`),
  ADD KEY `fk_BookedMeal_Meal` (`dishes`);

--
-- Indexes for table `bookedroom`
--
ALTER TABLE `bookedroom`
  ADD PRIMARY KEY (`bookedRoom_ID`),
  ADD KEY `fk_BookedRoom_Guest` (`userID`),
  ADD KEY `fk_BookedRoom_Room` (`roomID`);

--
-- Indexes for table `chef`
--
ALTER TABLE `chef`
  ADD PRIMARY KEY (`chefID`);

--
-- Indexes for table `guest`
--
ALTER TABLE `guest`
  ADD PRIMARY KEY (`userID`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `meal`
--
ALTER TABLE `meal`
  ADD KEY `fk_Meal_Chef` (`chefID`),
  ADD KEY `dishes` (`dishes`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`roomID`),
  ADD KEY `fk_Room_RoomType` (`roomTypeID`);

--
-- Indexes for table `roomtype`
--
ALTER TABLE `roomtype`
  ADD PRIMARY KEY (`roomTypeID`);

--
-- Indexes for table `schedule`
--
ALTER TABLE `schedule`
  ADD KEY `fk_Schedule_Chef` (`chefID`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`staffID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookedmeal`
--
ALTER TABLE `bookedmeal`
  MODIFY `bookedMeal_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `bookedroom`
--
ALTER TABLE `bookedroom`
  MODIFY `bookedRoom_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `chef`
--
ALTER TABLE `chef`
  MODIFY `chefID` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `guest`
--
ALTER TABLE `guest`
  MODIFY `userID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `roomtype`
--
ALTER TABLE `roomtype`
  MODIFY `roomTypeID` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `staff`
--
ALTER TABLE `staff`
  MODIFY `staffID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookedmeal`
--
ALTER TABLE `bookedmeal`
  ADD CONSTRAINT `fk_BookedMeal_BookedRoom` FOREIGN KEY (`bookedRoom_ID`) REFERENCES `bookedroom` (`bookedRoom_ID`),
  ADD CONSTRAINT `fk_BookedMeal_Chef` FOREIGN KEY (`chefID`) REFERENCES `chef` (`chefID`),
  ADD CONSTRAINT `fk_BookedMeal_Meal` FOREIGN KEY (`dishes`) REFERENCES `meal` (`dishes`);

--
-- Constraints for table `bookedroom`
--
ALTER TABLE `bookedroom`
  ADD CONSTRAINT `fk_BookedRoom_Guest` FOREIGN KEY (`userID`) REFERENCES `guest` (`userID`),
  ADD CONSTRAINT `fk_BookedRoom_Room` FOREIGN KEY (`roomID`) REFERENCES `room` (`roomID`);

--
-- Constraints for table `meal`
--
ALTER TABLE `meal`
  ADD CONSTRAINT `fk_Meal_Chef` FOREIGN KEY (`chefID`) REFERENCES `chef` (`chefID`);

--
-- Constraints for table `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `fk_Room_RoomType` FOREIGN KEY (`roomTypeID`) REFERENCES `roomtype` (`roomTypeID`);

--
-- Constraints for table `schedule`
--
ALTER TABLE `schedule`
  ADD CONSTRAINT `fk_Schedule_Chef` FOREIGN KEY (`chefID`) REFERENCES `chef` (`chefID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

/*END FILE*/