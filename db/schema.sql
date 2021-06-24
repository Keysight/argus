SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema ARGUS
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `ARGUS` ;

CREATE SCHEMA IF NOT EXISTS `ARGUS` DEFAULT CHARACTER SET latin1 ;
USE `ARGUS` ;

-- -----------------------------------------------------
-- Table `ARGUS`.`b_runs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ARGUS`.`b_runs` ;

CREATE TABLE IF NOT EXISTS `ARGUS`.`b_runs` (
    `KEY` INT(11) NOT NULL AUTO_INCREMENT,
    `run_owner` VARCHAR(30) CHARACTER SET 'latin1' NOT NULL,
    `run_start_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `run_end_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `run_name` varchar(200) NOT NULL,
    `total_count_end` int(11) NOT NULL,
    `total_count_now` int(11) NOT NULL DEFAULT '0',
    `pass_count` int(11) NOT NULL DEFAULT '0',
    `fail_count` int(11) NOT NULL DEFAULT '0',
    `skip_count` int(11) NOT NULL DEFAULT '0',
    `pass_percent` tinyint(4) NOT NULL DEFAULT '0',
    `fail_percent` tinyint(4) NOT NULL DEFAULT '0',
    `skip_percent` tinyint(4) NOT NULL DEFAULT '0',
    `run_duration` int(20) NOT NULL DEFAULT '0',
    `run_comment` TEXT,
    `visible` TINYINT(4) NOT NULL DEFAULT '1',
    PRIMARY KEY (`KEY`),
    UNIQUE KEY `KEY` (`KEY`),
    KEY `run_owner` (`run_owner`),
    KEY `run_start_date` (`run_start_date`),
    KEY `run_name` (`run_name`),
    KEY `visible` (`visible`)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_bin;


-- -----------------------------------------------------
-- Table `ARGUS`.`b_tests`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ARGUS`.`b_tests` ;

CREATE TABLE IF NOT EXISTS `ARGUS`.`b_tests` (
    `KEY` INT(11) NOT NULL AUTO_INCREMENT,
    `run_key` INT(11) NOT NULL,
    `test_location` VARCHAR(500) NOT NULL,
    `test_name` VARCHAR(150) NOT NULL,
    `test_result` CHAR(5) NOT NULL,
    `test_log` LONGTEXT NOT NULL,
    `test_error` VARCHAR(1000) NOT NULL,
    `test_start_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `test_duration` INT(20) NOT NULL,
    `test_client_address` VARCHAR(40) NOT NULL,
    `test_client_port` VARCHAR(11) NOT NULL,
    `test_topology` VARCHAR(30) NOT NULL,
    `test_hash_code` VARCHAR(100) NOT NULL,
    `suite` VARCHAR(150) NOT NULL,
    `setup_type` VARCHAR(100) NOT NULL,
    `setup_sn` varchar(500) NOT NULL,
    `setup_address` VARCHAR(300) NOT NULL,
    `setup_ports` VARCHAR(1000) NOT NULL,
    `ports_type` VARCHAR(1000) NOT NULL,
    `tc_management` varchar(100) NOT NULL DEFAULT 'X',
    `bug_management` varchar(100) NOT NULL DEFAULT 'X',
    `test_comment` TEXT,
    `visible` TINYINT(4) NOT NULL DEFAULT '1',

    PRIMARY KEY (`KEY`),
    UNIQUE KEY `KEY` (`KEY`),
    FOREIGN KEY (`run_key`) REFERENCES b_runs(`KEY`),
    KEY `test_result` (`test_result`),
    KEY `test_location` (`test_location`),
    KEY `visible` (`visible`),
    KEY `test_topology` (`test_topology`),
    FULLTEXT KEY `setup_sn` (`setup_sn`)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_bin;


-- -----------------------------------------------------
-- Trigers to keep the table b_runs counts updated
-- -----------------------------------------------------

DROP TRIGGER IF EXISTS  `ARGUS`.`update_b_runs_i`;
delimiter //
CREATE TRIGGER `ARGUS`.`update_b_runs_i` AFTER INSERT ON `ARGUS`.`b_tests`
    FOR EACH ROW BEGIN
        IF NEW.visible = 1 THEN
            CASE NEW.test_result
                WHEN 'EXEC' THEN UPDATE `ARGUS`.`b_runs`
                    SET
                        total_count_now=total_count_now+1,
                        pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                        fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                        skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED)
                    WHERE `KEY` = NEW.`run_key`;
                WHEN 'PASS' THEN UPDATE `ARGUS`.`b_runs`
                    SET
                        total_count_now=total_count_now+1,
                        pass_count=pass_count+1,
                        run_duration=run_duration+NEW.test_duration,
                        pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                        fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                        skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED)
                    WHERE `KEY` = NEW.`run_key`;
                WHEN 'FAIL' THEN UPDATE `ARGUS`.`b_runs`
                    SET
                        total_count_now=total_count_now+1,
                        fail_count=fail_count+1,
                        run_duration=run_duration+NEW.test_duration,
                        pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                        fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                        skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED)
                    WHERE `KEY` = NEW.`run_key`;
                WHEN 'SKIP' THEN UPDATE `ARGUS`.`b_runs`
                    SET
                        total_count_now=total_count_now+1,
                        skip_count=skip_count+1,
                        pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                        fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                        skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED)
                    WHERE `KEY` = NEW.`run_key`;
                ELSE BEGIN END;
            END CASE;
        END IF;
    END;//
