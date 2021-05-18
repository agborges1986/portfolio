/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50514
Source Host           : localhost:3306
Source Database       : proximitydb

Target Server Type    : MYSQL
Target Server Version : 50514
File Encoding         : 65001

Date: 2013-04-28 11:48:49
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `categories`
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO categories VALUES ('1', 'cat1', 'cat1');
INSERT INTO categories VALUES ('2', 'cat2', 'cat2');
INSERT INTO categories VALUES ('3', 'cat3', 'cat3');
INSERT INTO categories VALUES ('4', 'cat4', 'cat4');

-- ----------------------------
-- Table structure for `comments`
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `creation_time` bigint(20) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `RATING` tinyint(4) DEFAULT NULL,
  `TEXT` varchar(255) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `place_id` int(11) NOT NULL,
  `author_id` int(11) NOT NULL,
  PRIMARY KEY (`comment_id`),
  UNIQUE KEY `comment_id` (`comment_id`),
  KEY `FK_comments_place_id` (`place_id`),
  KEY `FK_comments_category_id` (`category_id`),
  KEY `FK_comments_author_id` (`author_id`),
  CONSTRAINT `FK_comments_author_id` FOREIGN KEY (`author_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_comments_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`),
  CONSTRAINT `FK_comments_place_id` FOREIGN KEY (`place_id`) REFERENCES `places` (`place_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of comments
-- ----------------------------

-- ----------------------------
-- Table structure for `places`
-- ----------------------------
DROP TABLE IF EXISTS `places`;
CREATE TABLE `places` (
  `place_id` int(11) NOT NULL AUTO_INCREMENT,
  `cant_rating` int(11) DEFAULT NULL,
  `creation_time` bigint(20) NOT NULL,
  `heat_duration` bigint(20) DEFAULT NULL,
  `LATITUDE` double NOT NULL,
  `LONGITUDE` double NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `total_rating` int(11) DEFAULT NULL,
  PRIMARY KEY (`place_id`),
  UNIQUE KEY `place_id` (`place_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of places
-- ----------------------------

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `maximum_distance` int(11) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of users
-- ----------------------------

-- ----------------------------
-- Table structure for `users_categories`
-- ----------------------------
DROP TABLE IF EXISTS `users_categories`;
CREATE TABLE `users_categories` (
  `User_user_id` int(11) NOT NULL,
  `categories_category_id` int(11) NOT NULL,
  PRIMARY KEY (`User_user_id`,`categories_category_id`),
  KEY `FK_users_categories_categories_category_id` (`categories_category_id`),
  CONSTRAINT `FK_users_categories_User_user_id` FOREIGN KEY (`User_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_users_categories_categories_category_id` FOREIGN KEY (`categories_category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of users_categories
-- ----------------------------
