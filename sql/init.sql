-- 建库脚本：首次运行前执行本文件，再导入 pet_adoption.sql
-- 用法： mysql -uroot -p < init.sql

CREATE DATABASE IF NOT EXISTS `pet_adoption`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE `pet_adoption`;
