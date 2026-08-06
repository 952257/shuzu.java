-- 一对多：部门 / 员工
CREATE TABLE IF NOT EXISTS t_department (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50),
  location VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS t_employees (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50),
  salary INT,
  dept_id INT
);

DELETE FROM t_employees;
DELETE FROM t_department;
INSERT INTO t_department VALUES (1, 'A部门', '一楼');
INSERT INTO t_department VALUES (2, 'B部门', '二楼');
INSERT INTO t_employees VALUES (1001, '张三', 5000, 1);
INSERT INTO t_employees VALUES (1002, '李四', 6000, 1);
INSERT INTO t_employees VALUES (1003, '张三', 3000, 2);

-- 多对多：学生 / 课程
CREATE TABLE IF NOT EXISTS t_student (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50),
  sex VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS t_subject (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50),
  grade INT
);

CREATE TABLE IF NOT EXISTS t_stu_sub (
  id INT PRIMARY KEY AUTO_INCREMENT,
  student_id INT,
  subject_id INT
);

DELETE FROM t_stu_sub;
DELETE FROM t_student;
DELETE FROM t_subject;
INSERT INTO t_student VALUES (1001, 'tom', 'M');
INSERT INTO t_student VALUES (1002, 'jack', 'M');
INSERT INTO t_student VALUES (1003, '李四', 'F');
INSERT INTO t_student VALUES (1004, '王五', 'F');
INSERT INTO t_subject VALUES (10, 'JavaSe', 1);
INSERT INTO t_subject VALUES (20, 'JavaWeb', 2);
INSERT INTO t_subject VALUES (30, 'Framework', 3);
INSERT INTO t_subject VALUES (40, 'MicroService', 4);
INSERT INTO t_stu_sub VALUES (1, 1001, 10);
INSERT INTO t_stu_sub VALUES (2, 1001, 20);
INSERT INTO t_stu_sub VALUES (3, 1002, 10);
INSERT INTO t_stu_sub VALUES (4, 1003, 10);
