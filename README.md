# MyBlog_Backend
Source Code of the MyBlog backend

## Application.yml

```yml
server:

 port: ENTER_YOUR_SPRINGBOOT_PORT_HERE

spring:

 thymeleaf:

  cache: false

 servlet:

  multipart:

   enabled: true

   file-size-threshold: 2MB

   max-file-size: 5MB

   max-request-size: 10MB
   

 datasource:

  name: my-blog-datasource

  driverClassName: com.mysql.cj.jdbc.Driver

  url: ENTER_YOUR_DATABASE_URL_HERE

  username: USERNAME_HERE

  password: PASSWORD_HERE

  hikari:

   minimum-idle: 5

   maximum-pool-size: 15

   auto-commit: true

   idle-timeout: 30000

   pool-name: hikariCP

   max-lifetime: 30000

   connection-timeout: 30000

   connection-test-query: SELECT 1

 jpa:

  show-sql: true

mybatis:

 mapper-locations: classpath:mapper/*Mapper.xml

 configuration:

  log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

cloud:

 aws:

  credentials:

   access-key: AWS-ACCESS-KEY-HERE

   secret-key: AWS-SECRET-KEY-HERE

  region:

   static: ca-central-1

  stack:

   auto: false

mail-sender:

 username: ENTER_YOUR_EMAIL_ADDRESS

 password: ENTER_PASSWORD

application:

 bucket:

  name: ENTER_BUCKET_NAME_FOR_FILE_STORGE


```



