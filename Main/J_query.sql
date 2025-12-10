-- 박진영 - Q/A , 댓글, 리뷰

# DROP DATABASE IF EXISTS `main-project-db`;
CREATE DATABASE IF NOT EXISTS `main-project-db`
CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `main-project-db`;

-- Q&A 
CREATE TABLE QaAs (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	title VARCHAR(50) NOT NULL COMMENT 'QaA 제목',
	context LONGTEXT NOT NULL COMMENT 'QaA 내용',

	view_count BIGINT NOT NULL DEFAULT 0 COMMENT '조회수',

	user_id BIGINT NOT NULL COMMENT '작성자',

	created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

	INDEX `idx_QaAs_created_at` (created_at),
	INDEX `idx_QaAs_updated_at` (updated_at),

	CONSTRAINT `fk_QaAs_user` FOREIGN KEY (user_id) REFERENCES users(id)
)
ENGINE=InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_unicode_ci
COMMENT = 'Q&A 테이블';



CREATE TABLE comments (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	comment VARCHAR(50) NOT NULL COMMENT '댓글',

	user_id BIGINT NOT NULL COMMENT '사용자',
	QaAs_id BIGINT NOT NULL COMMENT 'QaA',

	created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

	INDEX `idx_comments_user_id` (updated_at),
	INDEX `idx_comments_created_at` (created_at),
	INDEX `idx_comments_updated_at` (updated_at),

	CONSTRAINT `fk_user_commets` FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT `fk_QaA_comments` FOREIGN KEY (QaA_id) REFERENCES QaAs(id) ON DELETE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_unicode_ci
COMMENT = 'Q&A 댓글 테이블';

CREATE TABLE reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
	rating TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5) COMMENT '별점',
	content TEXT NULL COMMENT '리뷰 내용',
    
    user_id BIGINT NOT NULL COMMENT '사용자',
	room_id BIGINT NOT NULL COMMENT '숙소',
	
	created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    
	INDEX `idx_reviews_room` (room_id, rating),
    INDEX `idx_reviews_created_at` (created_at),
	INDEX `idx_reviews_updated_at` (updated_at),
    
	CONSTRAINT `fk_reviews_user_id` FOREIGN KEY (user_id) REFERENCES users(id),
	CONSTRAINT `fk_reviews_room_id` FOREIGN KEY (exhibition_id) REFERENCES exhibitions(id)
)
ENGINE=InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_unicode_ci
COMMENT = '리뷰 테이블';