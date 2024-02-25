-- retrorack.rr_albums definition

CREATE TABLE `rr_albums` (
  `id` varchar(32) NOT NULL,
  `name` varchar(1024) DEFAULT NULL,
  `ReleaseDate` varchar(16) DEFAULT NULL,
  `totaltracks` varchar(8) DEFAULT NULL,
  `createDate` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;