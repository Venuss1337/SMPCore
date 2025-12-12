CREATE TABLE IF NOT EXISTS `user` (
    `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `uuid` CHAR(36) NOT NULL,
    `nickname` VARCHAR(16) NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uq_users_uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;