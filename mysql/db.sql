CREATE TABLE `emp` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `empno` varchar(20) DEFAULT NULL COMMENT '员工号',
  `ename` varchar(20) DEFAULT NULL COMMENT '员工姓名',
  `job` varchar(20) DEFAULT NULL COMMENT '工作',
  `mgr` int(11) DEFAULT NULL COMMENT '上级编号',
  `hiredate` date DEFAULT NULL COMMENT '入职日期',
  `salary` decimal(10,2) DEFAULT NULL COMMENT '工资',
  `comm` decimal(10,2) DEFAULT NULL COMMENT '奖金',
  `deptno` int(11) DEFAULT NULL COMMENT '部门编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4;

INSERT INTO `emp` (`id`, `empno`, `ename`, `job`, `mgr`, `hiredate`, `salary`, `comm`, `deptno`)
VALUES
	(1, '7369', 'SMITH', 'CLERK', 7902, '1980-12-17', 800.00, NULL, 20),
	(2, '7499', 'ALLEN', 'SALESMAN', 7698, '1981-02-20', 1600.00, 300.00, 30),
	(3, '7521', 'WARD', 'SALESMAN', 7968, '1981-02-22', 1250.00, 500.00, 30),
	(4, '7566', 'JONES', 'MANAGER', 7839, '1981-04-02', 2975.00, NULL, 20),
	(5, '7654', 'MARTIN', 'SALESMAN', 7698, '1981-09-28', 1250.00, 1500.00, 30),
	(6, '7698', 'BLAKE', 'MANAGER', 7839, '1981-05-01', 2850.00, NULL, 30),
	(7, '7782', 'CLARK', 'MANAGER', 7839, '1981-06-09', 2450.00, NULL, 10),
	(8, '7788', 'SCOTT', 'ANALYST', 7566, '1987-04-19', 4000.00, NULL, 20),
	(9, '7839', 'KING', 'PRESIDENT', NULL, '1981-11-17', 5000.00, NULL, 10),
	(10, '7844', 'TURNER', 'SALESMAN', 7698, '1981-09-08', 1500.00, 0.00, 30),
	(11, '7876', 'ADAMS', 'CLERK', 7788, '1987-05-23', 1100.00, NULL, 20),
	(12, '7900', 'JAMES', 'CLERK', 7698, '1981-12-03', 950.00, NULL, 30),
	(13, '7902', 'FORD', 'ANALYST', 7566, '1981-12-03', 3000.00, NULL, 20),
	(14, '7934', 'MILLER', 'CLERK', 7782, '1982-01-23', 1300.00, NULL, 10),
	(15, '102', 'EricHu', 'Developer', 1455, '2011-05-26', 5000.00, 14.00, 10),
	(16, '104', 'Jason', 'PM', 1455, '2011-05-26', 5500.00, 14.00, 10),
	(17, '105', 'WANGJING', 'Developer', 1455, '2011-05-26', 5500.00, 14.00, 10);


CREATE TABLE `dept` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `dept` int(11) DEFAULT NULL COMMENT '部门编号',
  `dname` varchar(20) DEFAULT NULL COMMENT '部门名称',
  `location` varchar(20) DEFAULT NULL COMMENT '地点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;


INSERT INTO `dept` (`id`, `dept`, `dname`, `location`)
VALUES
	(1, 10, 'ACCOUNTING', 'NEW YORK'),
	(2, 20, 'RESEARCH', 'DALLAS'),
	(3, 30, 'SALES', 'CHICAGO'),
	(4, 40, 'OPERATIONS', 'BOSTON'),
	(5, 50, '50abs', '50def'),
	(6, 60, 'Developer', 'Shanghai');
