CREATE TABLE retrorack.rr_albums (
	id varchar(32) NOT NULL,
	name varchar(1024) NULL,
	ReleaseDate varchar(16) NULL,
	totaltracks varchar(8) NULL,
	CONSTRAINT albums_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;
