-- 企业运维项目 SQL
CREATE DATABASE IF NOT EXISTS ops_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE ops_demo;

DROP TABLE IF EXISTS emp;
DROP TABLE IF EXISTS dept;

CREATE TABLE dept (
    id       BIGINT      NOT NULL COMMENT 'ID',
    name     VARCHAR(20) NOT NULL COMMENT '部门名称',
    location VARCHAR(100) NULL COMMENT '地址',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

CREATE TABLE emp (
    id     BIGINT       NOT NULL COMMENT 'ID',
    name   VARCHAR(100) NOT NULL COMMENT '姓名',
    salary INT          NULL COMMENT '薪水',
    deptid BIGINT      NULL COMMENT '部门ID',
    PRIMARY KEY (id),
    CONSTRAINT fk_emp_dept FOREIGN KEY (deptid) REFERENCES dept(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

INSERT INTO dept (id, name, location) VALUES
(1, '研发部', '一楼'),
(2, '人事部', '二楼');

INSERT INTO emp (id, name, salary, deptid) VALUES
(1001, '张三', 5000, 1),
(1002, '李四', 4000, 1),
(1003, '王五', 1000, 2),
(1004, '赵六', 3000, 2);

UPDATE emp SET salary = salary * 1.1 WHERE name = '张三';
SELECT * FROM emp WHERE name LIKE '王%' ORDER BY salary DESC;
SELECT * FROM emp ORDER BY id LIMIT 1, 3;
SELECT e.id, e.name, e.salary, d.name AS dept_name, d.location
FROM emp e LEFT JOIN dept d ON e.deptid = d.id;
SELECT d.name, COUNT(e.id) AS emp_count
FROM dept d LEFT JOIN emp e ON d.id = e.deptid
GROUP BY d.id, d.name;
SELECT d.name, AVG(e.salary) AS avg_salary
FROM dept d JOIN emp e ON d.id = e.deptid
GROUP BY d.id, d.name
HAVING AVG(e.salary) > 3000;
