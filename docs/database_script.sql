-- MySQL Script generated by MySQL Workbench
-- 02/24/15 13:15:44
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema hermanmk_calDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `User` (
  `userid` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(32) NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userid`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC));


-- -----------------------------------------------------
-- Table `Room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Room` (
  `roomid` INT NOT NULL AUTO_INCREMENT,
  `capacity` INT NOT NULL,
  `room_num` VARCHAR(45) NOT NULL,
  `accessible` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`roomid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `User_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `User_group` (
  `groupid` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `owner_id` INT NOT NULL,
  PRIMARY KEY (`groupid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Meeting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Meeting` (
  `meetingid` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL,
  `description` MEDIUMTEXT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Room_roomid` INT NOT NULL,
  `owner_id` INT NOT NULL,
  `Group_groupid` INT NULL,
  PRIMARY KEY (`meetingid`),
  INDEX `fk_Meeting_Room_idx` (`Room_roomid` ASC),
  INDEX `fk_Meeting_Group1_idx` (`Group_groupid` ASC),
  CONSTRAINT `fk_Meeting_Room`
    FOREIGN KEY (`Room_roomid`)
    REFERENCES `Room` (`roomid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Meeting_Group1`
    FOREIGN KEY (`Group_groupid`)
    REFERENCES `User_group` (`groupid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `User_invited_to_meeting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `User_invited_to_meeting` (
  `User_userid` INT NOT NULL,
  `Meeting_meetingid` INT NOT NULL,
  `notification_message` MEDIUMTEXT NOT NULL,
  `notification_read` TINYINT(1) NOT NULL DEFAULT 0 ON UPDATE 0,
  `confirmed` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`User_userid`, `Meeting_meetingid`),
  INDEX `fk_User_has_Meeting_Meeting1_idx` (`Meeting_meetingid` ASC),
  INDEX `fk_User_has_Meeting_User1_idx` (`User_userid` ASC),
  CONSTRAINT `fk_User_has_Meeting_User1`
    FOREIGN KEY (`User_userid`)
    REFERENCES `User` (`userid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_Meeting_Meeting1`
    FOREIGN KEY (`Meeting_meetingid`)
    REFERENCES `Meeting` (`meetingid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `User_group_has_User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `User_group_has_User` (
  `User_userid` INT NOT NULL,
  `User_group_groupid` INT NOT NULL,
  `notification_message` MEDIUMTEXT NOT NULL,
  `notification_read` TINYINT(1) NOT NULL DEFAULT 0 ON UPDATE 0,
  `confirmed` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`User_userid`, `User_group_groupid`),
  INDEX `fk_User_has_Group_Group1_idx` (`User_group_groupid` ASC),
  INDEX `fk_User_has_Group_User1_idx` (`User_userid` ASC),
  CONSTRAINT `fk_User_has_Group_User1`
    FOREIGN KEY (`User_userid`)
    REFERENCES `User` (`userid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_Group_Group1`
    FOREIGN KEY (`User_group_groupid`)
    REFERENCES `User_group` (`groupid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
