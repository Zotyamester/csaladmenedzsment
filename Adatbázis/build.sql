CREATE DATABASE `testdb`;

CREATE TABLE `family` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`fname` VARCHAR(64) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `listitem` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`iid` INT(11) NOT NULL,
	`iname` VARCHAR(64) NOT NULL,
	`quantity` FLOAT NOT NULL,
	`unit` VARCHAR(32) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `person` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`email` VARCHAR(64) NOT NULL,
	`uname` VARCHAR(32) NOT NULL,
	`passw` VARCHAR(64) NOT NULL,
	`salt` VARCHAR(8) NOT NULL,
	`firstname` VARCHAR(64) NOT NULL,
	`lastname` VARCHAR(64) NOT NULL,
	`fid` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `email` (`email`),
	UNIQUE INDEX `uname` (`uname`)
);

CREATE TABLE `post` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`iid` INT(11) NOT NULL,
	`title` VARCHAR(64) NOT NULL,
	`body` VARCHAR(512) NOT NULL,
	`cdate` DATETIME NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `task` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`iid` INT(11) NOT NULL,
	`title` VARCHAR(64) NOT NULL,
	`description` VARCHAR(128) NOT NULL,
	`deadline` DATETIME NOT NULL,
	`done` TINYINT(1) NOT NULL,
	PRIMARY KEY (`id`)
);
