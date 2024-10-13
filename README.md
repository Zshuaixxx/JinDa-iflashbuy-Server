# CangQiong-Takeout
#  **苍穹外卖项目后端工程**（包含骑手端）

骑手端GitHub地址：https://github.com/Zshuaixxx/CangQiong-Rider

#### 本工程完成苍穹外卖所有功能，并在此基础之上二次开发：

1. ##### 管理端操作日志记录功能

2. ##### 骑手端（包括订单模块，骑手个人信息管理模块）



##### 二次开发功能的部分思路和关键代码:

###### 1.管理端记录操作日志功能

新建操作日志记录表

![image](https://github.com/user-attachments/assets/839e1374-71c6-430a-b9b9-513293c9718d)


采用AOP切面技术使用自定义注解对需要记录的方法进行注解标记

###### 2.骑手端订单查询时，把行政区划编码加入查询条件，保证骑手只能查询到接单地点附近订单，并腾讯地图API对距离进行计算

![1728804330491](https://github.com/user-attachments/assets/98b69d34-4838-42fa-9ba1-27e38713edd8)


![1728804299986](https://github.com/user-attachments/assets/8311316a-0a96-4d58-a2c0-e7b192f5055f)


###### 3.骑手抢单功能，对于多个骑手同时抢单使用乐观锁防止抢单错误情况发生

![1728804487035](https://github.com/user-attachments/assets/5d87de8f-0378-4f80-8780-fdb01834f1a4)


###### 4.骑手送达订单时，需拍照上传送达凭证，图片上传使用阿里云OSS储存

###### 5.对于送达订单，记录骑手收益，提供今日收益和月收益两个统计接口

![1728804781636](https://github.com/user-attachments/assets/30464b89-c079-41d3-a5e0-c8a6e042c6d3)


![1728804840499](https://github.com/user-attachments/assets/d3bf86cc-d52c-4083-920a-6aaa3ca25826)


###### 6.使用redis储存平台“关于我们”信息

![1728804924101](https://github.com/user-attachments/assets/c4919769-81e5-4dfb-a173-f1f31858afbb)


![1728804941289](https://github.com/user-attachments/assets/8109e57c-59b8-45bd-a417-9b3cd6d1346a)
