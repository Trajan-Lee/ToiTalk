-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema toitalk
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema toitalk
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `toitalk` DEFAULT CHARACTER SET utf8mb3 ;
USE `toitalk` ;

-- -----------------------------------------------------
-- Table `toitalk`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `toitalk`.`users` ;

CREATE TABLE IF NOT EXISTS `toitalk`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `user_type` ENUM('student', 'tutor') NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `toitalk`.`tutors`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `toitalk`.`tutors` ;

CREATE TABLE IF NOT EXISTS `toitalk`.`tutors` (
  `tutor_id` INT NOT NULL AUTO_INCREMENT,
  `language` VARCHAR(45) NOT NULL,
  `years` INT NULL DEFAULT NULL,
  `bio` VARCHAR(500) NULL DEFAULT NULL,
  `rating` FLOAT NULL DEFAULT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`tutor_id`, `user_id`),
  INDEX `fk_tutors_users_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_tutors_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `toitalk`.`users` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `toitalk`.`students`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `toitalk`.`students` ;

CREATE TABLE IF NOT EXISTS `toitalk`.`students` (
  `student_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`student_id`, `user_id`),
  INDEX `fk_students_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_students_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `toitalk`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `toitalk`.`time_slots`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `toitalk`.`time_slots` ;

CREATE TABLE IF NOT EXISTS `toitalk`.`time_slots` (
  `slot_id` INT NOT NULL AUTO_INCREMENT,
  `day` ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') NOT NULL,
  `hour` INT NOT NULL,
  PRIMARY KEY (`slot_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `toitalk`.`bookings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `toitalk`.`bookings` ;

CREATE TABLE IF NOT EXISTS `toitalk`.`bookings` (
  `booking_id` INT NOT NULL AUTO_INCREMENT,
  `tutor_id` INT NOT NULL,
  `student_id` INT NOT NULL,
  `slot_id` INT NOT NULL,
  `date` DATETIME NULL,
  `status` ENUM('scheduled', 'completed', 'canceled') NOT NULL DEFAULT 'scheduled',
  PRIMARY KEY (`booking_id`),
  INDEX `tutor_id` (`tutor_id` ASC) VISIBLE,
  INDEX `fk_bookings_students1_idx` (`student_id` ASC) VISIBLE,
  INDEX `fk_bookings_time_slots1_idx` (`slot_id` ASC) VISIBLE,
  CONSTRAINT `sessions_ibfk_1`
    FOREIGN KEY (`tutor_id`)
    REFERENCES `toitalk`.`tutors` (`tutor_id`),
  CONSTRAINT `fk_bookings_students1`
    FOREIGN KEY (`student_id`)
    REFERENCES `toitalk`.`students` (`student_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bookings_time_slots1`
    FOREIGN KEY (`slot_id`)
    REFERENCES `toitalk`.`time_slots` (`slot_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `toitalk`.`feedback`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `toitalk`.`feedback` ;

CREATE TABLE IF NOT EXISTS `toitalk`.`feedback` (
  `feedback_id` INT NOT NULL AUTO_INCREMENT,
  `booking_id` INT NOT NULL,
  `rating` INT NOT NULL,
  `comment` TEXT NULL DEFAULT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `tutor_id` INT NOT NULL,
  PRIMARY KEY (`feedback_id`),
  INDEX `session_id` (`booking_id` ASC) VISIBLE,
  INDEX `fk_feedback_tutors1_idx` (`tutor_id` ASC) VISIBLE,
  CONSTRAINT `feedback_ibfk_1`
    FOREIGN KEY (`booking_id`)
    REFERENCES `toitalk`.`bookings` (`booking_id`),
  CONSTRAINT `fk_feedback_tutors1`
    FOREIGN KEY (`tutor_id`)
    REFERENCES `toitalk`.`tutors` (`tutor_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `toitalk`.`languages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `toitalk`.`languages` ;

CREATE TABLE IF NOT EXISTS `toitalk`.`languages` (
  `language_id` INT NOT NULL AUTO_INCREMENT,
  `language_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`language_id`),
  UNIQUE INDEX `language_name` (`language_name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `toitalk`.`tutor_languages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `toitalk`.`tutor_languages` ;

CREATE TABLE IF NOT EXISTS `toitalk`.`tutor_languages` (
  `tutor_id` INT NOT NULL,
  `language_id` INT NOT NULL,
  PRIMARY KEY (`tutor_id`, `language_id`),
  INDEX `language_id` (`language_id` ASC) VISIBLE,
  CONSTRAINT `tutor_languages_ibfk_1`
    FOREIGN KEY (`tutor_id`)
    REFERENCES `toitalk`.`tutors` (`tutor_id`),
  CONSTRAINT `tutor_languages_ibfk_2`
    FOREIGN KEY (`language_id`)
    REFERENCES `toitalk`.`languages` (`language_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `toitalk`.`tutor_schedule`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `toitalk`.`tutor_schedule` ;

CREATE TABLE IF NOT EXISTS `toitalk`.`tutor_schedule` (
  `tutor_schedule_id` INT NOT NULL AUTO_INCREMENT,
  `slot_id` INT NOT NULL,
  `tutor_id` INT NOT NULL,
  `status` ENUM("Booked", "Open") NOT NULL DEFAULT 'Open',
  PRIMARY KEY (`tutor_schedule_id`),
  INDEX `fk_tutor_schedule_time_slots1_idx` (`slot_id` ASC) VISIBLE,
  INDEX `fk_tutor_schedule_tutors1_idx` (`tutor_id` ASC) VISIBLE,
  CONSTRAINT `fk_tutor_schedule_time_slots1`
    FOREIGN KEY (`slot_id`)
    REFERENCES `toitalk`.`time_slots` (`slot_id`),
  CONSTRAINT `fk_tutor_schedule_tutors1`
    FOREIGN KEY (`tutor_id`)
    REFERENCES `toitalk`.`tutors` (`tutor_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
