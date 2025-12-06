# OnlineOpenChat

## DDL

### auth_refresh_token
* jwt RefreshToken 관리 테이블
```
    CREATE TABLE auth_refresh_token (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id BIGINT NOT NULL, -- user 테이블의 t_id 값
        refresh_token VARCHAR(255) NOT NULL, -- jwt refreshToken 값
        expired_at TIMESTAMP NOT NULL, -- 만료 일자 ex) 1일, 7일...
        is_revoked TINYINT(1) NOT NULL DEFAULT 0, -- 강제 만료 처리(로그 아웃)
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    );
```
### user, user_credentials
* 유저 정보 관리 테이블
```
    create table user_credentials
    (
        user_t_id       BIGINT   null, -- 유저 고유의 ID 값, 유저 테이블의 t_id 값
        hashed_password char(60) not null, -- 해싱된 유저 비번
        constraint user_t_id UNIQUE (user_t_id)
    );

    create table user
    (
        t_id       BIGINT AUTO_INCREMENT PRIMARY KEY   primary key,
        name       VARCHAR(100)                        not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP null
    );
```