delimiter ;


DROP TRIGGER IF EXISTS  `ARGUS`.`update_b_runs_u`;
delimiter //
CREATE TRIGGER `ARGUS`.`update_b_runs_u` AFTER UPDATE ON `ARGUS`.`b_tests`
FOR EACH ROW BEGIN
    IF OLD.test_result = 'EXEC' THEN
        CASE NEW.test_result
            WHEN 'PASS' THEN UPDATE `ARGUS`.`b_runs`
                SET
                    pass_count=pass_count+1,
                    run_duration=run_duration+NEW.test_duration,
                    pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                    fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                    skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED),
                    run_end_date=NOW()
                WHERE `KEY` = NEW.`run_key`;
            WHEN 'FAIL' THEN UPDATE `ARGUS`.`b_runs`
                SET
                    fail_count=fail_count+1,
                    run_duration=run_duration+NEW.test_duration,
                    pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                    fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                    skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED),
                    run_end_date=NOW()
                WHERE `KEY` = NEW.`run_key`;
            WHEN 'SKIP' THEN UPDATE `ARGUS`.`b_runs`
                SET
                    skip_count=skip_count+1,
                    pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                    fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                    skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED),
                    run_end_date=NOW()
                WHERE `KEY` = NEW.`run_key`;
            ELSE BEGIN END;
        END CASE;
    END IF;
END;//
delimiter ;


DROP TRIGGER IF EXISTS  `ARGUS`.`update_b_runs_d`;
delimiter //
CREATE TRIGGER `ARGUS`.`update_b_runs_d` AFTER DELETE  ON `ARGUS`.`b_tests`
FOR EACH ROW BEGIN
    CASE OLD.test_result
        WHEN 'EXEC' THEN 
            UPDATE `ARGUS`.`b_runs` SET
                total_count_now=total_count_now-1,
                pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED)
            WHERE `KEY` = OLD.`run_key`;
        WHEN 'PASS' THEN 
            UPDATE `ARGUS`.`b_runs` SET
                total_count_now=total_count_now-1,
                pass_count=pass_count-1 ,
                run_duration=run_duration+OLD.test_duration,
                pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED)
            WHERE `KEY` = OLD.`run_key`;
        WHEN 'FAIL' THEN 
            UPDATE `ARGUS`.`b_runs` SET
                total_count_now=total_count_now-1,
                fail_count=fail_count-1,
                run_duration=run_duration+OLD.test_duration,
                pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED)
            WHERE `KEY` = OLD.`run_key`;
        WHEN 'SKIP' THEN 
            IF (SELECT total_count_now FROM `ARGUS`.`b_runs` WHERE (`KEY` = OLD.run_key) = 1) THEN
                DELETE FROM `ARGUS`.`b_runs` WHERE (`KEY` = OLD.run_key);
            ELSE
                UPDATE `ARGUS`.`b_runs` SET
                    total_count_now=total_count_now-1,
                    skip_count=skip_count-1,
                    pass_percent=CAST((pass_count*100)/total_count_now AS UNSIGNED),
                    fail_percent=CAST((fail_count*100)/total_count_now AS UNSIGNED),
                    skip_percent=CAST((skip_count*100)/total_count_now AS UNSIGNED)
                WHERE `KEY` = OLD.`run_key`;
            END IF;
        ELSE BEGIN END;
    END CASE;
