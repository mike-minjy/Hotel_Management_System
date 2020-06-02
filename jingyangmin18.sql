-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 02, 2020 at 08:52 PM
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
  `userID` int(10) UNSIGNED NOT NULL,
  `bookedRoom` bit(1) NOT NULL,
  `dishesType_ID` int(10) UNSIGNED NOT NULL,
  `orderDate` datetime NOT NULL,
  `serveDate` datetime NOT NULL,
  `count` int(10) UNSIGNED NOT NULL DEFAULT 1,
  `totalPrice` float UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `bookedmeal`
--

INSERT INTO `bookedmeal` (`bookedMeal_ID`, `userID`, `bookedRoom`, `dishesType_ID`, `orderDate`, `serveDate`, `count`, `totalPrice`) VALUES
(1, 7, b'1', 6, '2020-06-02 22:52:45', '2020-06-04 12:23:00', 5, 28),
(2, 7, b'0', 7, '2020-06-02 23:09:44', '2020-06-03 07:30:00', 1, 2);

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
(16, 6, 913, '2020-05-15', '2020-11-01', '2020-05-14 19:11:56'),
(17, 6, 607, '2020-05-16', '2020-12-01', '2020-05-14 19:20:27'),
(18, 6, 107, '2020-05-15', '2020-11-30', '2020-05-14 20:03:23'),
(19, 6, 310, '2020-05-15', '2020-11-29', '2020-05-14 20:13:43'),
(20, 7, 713, '2020-06-04', '2020-06-05', '2020-06-02 14:36:17');

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
(1, 'test', '123', 'zzz', 'TEST123', 123456789, '2579583@test.com', '2020-04-23 14:12:48'),
(2, 'mika', '12345', 'mmm', 'ER102897', 222555666, '2579583@student.xjtlu.edu.cn', '2020-05-04 14:12:48'),
(3, 'trigger', '123', 'mike sakila', 'ER1252363', 18972634381, '26839586@qq.com', '2020-06-02 10:43:50'),
(4, 'mikalon', '666', 'mika', 'QS553322', 59283098, '33322_dewd@mail.com', '2020-05-07 13:44:20'),
(5, 'ayasaki', '23335', 'miuki', 'NS9365605', 1223366778, '', '2020-05-08 14:35:02'),
(6, 'timi', '12356', 'smalion', 'EC335678', 123563216, '', '2020-05-17 12:49:49'),
(7, 'minicap', '333666', 'mike simon', 'WCMM22233', 22222256, '2553755@123.com', '2020-05-11 06:58:55'),
(8, 'muniku', '2333666', 'mikunika', 'EC233366', 222369796, '2333@qq.com', '2020-05-13 12:55:34'),
(9, 'ssscc', 'w2', 'minitab', 'WEVE332', 12389795, '', '2020-05-11 03:05:49');

-- --------------------------------------------------------

--
-- Table structure for table `meal`
--

