# CangQiong-Takeout
#  **苍穹外卖项目后端工程**（包含骑手端）

骑手端GitHub地址：https://github.com/Zshuaixxx/CangQiong-Rider

#### 本工程完成苍穹外卖所有功能，并在此基础之上二次开发：

1. ##### 管理端操作日志记录功能

2. ##### 骑手端（包括订单模块，骑手个人信息管理模块）



##### 二次开发功能的部分思路和关键代码:

###### 1.管理端记录操作日志功能

新建操作日志记录表

![72880363336](C:\Users\ZUO\AppData\Local\Temp\1728803633365.png)

采用AOP切面技术使用自定义注解对需要记录的方法进行注解标记

###### 2.骑手端订单查询时，把行政区划编码加入查询条件，保证骑手只能查询到接单地点附近订单，并腾讯地图API对距离进行计算

![72880433049](C:\Users\ZUO\AppData\Local\Temp\1728804330491.png)

![72880429998](C:\Users\ZUO\AppData\Local\Temp\1728804299986.png)

###### 3.骑手抢单功能，对于多个骑手同时抢单使用乐观锁防止抢单错误情况发生

![72880448703](C:\Users\ZUO\AppData\Local\Temp\1728804487035.png)

###### 4.骑手送达订单时，需拍照上传送达凭证，图片上传使用阿里云OSS储存

###### 5.对于送达订单，记录骑手收益，提供今日收益和月收益两个统计接口

![72880478163](C:\Users\ZUO\AppData\Local\Temp\1728804781636.png)

![72880484049](C:\Users\ZUO\AppData\Local\Temp\1728804840499.png)

###### 6.使用redis储存平台“关于我们”信息

![72880492410](C:\Users\ZUO\AppData\Local\Temp\1728804924101.png)

![72880494128](C:\Users\ZUO\AppData\Local\Temp\1728804941289.png)