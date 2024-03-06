-- retrorack.rr_album_artist definition

CREATE TABLE `rr_album_artist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `artist_id` varchar(32) NOT NULL,
  `uri` varchar(64) NOT NULL,
  `href` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;