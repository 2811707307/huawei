#  huawei

> 基于 SpringBoot_Thymeleaf 做的手机售卖网，仿华为手机官网制作，前后端分离，网上商城都这样，大差不差，随着技术的提升👆，会增加更多的功能，并尽可能的完善注释。希望对同样初学的你有参考。



## 相关框架

- `SpringBoot`  ------ ***后端框架***
- `JPA` ------ 持久层 API
- `SpringMVC` ------ MVC 框架
- `Shiro` ------- 安全框架
- `Thymeleaf`  ------ ***前端模板***
- `Vue` ------ 渐进式JS 框架
- `BootStrap` ------ 强大的前端开发框架 
- `Axios` ------ AJAX 框架



## 环境需求

> 配置在 src/main/resources/application.yaml 文件中，请根据需求修改。

- Redis 5.0 及以上
- MariaDB 10.0 及以上



## 运行准备

1. 创建数据库 huawei，导入 huawei.sql 表结构，自带管理员 admin / admin
2. 导入数据时命令行登录后使用  source c:/xxx 的形式无报错导入
3. 运行项目，前台地址: localhost:8080     后台地址 localhost:8080/admin



## 后记

> 打算打包成 Docker 镜像的形式，希望对初学的人有帮助