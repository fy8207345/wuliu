# bosv2.0物流
BOS 后台管理系统，分为七个部分 ：
1、 基础设置 （物流业务管理 元数据 ）： 取派标准、取派时间管理、车辆管理、快
递员管理、区域管理 …
2、 取派： 下单管理、取件管理、配送管理 …
3、 中转： 货物运输过程中，中转点 出入库操作 …
4、 路由： 运输线路、运输交通工具 …
5、 PDA ： 快递员无线通讯设备 通讯功能 …
6、 财务 ：快递费用处理
7、 管理报表需求： 针对物流业务数据，产生报表
开发技术
Server 端架构： Struts2+ Spring + Spring Data(简化持久层) + JPA 接口+ Hibernate（JPA 显现）
后台管理系统 页面架构 ：jQuery Easyui 框架
前端互联网系统 页面架构 ：BootStrap 响应式 + AngularJS
Excel 解析、生成： POI 技术
远程调用： 基于 Restful 风格 CXF 编程
第三方短信平台、邮件平台 使用
Redis 缓存使用 、ActiveMQ 消息队列
搜索服务器 ElasticSearch 安装配送使用 ， Spring Data 操作 Elas