-- retrorack.rr_album_information definition

CREATE TABLE `rr_album_information` (
  `id` varchar(32) NOT NULL,
  `label` varchar(128) DEFAULT NULL,
  `popularity` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;