CREATE TABLE `meal` (
  `dishesType_ID` int(10) UNSIGNED NOT NULL,
  `chefID` tinyint(3) UNSIGNED NOT NULL,
  `dishes` varchar(60) NOT NULL,
  `price` float UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `meal`
--

INSERT INTO `meal` (`dishesType_ID`, `chefID`, `dishes`, `price`) VALUES
(1, 1, 'Shrimp soup', 5),
(2, 1, 'Cauliflower and mushroom stew', 3),
(3, 1, 'spicy chicken nuggets', 6),
(4, 1, 'steamed cod fish', 8),
(5, 1, 'turkey burger', 10),
(6, 1, 'veggie burger', 7),
(7, 1, 'fried egg', 2),
(8, 2, 'Chicken curry', 5),
(9, 2, 'Chicken masala', 5),
(10, 2, 'Mutton Korma', 8),
(11, 2, 'Keema Curry', 5),
(12, 2, 'Mushroom Tikka', 6),
(13, 2, 'fried egg', 2),
(14, 2, 'curry rice', 3),
(15, 3, 'Tofu teriyaki', 6),
(16, 3, 'Shrimp Tempura', 7),
(17, 3, 'Yaki Udon', 6),
(18, 3, 'Chicken Katsu', 6),
(19, 3, 'Salmon sashimi', 9),
(20, 3, 'fried egg', 2),
(21, 3, 'curry rice', 3),
(22, 4, 'Black pepper beef', 9),
(23, 4, 'Pork chowmein', 8),
(24, 4, 'Sweet & sour pork', 8),
(25, 4, 'Gongbao chicken', 7),
(26, 4, 'Pork Jiaozi', 8),
(27, 4, 'Soy glazed pork chops', 7),
(28, 4, 'curry rice', 3);

-- --------------------------------------------------------

--
-- Table structure for table `oneweek`
--

CREATE TABLE `oneweek` (
  `day_ID` tinyint(3) UNSIGNED NOT NULL,
  `day_Name` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `oneweek`
--

INSERT INTO `oneweek` (`day_ID`, `day_Name`) VALUES
(1, 'Monday'),
(2, 'Tuesday'),
(3, 'Wednesday'),
(4, 'Thursday'),
(5, 'Friday'),
(6, 'Saturday'),
(7, 'Sunday');

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
  `weekday_ID` int(10) UNSIGNED NOT NULL,
  `chefID` tinyint(3) UNSIGNED NOT NULL,
  `day_ID` tinyint(3) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `schedule`
--

INSERT INTO `schedule` (`weekday_ID`, `chefID`, `day_ID`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 1, 5),
(6, 2, 3),
(7, 2, 4),
(8, 2, 5),
(9, 2, 6),
(10, 2, 7),
(11, 3, 1),
(12, 3, 4),
(13, 3, 6),
(14, 4, 2),
(15, 4, 6),
(16, 4, 7);

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
(2, 'tigger', '2333', 987658632, '2020-05-19 12:58:08'),
(3, 'scott', '111', 2123535768, '2020-05-09 02:03:31');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookedmeal`
--
ALTER TABLE `bookedmeal`
  ADD PRIMARY KEY (`bookedMeal_ID`),
  ADD KEY `fk_BookedMeal_Guest` (`userID`),
  ADD KEY `fk_BookedMeal_Meal` (`dishesType_ID`);

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
  ADD PRIMARY KEY (`dishesType_ID`),
  ADD KEY `fk_Meal_Chef` (`chefID`);

--
-- Indexes for table `oneweek`
--
ALTER TABLE `oneweek`
  ADD PRIMARY KEY (`day_ID`);

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
  ADD PRIMARY KEY (`weekday_ID`),
  ADD KEY `fk_Schedule_Chef` (`chefID`),
  ADD KEY `fk_Schedule_OneWeek` (`day_ID`);

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
  MODIFY `bookedMeal_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `bookedroom`
--
ALTER TABLE `bookedroom`
  MODIFY `bookedRoom_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

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
-- AUTO_INCREMENT for table `meal`
--
ALTER TABLE `meal`
  MODIFY `dishesType_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `oneweek`
--
ALTER TABLE `oneweek`
  MODIFY `day_ID` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `roomtype`
--
ALTER TABLE `roomtype`
  MODIFY `roomTypeID` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `schedule`
--
ALTER TABLE `schedule`
  MODIFY `weekday_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

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
  ADD CONSTRAINT `fk_BookedMeal_Guest` FOREIGN KEY (`userID`) REFERENCES `guest` (`userID`),
  ADD CONSTRAINT `fk_BookedMeal_Meal` FOREIGN KEY (`dishesType_ID`) REFERENCES `meal` (`dishesType_ID`);

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
  ADD CONSTRAINT `fk_Schedule_Chef` FOREIGN KEY (`chefID`) REFERENCES `chef` (`chefID`),
  ADD CONSTRAINT `fk_Schedule_OneWeek` FOREIGN KEY (`day_ID`) REFERENCES `oneweek` (`day_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
