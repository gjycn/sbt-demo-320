DROP DATABASE if exists my_db;
create database my_db;
use my_db;
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT primary key,
    `product_code` varchar(255) DEFAULT NULL COMMENT '商品编号',
    `stock_code`   varchar(255) DEFAULT NULL COMMENT '仓库编号',
    `count`        int(11)      DEFAULT NULL COMMENT '库存量'
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4;
