CREATE TABLE IF NOT EXISTS `player_inventory_backup` (
    `backup_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id` INT UNSIGNED NOT NULL,
    `death_cause` VARCHAR(120) DEFAULT 'UNKNOWN',
    `inventory_data` MEDIUMBLOB NOT NULL, -- Compressed binary data
    `last_updated` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`backup_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;