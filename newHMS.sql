/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : hms

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 02/06/2020 05:32:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bookedmeal
-- ----------------------------
DROP TABLE IF EXISTS `bookedmeal`;
CREATE TABLE `bookedmeal`  (
  `bookedMeal_ID` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `userID` int(0) UNSIGNED NOT NULL,
  `bookedRoom` bit(1) NOT NULL,
  `dishesType_ID` int(0) UNSIGNED NOT NULL,
  `orderDate` datetime(0) NOT NULL,
  `serveDate` datetime(0) NOT NULL,
  `count` int(0) UNSIGNED NOT NULL DEFAULT 1,
  `totalPrice` float UNSIGNED NOT NULL,
  PRIMARY KEY (`bookedMeal_ID`) USING BTREE,
  INDEX `fk_BookedMeal_Meal`(`dishesType_ID`) USING BTREE,
  INDEX `fk_BookedMeal_Guest`(`userID`) USING BTREE,
  CONSTRAINT `fk_BookedMeal_Meal` FOREIGN KEY (`dishesType_ID`) REFERENCES `meal` (`dishesType_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_BookedMeal_Guest` FOREIGN KEY (`userID`) REFERENCES `guest` (`userID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bookedmeal
-- ----------------------------

-- ----------------------------
-- Table structure for bookedroom
-- ----------------------------
DROP TABLE IF EXISTS `bookedroom`;
CREATE TABLE `bookedroom`  (
  `bookedRoom_ID` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `userID` int(0) UNSIGNED NOT NULL,
  `roomID` int(0) UNSIGNED NOT NULL,
  `checkInDate` date NOT NULL,
  `checkOutDate` date NOT NULL,
  `operationTime` timestamp(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`bookedRoom_ID`) USING BTREE,
  INDEX `fk_BookedRoom_Guest`(`userID`) USING BTREE,
  INDEX `fk_BookedRoom_Room`(`roomID`) USING BTREE,
  CONSTRAINT `fk_BookedRoom_Guest` FOREIGN KEY (`userID`) REFERENCES `guest` (`userID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_BookedRoom_Room` FOREIGN KEY (`roomID`) REFERENCES `room` (`roomID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bookedroom
-- ----------------------------
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
INSERT INTO `bookedroom` VALUES (16, 6, 913, '2020-05-15', '2020-11-01', '2020-05-15 03:11:56');
INSERT INTO `bookedroom` VALUES (17, 6, 607, '2020-05-16', '2020-12-01', '2020-05-15 03:20:27');
INSERT INTO `bookedroom` VALUES (18, 6, 107, '2020-05-15', '2020-11-30', '2020-05-15 04:03:23');
INSERT INTO `bookedroom` VALUES (19, 6, 310, '2020-05-15', '2020-11-29', '2020-05-15 04:13:43');
INSERT INTO `bookedroom` VALUES (20, 6, 213, '2020-05-17', '2020-05-17', '2020-05-17 20:50:18');
INSERT INTO `bookedroom` VALUES (21, 6, 313, '2020-05-17', '2020-05-17', '2020-05-17 20:50:25');
INSERT INTO `bookedroom` VALUES (22, 6, 413, '2020-05-17', '2020-05-17', '2020-05-17 20:50:32');
INSERT INTO `bookedroom` VALUES (23, 6, 513, '2020-05-17', '2020-05-17', '2020-05-17 20:50:43');
INSERT INTO `bookedroom` VALUES (24, 6, 613, '2020-05-17', '2020-05-17', '2020-05-17 20:50:51');
INSERT INTO `bookedroom` VALUES (25, 6, 713, '2020-05-17', '2020-05-17', '2020-05-17 20:51:01');
INSERT INTO `bookedroom` VALUES (26, 6, 813, '2020-05-17', '2020-05-17', '2020-05-17 20:51:12');
INSERT INTO `bookedroom` VALUES (27, 6, 1013, '2020-05-17', '2020-05-17', '2020-05-17 20:51:15');
INSERT INTO `bookedroom` VALUES (28, 6, 513, '2020-05-18', '2020-05-18', '2020-05-18 00:20:58');

-- ----------------------------
-- Table structure for chef
-- ----------------------------
DROP TABLE IF EXISTS `chef`;
CREATE TABLE `chef`  (
  `chefID` tinyint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `chefName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`chefID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chef
-- ----------------------------
INSERT INTO `chef` VALUES (1, 'Karen Adam');
INSERT INTO `chef` VALUES (2, 'Hari Philip');
INSERT INTO `chef` VALUES (3, 'Thalia Hensley');
INSERT INTO `chef` VALUES (4, 'Nisha Moss');

-- ----------------------------
-- Table structure for guest
-- ----------------------------
DROP TABLE IF EXISTS `guest`;
CREATE TABLE `guest`  (
  `userID` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `realName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `passportID` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `telephoneNumber` bigint(0) UNSIGNED NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
  `operationTime` timestamp(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`userID`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of guest
-- ----------------------------
INSERT INTO `guest` VALUES (2, 'mika', '12345', 'mmm', 'ER102897', 222555666, '2579583@student.xjtlu.edu.cn', '2020-05-04 22:12:48');
INSERT INTO `guest` VALUES (3, 'trigger', '123', 'mike-minjy', 'ER1252363', 18972634381, '26839586@qq.com', '2020-05-05 02:19:46');
INSERT INTO `guest` VALUES (4, 'mikalon', '666', 'mika', 'QS553322', 59283098, '33322_dewd@mail.com', '2020-05-07 21:44:20');
INSERT INTO `guest` VALUES (5, 'ayasaki', '23335', 'miuki', 'NS9365605', 1223366778, NULL, '2020-05-08 22:35:02');
INSERT INTO `guest` VALUES (6, 'timi', '12356', 'smalion', 'EC335678', 123563216, NULL, '2020-05-17 20:49:49');
INSERT INTO `guest` VALUES (7, 'minicap', '333666', 'mike simon', 'WCMM22233', 22222256, '2553755@123.com', '2020-05-11 14:58:55');
INSERT INTO `guest` VALUES (8, 'muniku', '2333666', 'mikunika', 'EC233366', 222369796, '2333@qq.com', '2020-05-13 20:55:34');
INSERT INTO `guest` VALUES (9, 'ssscc', 'w2', 'minitab', 'WEVE332', 12389795, '', '2020-05-11 11:05:49');

-- ----------------------------
-- Table structure for meal
-- ----------------------------
DROP TABLE IF EXISTS `meal`;
CREATE TABLE `meal`  (
  `dishesType_ID` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `chefID` tinyint(0) UNSIGNED NOT NULL,
  `dishes` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` float UNSIGNED NOT NULL,
  PRIMARY KEY (`dishesType_ID`) USING BTREE,
  INDEX `fk_Meal_Chef`(`chefID`) USING BTREE,
  CONSTRAINT `fk_Meal_Chef` FOREIGN KEY (`chefID`) REFERENCES `chef` (`chefID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meal
-- ----------------------------
INSERT INTO `meal` VALUES (1, 1, 'Shrimp soup', 5);
INSERT INTO `meal` VALUES (2, 1, 'Cauliflower and mushroom stew', 3);
INSERT INTO `meal` VALUES (3, 1, 'spicy chicken nuggets', 6);
INSERT INTO `meal` VALUES (4, 1, 'steamed cod fish', 8);
INSERT INTO `meal` VALUES (5, 1, 'turkey burger', 10);
INSERT INTO `meal` VALUES (6, 1, 'veggie burger', 7);
INSERT INTO `meal` VALUES (7, 1, 'fried egg', 2);
INSERT INTO `meal` VALUES (8, 2, 'Chicken curry', 5);
INSERT INTO `meal` VALUES (9, 2, 'Chicken masala', 5);
INSERT INTO `meal` VALUES (10, 2, 'Mutton Korma', 8);
INSERT INTO `meal` VALUES (11, 2, 'Keema Curry', 5);
INSERT INTO `meal` VALUES (12, 2, 'Mushroom Tikka', 6);
INSERT INTO `meal` VALUES (13, 2, 'fried egg', 2);
INSERT INTO `meal` VALUES (14, 2, 'curry rice', 3);
INSERT INTO `meal` VALUES (15, 3, 'Tofu teriyaki', 6);
INSERT INTO `meal` VALUES (16, 3, 'Shrimp Tempura', 7);
INSERT INTO `meal` VALUES (17, 3, 'Yaki Udon', 6);
INSERT INTO `meal` VALUES (18, 3, 'Chicken Katsu', 6);
INSERT INTO `meal` VALUES (19, 3, 'Salmon sashimi', 9);
INSERT INTO `meal` VALUES (20, 3, 'fried egg', 2);
INSERT INTO `meal` VALUES (21, 3, 'curry rice', 3);
INSERT INTO `meal` VALUES (22, 4, 'Black pepper beef', 9);
INSERT INTO `meal` VALUES (23, 4, 'Pork chowmein', 8);
INSERT INTO `meal` VALUES (24, 4, 'Sweet & sour pork', 8);
INSERT INTO `meal` VALUES (25, 4, 'Gongbao chicken', 7);
INSERT INTO `meal` VALUES (26, 4, 'Pork Jiaozi', 8);
INSERT INTO `meal` VALUES (27, 4, 'Soy glazed pork chops', 7);
INSERT INTO `meal` VALUES (28, 4, 'curry rice', 3);

-- ----------------------------
-- Table structure for oneweek
-- ----------------------------
DROP TABLE IF EXISTS `oneweek`;
CREATE TABLE `oneweek`  (
  `day_ID` tinyint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `day_Name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`day_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oneweek
-- ----------------------------
INSERT INTO `oneweek` VALUES (1, 'Monday');
INSERT INTO `oneweek` VALUES (2, 'Tuesday');
INSERT INTO `oneweek` VALUES (3, 'Wednesday');
INSERT INTO `oneweek` VALUES (4, 'Thursday');
INSERT INTO `oneweek` VALUES (5, 'Friday');
INSERT INTO `oneweek` VALUES (6, 'Saturday');
INSERT INTO `oneweek` VALUES (7, 'Sunday');

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `roomID` int(0) UNSIGNED NOT NULL,
  `roomTypeID` tinyint(0) UNSIGNED NOT NULL,
  PRIMARY KEY (`roomID`) USING BTREE,
  INDEX `fk_Room_RoomType`(`roomTypeID`) USING BTREE,
  CONSTRAINT `fk_Room_RoomType` FOREIGN KEY (`roomTypeID`) REFERENCES `roomtype` (`roomTypeID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
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

-- ----------------------------
-- Table structure for roomtype
-- ----------------------------
DROP TABLE IF EXISTS `roomtype`;
CREATE TABLE `roomtype`  (
  `roomTypeID` tinyint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `roomType` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`roomTypeID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roomtype
-- ----------------------------
INSERT INTO `roomtype` VALUES (1, 'Large double bed');
INSERT INTO `roomtype` VALUES (2, 'Large single bed');
INSERT INTO `roomtype` VALUES (3, 'Small single bed');
INSERT INTO `roomtype` VALUES (4, 'VIP Room');

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule`  (
  `weekday_ID` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `chefID` tinyint(0) UNSIGNED NOT NULL,
  `day_ID` tinyint(0) UNSIGNED NOT NULL,
  PRIMARY KEY (`weekday_ID`) USING BTREE,
  INDEX `fk_Schedule_Chef`(`chefID`) USING BTREE,
  INDEX `fk_Schedule_OneWeek`(`day_ID`) USING BTREE,
  CONSTRAINT `fk_Schedule_Chef` FOREIGN KEY (`chefID`) REFERENCES `chef` (`chefID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_Schedule_OneWeek` FOREIGN KEY (`day_ID`) REFERENCES `oneweek` (`day_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule
-- ----------------------------
INSERT INTO `schedule` VALUES (1, 1, 1);
INSERT INTO `schedule` VALUES (2, 1, 2);
INSERT INTO `schedule` VALUES (3, 1, 3);
INSERT INTO `schedule` VALUES (4, 1, 4);
INSERT INTO `schedule` VALUES (5, 1, 5);
INSERT INTO `schedule` VALUES (6, 2, 3);
INSERT INTO `schedule` VALUES (7, 2, 4);
INSERT INTO `schedule` VALUES (8, 2, 5);
INSERT INTO `schedule` VALUES (9, 2, 6);
INSERT INTO `schedule` VALUES (10, 2, 7);
INSERT INTO `schedule` VALUES (11, 3, 1);
INSERT INTO `schedule` VALUES (12, 3, 4);
INSERT INTO `schedule` VALUES (13, 3, 6);
INSERT INTO `schedule` VALUES (14, 4, 2);
INSERT INTO `schedule` VALUES (15, 4, 6);
INSERT INTO `schedule` VALUES (16, 4, 7);

-- ----------------------------
-- Table structure for staff
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff`  (
  `staffID` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `telephoneNumber` bigint(0) UNSIGNED NOT NULL,
  `operationTime` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`staffID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of staff
-- ----------------------------
INSERT INTO `staff` VALUES (1, 'mike', '2333666', 8615833356871, '2020-05-15 02:58:33');
INSERT INTO `staff` VALUES (2, 'tigger', '2333', 987658632, '2020-05-19 20:58:08');
INSERT INTO `staff` VALUES (3, 'scott', '111', 2123535768, '2020-05-09 10:03:31');

SET FOREIGN_KEY_CHECKS = 1;