END;//
delimiter ;


-- -----------------------------------------------------
-- Table `ARGUS`.`b_builds`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ARGUS`.`b_builds` ;

CREATE TABLE IF NOT EXISTS `ARGUS`.`b_builds` (
    `KEY` INT(11) NOT NULL AUTO_INCREMENT,
    `run_key` INT(11) NOT NULL,
    `product_name` VARCHAR(30) CHARACTER SET 'latin1' NOT NULL,
    `product_value` VARCHAR(30) CHARACTER SET 'latin1' NOT NULL,
    PRIMARY KEY (`KEY`),
    UNIQUE KEY `KEY` (`KEY`),
    FOREIGN KEY (`run_key`) REFERENCES b_runs(`KEY`) ON DELETE CASCADE,
    KEY `product_name` (`product_name`)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_bin;

CREATE UNIQUE INDEX `idx_b_builds_run_key_product_name` ON `ARGUS`.`b_builds` (run_key, product_name) COMMENT 'prevent duplicate product names for a run' ALGORITHM DEFAULT LOCK DEFAULT;

-- CREATE INDEX `KEY` ON `ARGUS`.`b_builds` (`KEY` ASC);
-- CREATE INDEX `run_key` ON `ARGUS`.`b_builds` (`run_key` ASC);
-- CREATE INDEX `product_name` ON `ARGUS`.`b_builds` (`product_name` ASC);


-- -----------------------------------------------------
-- Table `ARGUS`.`ui_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ARGUS`.`ui_users` ;

CREATE TABLE IF NOT EXISTS `ARGUS`.`ui_users` (
    `KEY` int(11) NOT NULL AUTO_INCREMENT,
    `user_name` varchar(50) NOT NULL,
    `email_address` varchar(100) NOT NULL,
    `account_type` varchar(10) NOT NULL,
    `run_owner` varchar(30) NOT NULL,
    `home_folder` varchar(100) NOT NULL,
    PRIMARY KEY (`KEY`),
    UNIQUE KEY `KEY` (`KEY`),
    KEY `user_name` (`user_name`)
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE = latin1_bin;


-- -----------------------------------------------------
-- Table `ARGUS`.`ui_persistence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ARGUS`.`ui_persistence` ;

CREATE TABLE IF NOT EXISTS `ARGUS`.`ui_persistence` (
    `KEY` INT(11) NOT NULL AUTO_INCREMENT,
    `user_key` INT(11) NOT NULL,
    `property_name` VARCHAR(100) CHARACTER SET 'latin1' NOT NULL,
    `property_value` VARCHAR(100) CHARACTER SET 'latin1' NOT NULL,
    PRIMARY KEY (`KEY`),
    UNIQUE KEY `KEY` (`KEY`),
    FOREIGN KEY (`user_key`) REFERENCES ui_users(`KEY`),
    KEY `property_name` (`property_name`)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_bin;

CREATE UNIQUE INDEX `idx_ui_persistence_user_key_property_name` ON `ARGUS`.`ui_persistence` (user_key, property_name) COMMENT 'used by insert or update' ALGORITHM DEFAULT LOCK DEFAULT;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
