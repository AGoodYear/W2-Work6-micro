
**W2R6**

**备忘录微服务项目**

**技术栈**

服务发现：Nacos

RPC调用：Dubbo

限流熔断：Sentinel

链路追踪：MicroMeter+Zipkin

网关：Spring Cloud Gateway

安全框架：SaToken

ORM：Mybatis-Plus

**项目简介**

基于SpringCloud的微服务项目。

接口文档：https://apifox.com/apidoc/shared-a0bede62-9704-4074-b6d6-7a0589c6fe16

仓库地址：https://github.com/AGoodYear/W2-Work6-micro

项目完成度：80%

项目部署地址：154.40.59.2:8133（网关） 9411（Zipkin） 8080（Sentinel） 15672(RabbitMQ)  8848(Nacos)

项目结构

|Plain Text<br>├─commons （公用API、返回结果类等）<br>│  ├─src<br>│  │  ├─main<br>│  │  │  ├─java<br>│  │  │  │  └─com<br>│  │  │  │      └─ivmiku<br>│  │  │  │          └─w6r1<br>│  │  │  │              ├─api rpc调用的api<br>│  │  │  │              ├─entity <br>│  │  │  │              └─response 返回结果封装<br>├─gateway-8133 （网关）<br>│  ├─src<br>│  │  ├─main<br>│  │  │  ├─java<br>│  │  │  │  └─com<br>│  │  │  │      └─ivmiku<br>│  │  │  │          └─w6r1<br>│  │  │  │              ├─component satoken框架相关配置<br>│  │  │  │              └─config 相关配置<br>├─todo-8096 （对应备忘录模块）<br>│  ├─src<br>│  │  ├─main<br>│  │  │  ├─java<br>│  │  │  │  └─com<br>│  │  │  │      └─ivmiku<br>│  │  │  │          └─w6r1<br>│  │  │  │              ├─config <br>│  │  │  │              ├─entity<br>│  │  │  │              ├─listener rabbitmq异步插入备忘录<br>│  │  │  │              ├─mapper<br>│  │  │  │              ├─service<br>│  │  │  │              └─utils<br>└─user-8077 （对应用户模块）<br>`    `├─src<br>`    `│  ├─main<br>`    `│  │  ├─java<br>`    `│  │  │  └─com<br>`    `│  │  │      └─ivmiku<br>`    `│  │  │          └─w6r1<br>`    `│  │  │              ├─controller<br>`    `│  │  │              ├─entity<br>`    `│  │  │              ├─filter<br>`    `│  │  │              ├─mapper<br>`    `│  │  │              ├─service<br>`    `│  │  │              └─utils|
| :- |

**项目实现：**

**Docker-compose脚本**

|Dockerfile<br>version: "3"<br><br>services:<br>` `mysql:<br>`   `image: mysql:8.0.31<br>`   `container\_name: mysql<br>`   `restart: always<br>`   `ports :<br>`     `-  "3306:3306"<br>`   `environment:<br>`     `-  MYSQL\_ROOT\_PASSWORD=12345abcde<br>`     `-  MYSQL\_ROOT\_HOST=%    <br><br>` `redis:<br>`   `image:  redis<br>`   `container\_name:  redis<br>`   `ports:<br>`     `-  "6379:6379"<br>`   `command:<br>`     `--requirepass  "12345abcde"  <br>  <br>` `rabbitmq:<br>`   `image:  rabbitmq:3.7.7-management    <br>`   `container\_name:  rabbitmq<br>`   `restart:  always<br>`   `ports:<br>`      `-  "5672:5672"<br>`      `-  "15672:15672"      <br>`   `environment:<br>`      `-  RABBITMQ\_DEFAULT\_USER=guest<br>`      `-  RABBITMQ\_DEFAULT\_PASS=guest<br>  <br><br>` `nacos:<br>`    `image:  nacos/nacos-server<br>`    `container\_name:  nacos<br>`    `ports:  <br>`      `-  "8848:8848"<br>`    `environment:<br>`      `-  JVM\_XMS=256m <br>`      `-  JVM\_XMX=256m<br>`      `-  MODE=standalone<br>`      `-  MYSQL\_SERVICE\_HOST=154.3.2.56<br>`      `-  MYSQL\_SERVICE\_DB\_NAME=nacos\_db<br>`      `-  MYSQL\_SERVICE\_USER=root<br>`      `-  MYSQL\_SERVICE\_PASSWORD=12345abcde<br>    <br>` `gateway:<br>`   `image:  gateway:v1.0<br>`   `container\_name:  gateway<br>`   `ports:<br>`      `-  "8133:8133"<br><br>` `user:<br>`   `image:  user:v1.0<br>`   `container\_name:  user<br>`   `ports:<br>`      `-  "8077:8077"<br>`   `depends\_on:<br>`      `-  nacos<br>`      `-  mysql<br>`      `-  redis<br><br>` `todo:<br>`   `image:  todo:v1.0<br>`   `container\_name:  todo<br>`   `ports:<br>`      `-  "8096:8096"<br>`   `depends\_on:<br>`      `-  nacos<br>`      `-  mysql<br>`      `-  redis<br><br>` `zipkin:<br>`   `image:  zipkin:v1.0<br>`   `container\_name:  zipkin<br>`   `ports:<br>`      `-  "9411:9411"<br><br>` `sentinel:<br>`   `image:  sentinel:v1.0<br>`   `container\_name:  sentinel<br>`   `ports:<br>`      `-  "8080:8080"<br><br>  <br>  |
| :- |

> **项目亮点**

**使用Sentinel实现网关处限流**

使用Sentinel+Spring Cloud Gateway实现网关处限流，同一路由QPS设置为10，且同一IPQPS设置为2，从网关处先保护微服务防止流量过大。

**集成安全框架防止直接访问对应微服务模块**

使用SaToken的SAME-Token功能解决微服务间通信的鉴权问题，并防止非法访问（直接访问对应服务不经过网关）。

**RabbitMQ实现异步备忘录写入数据库**

将数据库的写入和用户端的发送分开，提高用户体验的同时也提升服务的可用性

**Micrometer+Zipkin实现链路追踪**

使用Micrometer对整个调用过程进行分析，并用Zipkin图形化展现数据

**待改进的点**

集成ElasticSearch：集成ElasticSearch替换数据库的like语句以提升查找性能
