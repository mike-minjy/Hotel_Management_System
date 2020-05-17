DROP TABLE IF EXISTS `bookedmeal`;
DROP TABLE IF EXISTS `schedule`;
DROP TABLE IF EXISTS `meal`;
DROP TABLE IF EXISTS `chef`;
DROP TABLE IF EXISTS `bookedroom`;
DROP TABLE IF EXISTS `guest`;
DROP TABLE IF EXISTS `room`;
DROP TABLE IF EXISTS `roomtype`;

CREATE TABLE `guest`  (
  `userID` int UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(255) UNIQUE NOT NULL,
  `password` varchar(255) NOT NULL,
  `realName` varchar(255) NOT NULL,
  `passportID` varchar(9) NOT NULL,
  `telephoneNumber` bigint UNSIGNED NOT NULL,
  `email` varchar(255) NULL DEFAULT '',
  `operationTime` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO `guest` VALUES (2, 'mika', '12345', 'mmm', 'ER102897', 222555666, '2579583@student.xjtlu.edu.cn', '2020-05-04 22:12:48');
INSERT INTO `guest` VALUES (3, 'trigger', '123', 'mike-minjy', 'ER1252363', 18972634381, '26839586@qq.com', '2020-05-05 02:19:46');
INSERT INTO `guest` VALUES (4, 'mikalon', '666', 'mika', 'QS553322', 59283098, '33322_dewd@mail.com', '2020-05-07 21:44:20');
INSERT INTO `guest` VALUES (5, 'ayasaki', '23335', 'miuki', 'NS9365605', 1223366778, NULL, '2020-05-08 22:35:02');
INSERT INTO `guest` VALUES (6, 'timi', '12356', 'smalion', 'WC33567', 123563216, NULL, '2020-05-11 12:50:03');
INSERT INTO `guest` VALUES (7, 'minicap', '333666', 'mike simon', 'WCMM22233', 22222256, '2553755@123.com', '2020-05-11 14:58:55');
INSERT INTO `guest` VALUES (8, 'muniku', '2333666', 'mikunika', 'EC233366', 222369796, '2333@qq.com', '2020-05-13 20:55:34');
INSERT INTO `guest` VALUES (9, 'ssscc', 'w2', 'minitab', 'WEVE332', 12389795, '', '2020-05-11 11:05:49');


CREATE TABLE `chef`  (
  `chefID` tinyint UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  `chefName` varchar(20) NOT NULL
);

INSERT INTO `chef` VALUES (1, 'Karen Adam');
INSERT INTO `chef` VALUES (2, 'Hari Philip');
INSERT INTO `chef` VALUES (3, 'Thalia Hensley');
INSERT INTO `chef` VALUES (4, 'Nisha Moss');

CREATE TABLE `meal`  (
  `dishes` varchar(60) NOT NULL,
  `chefID` tinyint UNSIGNED NOT NULL,
  `price` float UNSIGNED NOT NULL,
  CONSTRAINT `fk_Meal_Chef` FOREIGN KEY (`chefID`) REFERENCES `chef` (`chefID`)
);

INSERT INTO `meal` VALUES ('Shrimp soup', 1, 0);
INSERT INTO `meal` VALUES ('Cauliflower and mushroom stew', 1, 0);
INSERT INTO `meal` VALUES ('spicy chicken nuggets', 1, 0);
INSERT INTO `meal` VALUES ('steamed cod fish', 1, 0);
INSERT INTO `meal` VALUES ('turkey burger', 1, 0);
INSERT INTO `meal` VALUES ('veggie burger', 1, 0);
INSERT INTO `meal` VALUES ('fried egg', 1, 0);
INSERT INTO `meal` VALUES ('Chicken curry', 2, 0);
INSERT INTO `meal` VALUES ('Chicken masala', 2, 0);
INSERT INTO `meal` VALUES ('Mutton Korma', 2, 0);
INSERT INTO `meal` VALUES ('Keema Curry', 2, 0);
INSERT INTO `meal` VALUES ('Mushroom Tikka', 2, 0);
INSERT INTO `meal` VALUES ('fried egg', 2, 0);
INSERT INTO `meal` VALUES ('curry rice', 2, 0);
INSERT INTO `meal` VALUES ('Tofu teriyaki', 3, 0);
INSERT INTO `meal` VALUES ('Shrimp Tempura', 3, 0);
INSERT INTO `meal` VALUES ('Yaki Udon', 3, 0);
INSERT INTO `meal` VALUES ('Chicken Katsu', 3, 0);
INSERT INTO `meal` VALUES ('Salmon sashimi', 3, 0);
INSERT INTO `meal` VALUES ('fried egg', 3, 0);
INSERT INTO `meal` VALUES ('curry rice', 3, 0);
INSERT INTO `meal` VALUES ('Black pepper beef', 4, 0);
INSERT INTO `meal` VALUES ('Pork chowmein', 4, 0);
INSERT INTO `meal` VALUES ('Sweet & sour pork', 4, 0);
INSERT INTO `meal` VALUES ('Gongbao chicken', 4, 0);
INSERT INTO `meal` VALUES ('Pork Jiaozi', 4, 0);
INSERT INTO `meal` VALUES ('Soy glazed pork chops', 4, 0);
INSERT INTO `meal` VALUES ('curry rice', 4, 0);

CREATE TABLE `roomtype`  (
  `roomTypeID` tinyint UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  `roomType` varchar(30) NOT NULL
);

INSERT INTO `roomtype` VALUES (1, 'Large double bed');
INSERT INTO `roomtype` VALUES (2, 'Large single bed');
INSERT INTO `roomtype` VALUES (3, 'Small single bed');
INSERT INTO `roomtype` VALUES (4, 'VIP Room');

CREATE TABLE `room`  (
  `roomID` int UNSIGNED PRIMARY KEY,
  `roomTypeID` tinyint UNSIGNED NOT NULL,
  CONSTRAINT `fk_Room_RoomType` FOREIGN KEY (`roomTypeID`) REFERENCES `roomtype` (`roomTypeID`)
);

INSERT INTO `room` VALUES (101, 1);
INSERT INTO `room` VALUES (102, 1);
INSERT INTO `room` VALUES (111, 1);
INSERT INTO `room` VALUES (112, 1);
INSERT INTO `room` VALUES (201, 1);
INSERT INTO `room` VALUES (202, 1);
INSERT INTO `room` VALUES (211, 1);
INSERT INTO `room` VALUES (212, 1);
INSERT INTO `room` VALUES (301, 1);
INSERT INTO `room` VALUES (302, 1);
INSERT INTO `room` VALUES (311, 1);
INSERT INTO `room` VALUES (312, 1);
INSERT INTO `room` VALUES (401, 1);
INSERT INTO `room` VALUES (402, 1);
INSERT INTO `room` VALUES (411, 1);
INSERT INTO `room` VALUES (412, 1);
INSERT INTO `room` VALUES (501, 1);
INSERT INTO `room` VALUES (502, 1);
INSERT INTO `room` VALUES (511, 1);
INSERT INTO `room` VALUES (512, 1);
INSERT INTO `room` VALUES (601, 1);
INSERT INTO `room` VALUES (602, 1);
INSERT INTO `room` VALUES (611, 1);
INSERT INTO `room` VALUES (612, 1);
INSERT INTO `room` VALUES (701, 1);
INSERT INTO `room` VALUES (702, 1);
INSERT INTO `room` VALUES (711, 1);
INSERT INTO `room` VALUES (712, 1);
INSERT INTO `room` VALUES (801, 1);
INSERT INTO `room` VALUES (802, 1);
INSERT INTO `room` VALUES (811, 1);
INSERT INTO `room` VALUES (812, 1);
INSERT INTO `room` VALUES (901, 1);
INSERT INTO `room` VALUES (902, 1);
INSERT INTO `room` VALUES (911, 1);
INSERT INTO `room` VALUES (912, 1);
INSERT INTO `room` VALUES (1001, 1);
INSERT INTO `room` VALUES (1002, 1);
INSERT INTO `room` VALUES (1011, 1);
INSERT INTO `room` VALUES (1012, 1);
INSERT INTO `room` VALUES (103, 2);
INSERT INTO `room` VALUES (104, 2);
INSERT INTO `room` VALUES (109, 2);
INSERT INTO `room` VALUES (110, 2);
INSERT INTO `room` VALUES (203, 2);
INSERT INTO `room` VALUES (204, 2);
INSERT INTO `room` VALUES (209, 2);
INSERT INTO `room` VALUES (210, 2);
INSERT INTO `room` VALUES (303, 2);
INSERT INTO `room` VALUES (304, 2);
INSERT INTO `room` VALUES (309, 2);
INSERT INTO `room` VALUES (310, 2);
INSERT INTO `room` VALUES (403, 2);
INSERT INTO `room` VALUES (404, 2);
INSERT INTO `room` VALUES (409, 2);
INSERT INTO `room` VALUES (410, 2);
INSERT INTO `room` VALUES (503, 2);
INSERT INTO `room` VALUES (504, 2);
INSERT INTO `room` VALUES (509, 2);
INSERT INTO `room` VALUES (510, 2);
INSERT INTO `room` VALUES (603, 2);
INSERT INTO `room` VALUES (604, 2);
INSERT INTO `room` VALUES (609, 2);
INSERT INTO `room` VALUES (610, 2);
INSERT INTO `room` VALUES (703, 2);
INSERT INTO `room` VALUES (704, 2);
INSERT INTO `room` VALUES (709, 2);
INSERT INTO `room` VALUES (710, 2);
INSERT INTO `room` VALUES (803, 2);
INSERT INTO `room` VALUES (804, 2);
INSERT INTO `room` VALUES (809, 2);
INSERT INTO `room` VALUES (810, 2);
INSERT INTO `room` VALUES (903, 2);
INSERT INTO `room` VALUES (904, 2);
INSERT INTO `room` VALUES (909, 2);
INSERT INTO `room` VALUES (910, 2);
INSERT INTO `room` VALUES (1003, 2);
INSERT INTO `room` VALUES (1004, 2);
INSERT INTO `room` VALUES (1009, 2);
INSERT INTO `room` VALUES (1010, 2);
INSERT INTO `room` VALUES (105, 3);
INSERT INTO `room` VALUES (106, 3);
INSERT INTO `room` VALUES (107, 3);
INSERT INTO `room` VALUES (108, 3);
INSERT INTO `room` VALUES (205, 3);
INSERT INTO `room` VALUES (206, 3);
INSERT INTO `room` VALUES (207, 3);
INSERT INTO `room` VALUES (208, 3);
INSERT INTO `room` VALUES (305, 3);
INSERT INTO `room` VALUES (306, 3);
INSERT INTO `room` VALUES (307, 3);
INSERT INTO `room` VALUES (308, 3);
INSERT INTO `room` VALUES (405, 3);
INSERT INTO `room` VALUES (406, 3);
INSERT INTO `room` VALUES (407, 3);
INSERT INTO `room` VALUES (408, 3);
INSERT INTO `room` VALUES (505, 3);
INSERT INTO `room` VALUES (506, 3);
INSERT INTO `room` VALUES (507, 3);
INSERT INTO `room` VALUES (508, 3);
INSERT INTO `room` VALUES (605, 3);
INSERT INTO `room` VALUES (606, 3);
INSERT INTO `room` VALUES (607, 3);
INSERT INTO `room` VALUES (608, 3);
INSERT INTO `room` VALUES (705, 3);
INSERT INTO `room` VALUES (706, 3);
INSERT INTO `room` VALUES (707, 3);
INSERT INTO `room` VALUES (708, 3);
INSERT INTO `room` VALUES (805, 3);
INSERT INTO `room` VALUES (806, 3);
INSERT INTO `room` VALUES (807, 3);
INSERT INTO `room` VALUES (808, 3);
INSERT INTO `room` VALUES (905, 3);
INSERT INTO `room` VALUES (906, 3);
INSERT INTO `room` VALUES (907, 3);
INSERT INTO `room` VALUES (908, 3);
INSERT INTO `room` VALUES (1005, 3);
INSERT INTO `room` VALUES (1006, 3);
INSERT INTO `room` VALUES (1007, 3);
INSERT INTO `room` VALUES (1008, 3);
INSERT INTO `room` VALUES (113, 4);
INSERT INTO `room` VALUES (213, 4);
INSERT INTO `room` VALUES (313, 4);
INSERT INTO `room` VALUES (413, 4);
INSERT INTO `room` VALUES (513, 4);
INSERT INTO `room` VALUES (613, 4);
INSERT INTO `room` VALUES (713, 4);
INSERT INTO `room` VALUES (813, 4);
INSERT INTO `room` VALUES (913, 4);
INSERT INTO `room` VALUES (1013, 4);


CREATE TABLE `bookedroom`  (
  `bookedRoom_ID` int UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  `userID` int UNSIGNED NOT NULL,
  `roomID` int UNSIGNED NOT NULL,
  `checkInDate` date NOT NULL,
  `checkOutDate` date NOT NULL,
  `operationTime` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_BookedRoom_Guest` FOREIGN KEY (`userID`) REFERENCES `guest` (`userID`),
  CONSTRAINT `fk_BookedRoom_Room` FOREIGN KEY (`roomID`) REFERENCES `room` (`roomID`)
);

INSERT INTO `bookedroom` VALUES (1, 2, 113, '2020-05-16', '2020-05-18', '2020-05-13 05:36:02');
INSERT INTO `bookedroom` VALUES (2, 3, 210, '2020-05-06', '2020-05-22', '2020-05-05 12:38:51');
INSERT INTO `bookedroom` VALUES (3, 4, 707, '2020-04-23', '2020-05-19', '2020-04-09 15:38:59');
INSERT INTO `bookedroom` VALUES (4, 5, 906, '2020-05-06', '2020-05-14', '2020-05-13 13:59:42');
INSERT INTO `bookedroom` VALUES (5, 2, 906, '2020-02-06', '2020-03-12', '2020-05-13 05:36:10');
INSERT INTO `bookedroom` VALUES (6, 2, 505, '2020-03-20', '2020-04-16', '2020-05-13 05:37:35');
INSERT INTO `bookedroom` VALUES (7, 7, 701, '2020-05-11', '2020-05-11', '2020-05-13 05:37:36');
INSERT INTO `bookedroom` VALUES (8, 8, 313, '2020-05-11', '2020-05-11', '2020-05-13 05:37:38');
INSERT INTO `bookedroom` VALUES (9, 9, 113, '2020-05-12', '2020-05-14', '2020-05-13 16:12:40');
INSERT INTO `bookedroom` VALUES (10, 6, 804, '2020-05-12', '2020-05-12', '2020-05-13 05:37:41');
INSERT INTO `bookedroom` VALUES (11, 5, 313, '2020-05-12', '2020-05-14', '2020-05-13 15:07:20');
INSERT INTO `bookedroom` VALUES (12, 7, 513, '2020-11-01', '2020-12-01', '2020-05-13 10:04:55');
INSERT INTO `bookedroom` VALUES (13, 4, 113, '2020-05-15', '2020-05-15', '2020-05-13 17:07:05');
INSERT INTO `bookedroom` VALUES (14, 6, 713, '2020-05-15', '2020-05-16', '2020-05-15 02:59:18');
INSERT INTO `bookedroom` VALUES (15, 6, 913, '2020-11-05', '2020-11-20', '2020-05-15 03:05:30');
INSERT INTO `bookedroom` VALUES (16, 6, 913, '2020-05-15', '2020-12-01', '2020-05-15 03:11:56');
INSERT INTO `bookedroom` VALUES (17, 6, 607, '2020-05-16', '2020-12-01', '2020-05-15 03:20:27');
INSERT INTO `bookedroom` VALUES (18, 6, 107, '2020-05-15', '2020-11-30', '2020-05-15 04:03:23');
INSERT INTO `bookedroom` VALUES (19, 6, 310, '2020-05-15', '2020-11-29', '2020-05-15 04:13:43');

DROP TABLE IF EXISTS `future_room_info`;
CREATE TABLE `future_room_info`  (
  `bookedRoom_ID` int UNSIGNED PRIMARY KEY,
  `userID` int UNSIGNED NOT NULL,
  `roomID` int UNSIGNED NOT NULL,
  `checkInDate` date NOT NULL,
  `checkOutDate` date NOT NULL,
  `operationTime` timestamp NOT NULL
);

DROP TABLE IF EXISTS `overdue_room_info`;
CREATE TABLE `overdue_room_info`  (
  `bookedRoom_ID` int UNSIGNED PRIMARY KEY,
  `userID` int UNSIGNED NOT NULL,
  `roomID` int UNSIGNED NOT NULL,
  `checkInDate` date NOT NULL,
  `checkOutDate` date NOT NULL,
  `operationTime` timestamp NOT NULL
);

CREATE TABLE `schedule`  (
  `weekday` varchar(10) NOT NULL,
  `chefID` tinyint UNSIGNED NOT NULL,
  CONSTRAINT `fk_Schedule_Chef` FOREIGN KEY (`chefID`) REFERENCES `chef` (`chefID`)
);

INSERT INTO `schedule` VALUES ('Monday', 1);
INSERT INTO `schedule` VALUES ('Tuesday', 1);
INSERT INTO `schedule` VALUES ('Wednesday', 1);
INSERT INTO `schedule` VALUES ('Thursday', 1);
INSERT INTO `schedule` VALUES ('Friday', 1);
INSERT INTO `schedule` VALUES ('Wednesday', 2);
INSERT INTO `schedule` VALUES ('Thursday', 2);
INSERT INTO `schedule` VALUES ('Friday', 2);
INSERT INTO `schedule` VALUES ('Saturday', 2);
INSERT INTO `schedule` VALUES ('Sunday', 2);
INSERT INTO `schedule` VALUES ('Monday', 3);
INSERT INTO `schedule` VALUES ('Thursday', 3);
INSERT INTO `schedule` VALUES ('Saturday', 3);
INSERT INTO `schedule` VALUES ('Tuesday', 4);
INSERT INTO `schedule` VALUES ('Saturday', 4);
INSERT INTO `schedule` VALUES ('Sunday', 4);

DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff`  (
  `staffID` int UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `telephoneNumber` bigint UNSIGNED NOT NULL,
  `operationTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO `staff` VALUES (1, 'mike', '2333666', 8615833356871, '2020-05-15 02:58:33');
INSERT INTO `staff` VALUES (2, 'tiger', '2333', 987658632, '2020-05-14 02:58:47');
INSERT INTO `staff` VALUES (3, 'scott', '111', 2123535768, '2020-05-09 10:03:31');

CREATE TABLE `bookedmeal`  (
  `bookedMeal_ID` int UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  `bookedRoom_ID` int NOT NULL,
  `chefID` tinyint UNSIGNED NOT NULL,
  `dishes` varchar(60) NOT NULL,
  `orderDate` DATETIME NOT NULL,
  `serveDate` DATETIME NOT NULL,
  `count` int UNSIGNED NOT NULL DEFAULT 1,
  `totalPrice` float UNSIGNED NOT NULL
);