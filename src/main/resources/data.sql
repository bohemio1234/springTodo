-- src/main/resources/data.sql

-- H2 데이터베이스는 일반적으로 ID를 1부터 시작합니다.
-- username, description, target_date, done 순서로 값을 넣습니다.
-- (주의: target_date 컬럼 이름은 JPA가 targetDate 필드 이름을 변환한 형태일 수 있습니다.
--  보통 target_date 또는 targetdate 입니다. SQL 오류 발생 시 H2 콘솔에서 실제 컬럼명 확인 필요)
--  LocalDate는 'YYYY-MM-DD' 형식의 문자열로 입력합니다.

INSERT INTO TODO (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE)
VALUES(10001, 'gemini_user', 'Learn JPA and H2', CURRENT_DATE + INTERVAL '1' YEAR, false);

INSERT INTO TODO (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE)
VALUES(10002, 'gemini_user', 'Master Spring Boot', CURRENT_DATE + INTERVAL '2' YEAR, false);

INSERT INTO TODO (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE)
VALUES(10003, 'another_user', 'Practice Coding Interview Questions', CURRENT_DATE + INTERVAL '6' MONTH, false);