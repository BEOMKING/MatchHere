-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema matching
-- -----------------------------------------------------
-- edited by ilminmoon

-- -----------------------------------------------------
-- Schema matching
--
-- edited by ilminmoon
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `matching` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `matching` ;

-- -----------------------------------------------------
-- Table `matching`.`files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`files` (
  `id` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `data` LONGBLOB NULL DEFAULT NULL,
  `file_name` VARCHAR(255) NULL DEFAULT NULL,
  `file_type` VARCHAR(255) NULL DEFAULT NULL,
  `download_uri` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`member` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `authority` VARCHAR(20) NOT NULL,
  `banned` BIT(1) NULL DEFAULT NULL,
  `bio` VARCHAR(1000) NULL DEFAULT NULL,
  `city` VARCHAR(10) NOT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `is_active` BIT(1) NOT NULL,
  `name` VARCHAR(8) NOT NULL,
  `nickname` VARCHAR(10) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `position` VARCHAR(20) NOT NULL,
  `tel` VARCHAR(255) NULL DEFAULT NULL,
  `cover_pic` VARCHAR(255) NULL DEFAULT NULL,
  `portfolio_uri` VARCHAR(1000) NULL DEFAULT NULL,
  `portfolio_uuid` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_member_files1_idx` (`cover_pic` ASC) VISIBLE,
  INDEX `fk_member_files2_idx` (`portfolio_uuid` ASC) VISIBLE,
  CONSTRAINT `fk_member_files1`
    FOREIGN KEY (`cover_pic`)
    REFERENCES `matching`.`files` (`id`),
  CONSTRAINT `fk_member_files2`
    FOREIGN KEY (`portfolio_uuid`)
    REFERENCES `matching`.`files` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 46
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`club`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`club` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `bio` VARCHAR(1000) NULL DEFAULT NULL,
  `city` VARCHAR(10) NOT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `is_active` BIT(1) NOT NULL,
  `is_public` BIT(1) NOT NULL,
  `max_count` INT NOT NULL,
  `member_count` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `topic` VARCHAR(45) NOT NULL,
  `cover_pic` VARCHAR(255) NULL DEFAULT NULL,
  `host_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_club_files1_idx` (`cover_pic` ASC) VISIBLE,
  INDEX `fk_club_member1_idx` (`host_id` ASC) VISIBLE,
  CONSTRAINT `fk_club_files1`
    FOREIGN KEY (`cover_pic`)
    REFERENCES `matching`.`files` (`id`),
  CONSTRAINT `fk_club_member1`
    FOREIGN KEY (`host_id`)
    REFERENCES `matching`.`member` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`club_board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`club_board` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `club_id` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_club_board_club1_idx` (`club_id` ASC) VISIBLE,
  CONSTRAINT `fk_club_board_club1`
    FOREIGN KEY (`club_id`)
    REFERENCES `matching`.`club` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`career`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`career` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `company` VARCHAR(255) NOT NULL,
  `role` VARCHAR(50) NOT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `end_date` DATETIME(6) NULL DEFAULT NULL,
  `start_date` DATETIME(6) NOT NULL,
  `member_id` BIGINT NOT NULL,
  `is_incumbent` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKj3sr8mqtm4j9hh3cdk9514iuy` (`member_id` ASC) VISIBLE,
  CONSTRAINT `FKj3sr8mqtm4j9hh3cdk9514iuy`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`certification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`certification` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  `expired_date` DATETIME(6) NULL DEFAULT NULL,
  `grade` VARCHAR(255) NULL DEFAULT NULL,
  `issued_date` DATETIME(6) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `organization` VARCHAR(255) NOT NULL,
  `member_id` BIGINT NOT NULL,
  `is_expire` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKean8y31fu1keq8505jsdy4ts7` (`member_id` ASC) VISIBLE,
  CONSTRAINT `FKean8y31fu1keq8505jsdy4ts7`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`club_application_form`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`club_application_form` (
  `nickname` VARCHAR(10) NOT NULL,
  `city` VARCHAR(10) NOT NULL,
  `git` VARCHAR(145) NULL DEFAULT NULL,
  `twitter` VARCHAR(145) NULL DEFAULT NULL,
  `facebook` VARCHAR(145) NULL DEFAULT NULL,
  `backjoon` VARCHAR(145) NULL DEFAULT NULL,
  `bio` VARCHAR(145) NULL DEFAULT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `cover_pic` VARCHAR(255) NULL DEFAULT NULL,
  `club_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  PRIMARY KEY (`club_id`, `member_id`),
  INDEX `fk_club_application_form_files1_idx` (`cover_pic` ASC) VISIBLE,
  INDEX `fk_club_application_form_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_club_application_form_club1`
    FOREIGN KEY (`club_id`)
    REFERENCES `matching`.`club` (`id`),
  CONSTRAINT `fk_club_application_form_files1`
    FOREIGN KEY (`cover_pic`)
    REFERENCES `matching`.`files` (`id`),
  CONSTRAINT `fk_club_application_form_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`education`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`education` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `degree` VARCHAR(255) NOT NULL,
  `end_date` INT NULL DEFAULT NULL,
  `institution` VARCHAR(255) NOT NULL,
  `major` VARCHAR(255) NULL DEFAULT NULL,
  `start_date` INT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `state` VARCHAR(30) NOT NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `FKcwpq7xrd9cidl8hxgepm1cju8` (`member_id` ASC) VISIBLE,
  CONSTRAINT `FKcwpq7xrd9cidl8hxgepm1cju8`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`member_club`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`member_club` (
  `is_active` BIT(1) NOT NULL,
  `register_date` DATETIME(6) NULL DEFAULT NULL,
  `member_id` BIGINT NOT NULL,
  `club_id` BIGINT NOT NULL,
  `authority` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`member_id`, `club_id`),
  INDEX `fk_member_club_club1_idx` (`club_id` ASC) VISIBLE,
  CONSTRAINT `fk_member_club_club1`
    FOREIGN KEY (`club_id`)
    REFERENCES `matching`.`club` (`id`),
  CONSTRAINT `fk_member_club_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`project` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `cover_pic` VARCHAR(255) NULL DEFAULT NULL,
  `project_progress_state` VARCHAR(45) NOT NULL,
  `public_scope` VARCHAR(45) NOT NULL,
  `recruitment_state` VARCHAR(45) NOT NULL,
  `view_count` INT NOT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `modify_date` DATETIME(6) NOT NULL,
  `host_id` BIGINT NOT NULL,
  `schedule` VARCHAR(45) NULL DEFAULT NULL,
  `period` DATE NULL DEFAULT NULL,
  `designer_count` INT NOT NULL,
  `designer_max_count` INT NOT NULL,
  `apply_designer` BIT(1) NOT NULL,
  `developer_count` INT NOT NULL,
  `developer_max_count` INT NOT NULL,
  `apply_developer` BIT(1) NOT NULL,
  `planner_count` INT NOT NULL,
  `planner_max_count` INT NOT NULL,
  `apply_planner` BIT(1) NOT NULL,
  `city` VARCHAR(10) NOT NULL,
  `club_id` BIGINT NULL DEFAULT NULL,
  `bio` VARCHAR(1000) NULL DEFAULT NULL,
  `is_active` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_project_files1_idx` (`cover_pic` ASC) VISIBLE,
  INDEX `fk_project_club1_idx` (`club_id` ASC) VISIBLE,
  INDEX `fk_project_member1_idx` (`host_id` ASC) VISIBLE,
  CONSTRAINT `fk_project_club1`
    FOREIGN KEY (`club_id`)
    REFERENCES `matching`.`club` (`id`),
  CONSTRAINT `fk_project_files1`
    FOREIGN KEY (`cover_pic`)
    REFERENCES `matching`.`files` (`id`),
  CONSTRAINT `fk_project_member1`
    FOREIGN KEY (`host_id`)
    REFERENCES `matching`.`member` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci
INSERT_METHOD = LAST;


-- -----------------------------------------------------
-- Table `matching`.`member_project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`member_project` (
  `project_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `role` VARCHAR(20) NOT NULL,
  `register_date` DATETIME(6) NOT NULL,
  `authority` VARCHAR(45) NOT NULL,
  `is_active` BIT(1) NOT NULL,
  PRIMARY KEY (`project_id`, `member_id`),
  INDEX `fk_member_project_project1_idx` (`project_id` ASC) VISIBLE,
  CONSTRAINT `fk_member_project_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`),
  CONSTRAINT `fk_member_project_project1`
    FOREIGN KEY (`project_id`)
    REFERENCES `matching`.`project` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`member_sns`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`member_sns` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `member_id` BIGINT NOT NULL,
  `sns_account` VARCHAR(255) NOT NULL,
  `sns_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_member_sns_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_member_sns_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`study`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`study` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `cover_pic` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  `study_progress_state` VARCHAR(45) NOT NULL,
  `public_scope` VARCHAR(45) NOT NULL,
  `recruitment_state` VARCHAR(45) NOT NULL,
  `view_count` INT NOT NULL,
  `created_date` DATETIME(6) NOT NULL,
  `modified_date` DATETIME(6) NOT NULL,
  `host_id` BIGINT NOT NULL,
  `schedule` VARCHAR(45) NULL DEFAULT NULL,
  `member_count` INT NOT NULL,
  `max_count` INT NOT NULL,
  `city` VARCHAR(10) NOT NULL,
  `club_id` BIGINT NULL DEFAULT NULL,
  `bio` VARCHAR(1000) NULL DEFAULT NULL,
  `is_active` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_study_member1_idx` (`host_id` ASC) VISIBLE,
  INDEX `fk_study_club1_idx` (`club_id` ASC) VISIBLE,
  INDEX `fk_study_files1_idx` (`cover_pic` ASC) VISIBLE,
  CONSTRAINT `fk_study_club1`
    FOREIGN KEY (`club_id`)
    REFERENCES `matching`.`club` (`id`),
  CONSTRAINT `fk_study_member1`
    FOREIGN KEY (`host_id`)
    REFERENCES `matching`.`member` (`id`),
  CONSTRAINT `fk_study_files1`
    FOREIGN KEY (`cover_pic`)
    REFERENCES `matching`.`files` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`member_study`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`member_study` (
  `study_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `register_date` DATETIME(6) NOT NULL,
  `authority` VARCHAR(45) NOT NULL,
  `is_active` BIT(1) NOT NULL,
  PRIMARY KEY (`study_id`, `member_id`),
  INDEX `fk_member_study_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_member_study_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`),
  CONSTRAINT `fk_member_study_study1`
    FOREIGN KEY (`study_id`)
    REFERENCES `matching`.`study` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`techstack`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`techstack` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `img_uri` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`member_techstack`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`member_techstack` (
  `member_id` BIGINT NOT NULL,
  `techstack_id` INT NOT NULL,
  `level` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`member_id`, `techstack_id`),
  INDEX `FKt9pc24oxcu8o9129k92jt7qg7` (`techstack_id` ASC) VISIBLE,
  CONSTRAINT `FK4e6643xhjlp6k401w5aac3itk`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`),
  CONSTRAINT `FKt9pc24oxcu8o9129k92jt7qg7`
    FOREIGN KEY (`techstack_id`)
    REFERENCES `matching`.`techstack` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`chat_message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`chat_message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sender_id` BIGINT NOT NULL,
  `sent_time` DATETIME(6) NOT NULL,
  `content` TEXT NULL,
  `is_read` BIT(1) NULL,
  `file_id` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
  `chat_room_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_message_member1_idx` (`sender_id` ASC) VISIBLE,
  INDEX `fk_chat_message_files2_idx` (`file_id` ASC) VISIBLE,
  CONSTRAINT `fk_message_member1`
    FOREIGN KEY (`sender_id`)
    REFERENCES `matching`.`member` (`id`),
  CONSTRAINT `fk_chat_message_files2`
    FOREIGN KEY (`file_id`)
    REFERENCES `matching`.`files` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`project_application_form`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`project_application_form` (
  `project_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `name` VARCHAR(30) NOT NULL,
  `role` VARCHAR(20) NOT NULL,
  `bio` VARCHAR(145) NULL DEFAULT NULL,
  `create_date` DATETIME(6) NOT NULL,
  PRIMARY KEY (`project_id`, `member_id`),
  INDEX `fk_project_application_form_project1_idx` (`project_id` ASC) VISIBLE,
  INDEX `fk_project_application_form_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_project_application_form_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`),
  CONSTRAINT `fk_project_application_form_project1`
    FOREIGN KEY (`project_id`)
    REFERENCES `matching`.`project` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`project_techstack`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`project_techstack` (
  `project_id` BIGINT NOT NULL,
  `techstack_id` INT NOT NULL,
  `level` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`project_id`, `techstack_id`),
  INDEX `fk_project_techstack_project1_idx` (`project_id` ASC) VISIBLE,
  INDEX `fk_project_techstack_techstack1_idx` (`techstack_id` ASC) VISIBLE,
  CONSTRAINT `fk_project_techstack_project1`
    FOREIGN KEY (`project_id`)
    REFERENCES `matching`.`project` (`id`),
  CONSTRAINT `fk_project_techstack_techstack1`
    FOREIGN KEY (`techstack_id`)
    REFERENCES `matching`.`techstack` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`refresh_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`refresh_token` (
  `member_id` VARCHAR(255) NOT NULL,
  `value` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`member_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`study_application_form`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`study_application_form` (
  `study_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `name` VARCHAR(30) NOT NULL,
  `bio` VARCHAR(145) NULL DEFAULT NULL,
  `create_date` DATETIME(6) NOT NULL,
  PRIMARY KEY (`study_id`, `member_id`),
  INDEX `fk_study_application_form_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_study_application_form_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`),
  CONSTRAINT `fk_study_application_form_study1`
    FOREIGN KEY (`study_id`)
    REFERENCES `matching`.`study` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `matching`.`club_article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`club_article` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `club_board_id` INT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `modified_date` DATETIME(6) NULL,
  `view_count` INT NULL,
  INDEX `fk_club_article_club_board1_idx` (`club_board_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  INDEX `fk_club_article_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_club_article_club_board1`
    FOREIGN KEY (`club_board_id`)
    REFERENCES `matching`.`club_board` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_club_article_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`club_content`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`club_content` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `club_article_id` BIGINT NOT NULL,
  `content` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_club_content_club_article1_idx` (`club_article_id` ASC) VISIBLE,
  CONSTRAINT `fk_club_content_club_article1`
    FOREIGN KEY (`club_article_id`)
    REFERENCES `matching`.`club_article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`club_article_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`club_article_tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `club_article_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_club_article_tag_club_article1_idx` (`club_article_id` ASC) VISIBLE,
  CONSTRAINT `fk_club_article_tag_club_article1`
    FOREIGN KEY (`club_article_id`)
    REFERENCES `matching`.`club_article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`club_article_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`club_article_comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(500) NOT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `modified_date` DATETIME(6) NULL,
  `member_id` BIGINT NOT NULL,
  `club_article_id` BIGINT NOT NULL,
  `depth` INT NOT NULL,
  `parent_id` BIGINT NULL,
  `is_modified` BIT(1) NOT NULL,
  `is_deleted` BIT(1) NOT NULL,
  `reply_count` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_club_article_comment_member1_idx` (`member_id` ASC) VISIBLE,
  INDEX `fk_club_article_comment_club_article1_idx` (`club_article_id` ASC) VISIBLE,
  CONSTRAINT `fk_club_article_comment_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_club_article_comment_club_article1`
    FOREIGN KEY (`club_article_id`)
    REFERENCES `matching`.`club_article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`study_board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`study_board` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `study_id` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  INDEX `fk_study_board_study1_idx` (`study_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_study_board_study1`
    FOREIGN KEY (`study_id`)
    REFERENCES `matching`.`study` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`study_article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`study_article` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `study_board_id` INT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `modified_date` DATETIME(6) NULL,
  `view_count` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_study_article_study_board1_idx` (`study_board_id` ASC) VISIBLE,
  INDEX `fk_study_article_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_study_article_study_board1`
    FOREIGN KEY (`study_board_id`)
    REFERENCES `matching`.`study_board` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_article_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`study_article_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`study_article_tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `study_article_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_study_article_tag_study_article1_idx` (`study_article_id` ASC) VISIBLE,
  CONSTRAINT `fk_study_article_tag_study_article1`
    FOREIGN KEY (`study_article_id`)
    REFERENCES `matching`.`study_article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`study_content`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`study_content` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `study_article_id` BIGINT NOT NULL,
  `content` MEDIUMTEXT NOT NULL,
  INDEX `fk_study_content_study_article1_idx` (`study_article_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_study_content_study_article1`
    FOREIGN KEY (`study_article_id`)
    REFERENCES `matching`.`study_article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`study_article_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`study_article_comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(500) NOT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `modified_date` DATETIME(6) NULL,
  `depth` INT NOT NULL,
  `parent_id` BIGINT NULL,
  `is_modified` BIT(1) NOT NULL,
  `is_deleted` BIT(1) NOT NULL,
  `study_article_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `reply_count` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_study_article_comment_study_article1_idx` (`study_article_id` ASC) VISIBLE,
  INDEX `fk_study_article_comment_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_study_article_comment_study_article1`
    FOREIGN KEY (`study_article_id`)
    REFERENCES `matching`.`study_article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_article_comment_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`project_board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`project_board` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `project_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_project_board_project1_idx` (`project_id` ASC) VISIBLE,
  CONSTRAINT `fk_project_board_project1`
    FOREIGN KEY (`project_id`)
    REFERENCES `matching`.`project` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`project_article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`project_article` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `member_id` BIGINT NOT NULL,
  `project_board_id` INT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `modified_date` DATETIME(6) NULL,
  `view_count` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_project_article_member1_idx` (`member_id` ASC) VISIBLE,
  INDEX `fk_project_article_project_board1_idx` (`project_board_id` ASC) VISIBLE,
  CONSTRAINT `fk_project_article_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_project_article_project_board1`
    FOREIGN KEY (`project_board_id`)
    REFERENCES `matching`.`project_board` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`project_content`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`project_content` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `project_article_id` BIGINT NOT NULL,
  `content` MEDIUMTEXT NOT NULL,
  INDEX `fk_project_content_project_article1_idx` (`project_article_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_project_content_project_article1`
    FOREIGN KEY (`project_article_id`)
    REFERENCES `matching`.`project_article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`project_article_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`project_article_comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(500) NOT NULL,
  `create_date` DATETIME(6) NOT NULL,
  `modified_date` DATETIME(6) NULL,
  `depth` INT NOT NULL,
  `parent_id` BIGINT NULL,
  `is_modified` BIT(1) NOT NULL,
  `is_deleted` BIT(1) NOT NULL,
  `project_article_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `reply_count` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_project_article_comment_project_article1_idx` (`project_article_id` ASC) VISIBLE,
  INDEX `fk_project_article_comment_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_project_article_comment_project_article1`
    FOREIGN KEY (`project_article_id`)
    REFERENCES `matching`.`project_article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_project_article_comment_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`member_portfolio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`member_portfolio` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `member_id` BIGINT NOT NULL,
  `portfolio_uuid` VARCHAR(255) NOT NULL,
  INDEX `fk_member_portfolio_member1_idx` (`member_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_member_portfolio_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`project_article_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`project_article_tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `project_article_id` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tag_project_article1_idx` (`project_article_id` ASC) VISIBLE,
  CONSTRAINT `fk_tag_project_article1`
    FOREIGN KEY (`project_article_id`)
    REFERENCES `matching`.`project_article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`study_topic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`study_topic` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `study_id` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  INDEX `fk_study_subject_study1_idx` (`study_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_study_subject_study1`
    FOREIGN KEY (`study_id`)
    REFERENCES `matching`.`study` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`detail_position`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`detail_position` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `member_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_detail_position_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_detail_position_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`emailcheck`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`emailcheck` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `auth_code` VARCHAR(45) NOT NULL,
  `is_check` BIT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matching`.`chat_room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matching`.`chat_room` (
  `id` VARCHAR(255) NOT NULL,
  `user_id` BIGINT NOT NULL,
  `other_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_chat_room_member1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_chat_room_member2_idx` (`other_id` ASC) VISIBLE,
  CONSTRAINT `fk_chat_room_member1`
    FOREIGN KEY (`user_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_chat_room_member2`
    FOREIGN KEY (`other_id`)
    REFERENCES `matching`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (1, ".net");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (2, "2d rendering");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (3, "3d rendering");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (4, "3d volume rendering");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (5, "abap");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (6, "activemq");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (7, "adobe dreamweaver");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (8, "adobe illustrator");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (9, "adobe lightroom");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (10, "adobe photoshop");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (11, "adobe xd");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (12, "aerospike");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (13, "ajax");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (14, "akka");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (15, "alpine linux");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (16, "amazon api gateway");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (17, "amazon athena");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (18, "amazon aurora");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (19, "amazon chime");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (20, "amazon cloudfront");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (21, "amazon cloudwatch");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (22, "amazon codeguru");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (23, "amazon cognito");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (24, "amazon comprehend");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (25, "amazon connect");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (26, "amazon dynamodb");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (27, "amazon ebs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (28, "amazon ec2");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (29, "amazon ec2 container service");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (30, "amazon ecr");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (31, "amazon eks");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (32, "amazon elastic inference");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (33, "amazon elastic transcoder");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (34, "amazon elasticache");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (35, "amazon elasticsearch service");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (36, "amazon emr");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (37, "amazon honeycode");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (38, "amazon kendra");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (39, "amazon kinesis");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (40, "amazon kinesis firehose");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (41, "amazon lex");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (42, "amazon lightsail");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (43, "amazon linux");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (44, "amazon machine learning");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (45, "amazon mq");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (46, "amazon pinpoint");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (47, "amazon polly");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (48, "amazon rds");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (49, "amazon rds for postgresql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (50, "amazon redshift");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (51, "amazon redshift spectrum");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (52, "amazon rekognition");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (53, "amazon route 53");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (54, "amazon s3");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (55, "amazon sagemaker");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (56, "amazon ses");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (57, "amazon simpledb");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (58, "amazon sns");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (59, "amazon sqs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (60, "amazon sumerian");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (61, "amazon vpc");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (62, "amazon workspaces");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (63, "anaconda");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (64, "analog");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (65, "android os");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (66, "android sdk");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (67, "android studio");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (68, "angular 2");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (69, "angular cli");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (70, "angularjs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (71, "animate.css");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (72, "ansible");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (73, "apache aurora");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (74, "apache cloudstack");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (75, "apache cordova");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (76, "apache cxf");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (77, "apache flink");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (78, "apache freemarker");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (79, "apache http server");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (80, "apache impala");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (81, "apache jmeter");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (82, "apache kylin");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (83, "apache maven");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (84, "apache mesos");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (85, "apache spark");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (86, "apache storm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (87, "apache tomcat");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (88, "apollo");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (89, "ar");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (90, "arduino");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (91, "argo");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (92, "asana");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (93, "asp.net");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (94, "asp.net core");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (95, "atom");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (96, "autocad");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (97, "aws");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (98, "aws amplify");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (99, "aws app mesh");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (100, "aws appsync");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (101, "aws certificate manager");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (102, "aws cloud development kit");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (103, "aws cloudformation");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (104, "aws cloudhsm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (105, "aws cloudtrail");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (106, "aws codebuild");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (107, "aws codecommit");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (108, "aws codedeploy");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (109, "aws codepipeline");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (110, "aws codestar");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (111, "aws copilot");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (112, "aws data pipeline");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (113, "aws data wrangler");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (114, "aws deepracer");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (115, "aws device farm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (116, "aws elastic beanstalk");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (117, "aws elastic load balancing");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (118, "aws elemental mediaconvert");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (119, "aws elemental medialive");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (120, "aws elemental mediatailor");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (121, "aws firecracker");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (122, "aws glue");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (123, "aws greengrass");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (124, "aws iam");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (125, "aws import/export");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (126, "aws iot device management");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (127, "aws lambda");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (128, "aws mobile hub");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (129, "aws outposts");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (130, "aws secrets manager");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (131, "aws service catalog");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (132, "aws shell");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (133, "aws shield");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (134, "aws snowball edge");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (135, "aws step functions");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (136, "aws transfer for sftp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (137, "aws waf");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (138, "aws x-ray");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (139, "axios");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (140, "axure");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (141, "azure");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (142, "azure app service");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (143, "azure application insights");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (144, "azure boards");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (145, "azure bot service");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (146, "azure cdn");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (147, "azure container instances");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (148, "azure container service");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (149, "azure cosmos db");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (150, "azure data factory");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (151, "azure database for mysql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (152, "azure database for postgresql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (153, "azure devops");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (154, "azure functions");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (155, "azure hdinsight");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (156, "azure iot hub");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (157, "azure key vault");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (158, "azure kubernetes service");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (159, "azure machine learning");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (160, "azure pipelines");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (161, "azure resource manager");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (162, "azure search");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (163, "azure security center");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (164, "azure service bus");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (165, "azure service fabric");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (166, "azure stack");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (167, "azure storage");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (168, "azure synapse");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (169, "azure virtual machines");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (170, "azure websites");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (171, "babel");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (172, "babylonjs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (173, "backbone.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (174, "bamboo");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (175, "beautifulsoup");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (176, "bigdata");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (177, "bintray");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (178, "bitbucket");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (179, "bitbucket pipelines");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (180, "blockchain");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (181, "boot2docker");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (182, "bootstrap");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (183, "bourbon");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (184, "bower");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (185, "browserify");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (186, "browserstack");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (187, "bulma");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (188, "c");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (189, "c#");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (190, "c++");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (191, "cakephp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (192, "capistrano");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (193, "casperjs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (194, "cassandra");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (195, "ccie");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (196, "ccna");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (197, "ccnp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (198, "cdnetworks");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (199, "centos");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (200, "chart.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (201, "chef");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (202, "circleci");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (203, "circuit design");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (204, "cisa");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (205, "cisco");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (206, "cissp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (207, "classic asp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (208, "clion");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (209, "clojure");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (210, "clojurescript");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (211, "cloudflare");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (212, "cloudflare cdn");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (213, "cobol");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (214, "cocoa touch (ios)");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (215, "cocos2d-x");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (216, "codeigniter");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (217, "codemirror");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (218, "codeship");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (219, "codestream");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (220, "coffeescript");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (221, "communication");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (222, "confluence");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (223, "coreos");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (224, "couchdb");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (225, "cppg");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (226, "crashlytics");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (227, "create react app");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (228, "create react native app");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (229, "css 3");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (230, "cuda");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (231, "cygwin");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (232, "cypress");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (233, "d3.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (234, "dapper");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (235, "dart");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (236, "datadog");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (237, "datagrip");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (238, "datatables");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (239, "dbeaver");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (240, "dbunit");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (241, "ddos");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (242, "debian");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (243, "deeplearning");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (244, "deno");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (245, "dialogflow");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (246, "dicom");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (247, "digitalocean");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (248, "directx");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (249, "discord");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (250, "django");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (251, "dm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (252, "docker");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (253, "docker cloud");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (254, "docker compose");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (255, "docker datacenter");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (256, "druid");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (257, "drupal");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (258, "dw");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (259, "eclipse");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (260, "elasticsearch");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (261, "electron");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (262, "elixir");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (263, "elk");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (264, "emacs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (265, "embedded");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (266, "ember.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (267, "emotion");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (268, "entity framework");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (269, "enzyme");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (270, "eos");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (271, "erlang");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (272, "erp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (273, "es6");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (274, "eslint");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (275, "etl");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (276, "eureka");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (277, "expo");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (278, "expressjs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (279, "fabric");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (280, "facebook ads");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (281, "facebook workplace");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (282, "fastlane");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (283, "fedora");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (284, "fiddler");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (285, "figma");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (286, "filebeat");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (287, "firebase");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (288, "firewall");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (289, "flask");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (290, "fluentd");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (291, "flutter");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (292, "flux");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (293, "flyway");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (294, "forever");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (295, "foundation");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (296, "fpga");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (297, "freeipa");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (298, "fw");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (299, "g suite");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (300, "gatsby");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (301, "gcp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (302, "gearman");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (303, "gerrit code review");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (304, "git");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (305, "git flow");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (306, "gitea");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (307, "github");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (308, "github actions");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (309, "github enterprise");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (310, "github pages");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (311, "gitkraken");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (312, "gitlab");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (313, "gitlab ci");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (314, "gitter");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (315, "go");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (316, "goland");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (317, "google ads");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (318, "google adsense");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (319, "google ai platform");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (320, "google analytics");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (321, "google anthos");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (322, "google app engine");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (323, "google app maker");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (324, "google automl tables");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (325, "google bigquery");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (326, "google bigquery data transfer service");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (327, "google cloud bigtable");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (328, "google cloud cdn");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (329, "google cloud code");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (330, "google cloud dataflow");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (331, "google cloud datastore");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (332, "google cloud functions");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (333, "google cloud iot core");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (334, "google cloud load balancing");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (335, "google cloud messaging");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (336, "google cloud natural language");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (337, "google cloud platform");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (338, "google cloud pub/sub");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (339, "google cloud run");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (340, "google cloud speech api");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (341, "google cloud sql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (342, "google cloud storage");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (343, "google compute engine");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (344, "google docs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (345, "google drive");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (346, "google hangouts");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (347, "google hangouts chat");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (348, "google hire");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (349, "google kubernetes engine");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (350, "google maps");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (351, "google meet");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (352, "google recaptcha");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (353, "google sheets");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (354, "google tag manager");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (355, "google webmasters");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (356, "gorm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (357, "gradle");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (358, "grafana");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (359, "graphql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (360, "graphql ruby");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (361, "graphql.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (362, "groovy");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (363, "grpc");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (364, "grunt");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (365, "gui");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (366, "gulp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (367, "h2 database");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (368, "hack");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (369, "hadoop");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (370, "hammer.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (371, "handlebars.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (372, "haproxy");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (373, "haskell");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (374, "hasura");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (375, "hazelcast");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (376, "hbase");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (377, "helm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (378, "heroku");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (379, "hexo");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (380, "hibernate");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (381, "highcharts");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (382, "hipchat");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (383, "hive");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (384, "html5");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (385, "hugo");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (386, "hw");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (387, "hyperledger fabric");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (388, "hyperledger indy");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (389, "ibatis");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (390, "ibm containers");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (391, "ibm watson");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (392, "immutable.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (393, "influxdb");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (394, "insomnia rest client");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (395, "intellij idea");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (396, "ionic");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (397, "ionic react");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (398, "ios");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (399, "ips");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (400, "ironmq");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (401, "isms");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (402, "istio");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (403, "jandi");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (404, "jasmine");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (405, "java");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (406, "javascript");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (407, "jekyll");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (408, "jenkins");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (409, "jest");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (410, "jetbrains rider");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (411, "jetbrains space");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (412, "jetty");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (413, "jira");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (414, "joomla!");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (415, "jquery");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (416, "jquery ui");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (417, "jsdelivr");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (418, "json");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (419, "json web token");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (420, "jsonapi");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (421, "jsp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (422, "jstl");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (423, "jsx");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (424, "junit");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (425, "jupyter");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (426, "k8s");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (427, "kafka");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (428, "kafka manager");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (429, "kafka streams");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (430, "kafkacenter");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (431, "kafkahq");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (432, "karma");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (433, "keras");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (434, "kibana");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (435, "koa");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (436, "kong");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (437, "kotlin");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (438, "kubernetes");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (439, "kudu");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (440, "kue.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (441, "l2");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (442, "l3");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (443, "l4");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (444, "l7");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (445, "laravel");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (446, "laravel elixir");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (447, "laravel nova");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (448, "ldap");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (449, "less");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (450, "linode");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (451, "linux");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (452, "litespeed");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (453, "livescript");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (454, "lodash");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (455, "log4j");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (456, "logback");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (457, "logstash");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (458, "lua");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (459, "lucee");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (460, "lucene");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (461, "lumen");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (462, "mac os x");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (463, "machinelearning");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (464, "macos");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (465, "magento");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (466, "mariadb");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (467, "markdown");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (468, "material design");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (469, "material-ui");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (470, "matlab");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (471, "mean");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (472, "memcached");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (473, "mes");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (474, "meteor");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (475, "mfc");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (476, "microelectronics");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (477, "microsoft azure");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (478, "microsoft excel");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (479, "microsoft iis");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (480, "microsoft office 365");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (481, "microsoft sharepoint");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (482, "microsoft sql server");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (483, "microsoft teams");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (484, "mocha");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (485, "mockito");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (486, "moment.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (487, "monaco editor");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (488, "mongodb");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (489, "mongoose");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (490, "mqtt");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (491, "msa");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (492, "mssql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (493, "mustache");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (494, "mybatis");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (495, "mysql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (496, "mysql workbench");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (497, "nativescript");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (498, "neo4j");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (499, "nestjs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (500, "netbeans ide");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (501, "netflix oss");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (502, "netlify");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (503, "netty");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (504, "network");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (505, "new relic");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (506, "next.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (507, "nft");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (508, "nginx");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (509, "nightwatchjs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (510, "nlp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (511, "node-sass");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (512, "node.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (513, "nomad");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (514, "nosql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (515, "notepad++");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (516, "notion.so");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (517, "npl");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (518, "numpy");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (519, "nunit");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (520, "nuxt.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (521, "oauth2");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (522, "objective-c");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (523, "okhttp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (524, "olap");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (525, "opencv");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (526, "opengl");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (527, "openmp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (528, "openssl");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (529, "openvpn");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (530, "oracle");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (531, "orcad");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (532, "pads");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (533, "pandas");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (534, "passenger");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (535, "pcb");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (536, "perfect");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (537, "perforce");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (538, "perl");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (539, "phalcon");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (540, "phoenix framework");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (541, "phonegap");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (542, "php");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (543, "phpstorm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (544, "pig");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (545, "pip");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (546, "playwright");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (547, "pm2");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (548, "pmd");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (549, "portainer");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (550, "postcss");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (551, "postfix");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (552, "postgis");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (553, "postgresql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (554, "postman");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (555, "powershell");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (556, "prettier");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (557, "prisma");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (558, "pro-e");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (559, "prometheus");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (560, "proto.io");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (561, "protopie");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (562, "puma");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (563, "puppeteer");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (564, "purifycss");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (565, "pusher");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (566, "pycharm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (567, "pyqt");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (568, "pytest");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (569, "python");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (570, "pytorch");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (571, "qa");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (572, "qt");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (573, "querydsl");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (574, "r");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (575, "r language");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (576, "rabbitmq");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (577, "racket");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (578, "rails");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (579, "rails spring");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (580, "ramda");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (581, "rancher");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (582, "raphael");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (583, "rdb");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (584, "react");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (585, "react native");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (586, "react redux");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (587, "react router");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (588, "react storybook");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (589, "react.js boilerplate");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (590, "reactivex");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (591, "reactphp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (592, "realm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (593, "reasonml");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (594, "red hat enterprise linux");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (595, "red hat openshift");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (596, "redis");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (597, "redmine");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (598, "redux");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (599, "redux-saga");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (600, "requirejs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (601, "rest api");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (602, "rf");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (603, "rhcsa");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (604, "rocketchat");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (605, "rollup");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (606, "router");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (607, "rpa");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (608, "rstudio");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (609, "ruby");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (610, "rubymine");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (611, "rust");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (612, "rxjs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (613, "rxswift");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (614, "sails.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (615, "sass");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (616, "scala");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (617, "scala native");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (618, "scikit-learn");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (619, "scrapy");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (620, "scss");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (621, "segment");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (622, "select2");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (623, "selenium");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (624, "semantic ui");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (625, "semantic ui react");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (626, "sentry");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (627, "sequel pro");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (628, "sequelize");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (629, "shopify");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (630, "sketch");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (631, "sketchup");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (632, "skype");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (633, "slack");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (634, "slf4j");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (635, "slick");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (636, "slim");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (637, "smalltalk");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (638, "smarty");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (639, "smps");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (640, "socket.io");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (641, "solidity");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (642, "solidworks");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (643, "solr");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (644, "sonarqube");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (645, "sonatype nexus");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (646, "sourcetree");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (647, "spark");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (648, "sphinx");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (649, "splunk");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (650, "spring");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (651, "spring batch");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (652, "spring boot");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (653, "spring cloud");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (654, "spring data");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (655, "spring data jpa");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (656, "spring framework");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (657, "spring mvc");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (658, "spring security");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (659, "spring tools 4");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (660, "sql");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (661, "sqlalchemy");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (662, "sqlite");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (663, "squid");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (664, "stackdriver");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (665, "storybook");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (666, "stylelint");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (667, "sublime text");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (668, "svelte");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (669, "sw");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (670, "swagger codegen");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (671, "swagger ui");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (672, "swift");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (673, "swiftlint");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (674, "swiftui");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (675, "switch");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (676, "swoole");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (677, "symfony");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (678, "tcp/ip");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (679, "teamcity");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (680, "telegram");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (681, "tensorflow");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (682, "tensorflow.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (683, "terraform");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (684, "testflight");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (685, "three.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (686, "thymeleaf");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (687, "tornado");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (688, "tortoisegit");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (689, "tortoisesvn");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (690, "travis ci");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (691, "trello");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (692, "tslint");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (693, "twitter ads");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (694, "typescript");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (695, "ubuntu");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (696, "uglifyjs");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (697, "ultraedit");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (698, "underscore");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (699, "undertow");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (700, "unity");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (701, "unreal engine");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (702, "upsource");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (703, "utm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (704, "vagrant");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (705, "vala");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (706, "varnish");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (707, "vault");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (708, "vba");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (709, "vert.x");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (710, "vim");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (711, "virtualbox");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (712, "visual studio");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (713, "visual studio code");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (714, "vmware");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (715, "vmware vsphere");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (716, "vpn");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (717, "vr");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (718, "vue native");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (719, "vue.js");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (720, "vuepress");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (721, "vuetify");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (722, "vuex");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (723, "vultr");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (724, "webassembly");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (725, "webflow");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (726, "webgl");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (727, "webpack");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (728, "webpacker");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (729, "webrtc");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (730, "websocket");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (731, "webstorm");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (732, "windows");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (733, "windows server");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (734, "wine");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (735, "woocommerce");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (736, "wordpress");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (737, "wpf");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (738, "wrike");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (739, "xamarin");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (740, "xamarin forms");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (741, "xampp");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (742, "xcode");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (743, "xen");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (744, "xenserver");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (745, "xml");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (746, "xunit");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (747, "yarn");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (748, "yii");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (749, "yui library");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (750, "zend framework");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (751, "zeplin");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (752, "zeromq");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (753, "zipkin");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (754, "zookeeper");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (755, "zoom");
INSERT INTO `matching`.`techstack` (`id`, `name`) VALUES (756, "zuul");