-- Users
INSERT INTO users(account_id, password, nickname, user_type, create_time) VALUES('test1', '', 'test-nickname1', 1, now());
INSERT INTO users(account_id, password, nickname, user_type, create_time) VALUES('test2', '', 'test-nickname2', 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP));
INSERT INTO users(account_id, password, nickname, user_type, create_time) VALUES('test3', '', 'test-nickname3', 1, DATEADD('MONTH', -1, CURRENT_TIMESTAMP));
INSERT INTO users(account_id, password, nickname, user_type, create_time) VALUES('admin', '', 'administrator', 100, DATEADD('YEAR', -1, CURRENT_TIMESTAMP));

-- Busking(Active)
-- 정자동
INSERT INTO busking(host_id, latitude, longitude, title, description, managed_start_time, managed_end_time, create_time) VALUES(1, 37.368, 127.1090,                        '정자동 버스킹1', '좌상단 버스킹',                    CURRENT_TIMESTAMP, DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);
INSERT INTO keyword(post_id, post_type, keyword) VALUES(1, 1, '정자동');
INSERT INTO keyword(post_id, post_type, keyword) VALUES(1, 1, '정자교');
INSERT INTO keyword(post_id, post_type, keyword) VALUES(1, 1, '뮤즈-!');

INSERT INTO busking(host_id, latitude, longitude, title, description, managed_start_time, managed_end_time, create_time) VALUES(1, 37.367930384523222, 127.109074734832,    '정자동 버스킹2', '바로 활성화되는 버스킹',             CURRENT_TIMESTAMP, DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);
INSERT INTO keyword(post_id, post_type, keyword) VALUES(2, 1, '농구');
INSERT INTO keyword(post_id, post_type, keyword) VALUES(2, 1, '버스킹');
INSERT INTO keyword(post_id, post_type, keyword) VALUES(2, 1, '뮤즈-!!!');
INSERT INTO image(post_id, post_type, path, upload_order) VALUES(2, 1, 'image1.png', 0);
INSERT INTO image(post_id, post_type, path, upload_order) VALUES(2, 1, 'image2.png', 1);
INSERT INTO image(post_id, post_type, path, upload_order) VALUES(2, 1, 'image3.png', 2);

INSERT INTO busking(host_id, latitude, longitude, title, description, managed_start_time, managed_end_time, create_time) VALUES(1, 37.367322183399165, 127.109028192323,    '정자동 버스킹3', '1분 뒤 활성화되는 버스킹',           DATEADD('MINUTE', 1, CURRENT_TIMESTAMP), DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);
INSERT INTO keyword(post_id, post_type, keyword) VALUES(3, 1, '농구');
INSERT INTO keyword(post_id, post_type, keyword) VALUES(3, 1, '버스킹');
INSERT INTO keyword(post_id, post_type, keyword) VALUES(3, 1, '뮤즈-!!!');
INSERT INTO image(post_id, post_type, path, upload_order) VALUES(3, 1, 'image3.png', 2);
INSERT INTO image(post_id, post_type, path, upload_order) VALUES(3, 1, 'image4.png', 1);
INSERT INTO image(post_id, post_type, path, upload_order) VALUES(3, 1, 'image5.png', 0);

INSERT INTO busking(host_id, latitude, longitude, title, description, managed_start_time, managed_end_time, create_time) VALUES(1, 37.366583943581222, 127.109124838234,    '정자동 버스킹4', '45분 뒤 활성화되는 버스킹',          DATEADD('MINUTE', 45, CURRENT_TIMESTAMP), DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);
INSERT INTO busking(host_id, latitude, longitude, title, description, managed_start_time, managed_end_time, create_time) VALUES(1, 37.366, 127.1092,                        '정자동 버스킹5', '가운데 버스킹',                    CURRENT_TIMESTAMP, DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);
INSERT INTO busking(host_id, latitude, longitude, title, description, managed_start_time, managed_end_time, create_time) VALUES(1, 37.365323253432553, 127.109328538523,    '정자동 버스킹6', '2분 뒤 활성화되는 버스킹',           DATEADD('MINUTE', 2, CURRENT_TIMESTAMP), DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);
INSERT INTO busking(host_id, latitude, longitude, title, description, managed_start_time, managed_end_time, create_time) VALUES(1, 37.364434875348599,  127.109423141257,   '정자동 버스킹7', '2분 뒤 활성화되는 버스킹2\n재밌겠당',  DATEADD('MINUTE', 2, CURRENT_TIMESTAMP), DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);
INSERT INTO busking(host_id, latitude, longitude, title, description, managed_start_time, managed_end_time, create_time) VALUES(1, 37.363428473593485, 127.109488237282,    '정자동 버스킹8', '1일 뒤 활성화되는 버스킹',           CURRENT_TIMESTAMP, DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);
INSERT INTO busking(host_id, latitude, longitude, title, description, managed_start_time, managed_end_time, create_time) VALUES(1, 37.363, 127.1095,                        '정자동 버스킹9', '우하단 버스킹',                    CURRENT_TIMESTAMP, DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);