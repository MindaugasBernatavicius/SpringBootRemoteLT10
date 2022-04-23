DROP TABLE IF EXISTS `sdaremotelt10`.`blog_post`;

CREATE TABLE `blog_post` (
     `id` bigint(20) NOT NULL,
     `blah` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
     `date` date DEFAULT NULL,
     `text` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
     `title` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `sdaremotelt10`.`blog_post`
(`id`, `text`, `title`, `date`, `blah`)
VALUES
    (1, 'BP1', 'BPT1', CURRENT_DATE(), 'blah blah'),
    (2, 'BP2', 'BPT2', CURRENT_DATE(), 'blah blah'),
    (3, 'BP3', 'BPT3', CURRENT_DATE(), 'blah blah'),
    (4, 'BP4', 'BPT4', CURRENT_DATE(), 'blah blah');