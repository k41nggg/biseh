- 或者把 profile 改为通过环境变量更灵活地配置。

默认数据库已改为本地 MySQL（用于开发）：

- 地址：127.0.0.1:3306
- 数据库：zhidao
- 用户名：root
- 密码：123123123

如果你尚未创建数据库，请先运行：

```powershell
mysql -u root -p123123123 -e "CREATE DATABASE IF NOT EXISTS zhidao CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

建表 SQL（以下 SQL 会创建与 JPA 实体对应的表结构和外键）：

```sql
-- users 表
CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  display_name VARCHAR(255),
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- posts 表
CREATE TABLE IF NOT EXISTS posts (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  summary TEXT,
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  author_id BIGINT,
  CONSTRAINT fk_posts_author FOREIGN KEY (author_id) REFERENCES users(id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- comments 表
CREATE TABLE IF NOT EXISTS comments (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  post_id BIGINT,
  author_id BIGINT,
  content TEXT,
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES users(id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

说明：
- 项目中 `spring.jpa.hibernate.ddl-auto` 仍然设置为 `update`，应用启动时也会自动创建/更新表结构；如果你更希望自己建表，则可以运行上面的 SQL。 
- 如果要在生产环境使用，请把密码与配置移到环境变量或 secrets 管理中，并调整 `ddl-auto` 为 `validate` 或移除。
