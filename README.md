Spring Boot JEST SSL
====================

You want SSL and security on Spring Boot JEST, here you go

Installation
------------

In your Java application add a dependency on:

```xml
<dependency>
    <groupId>com.infobelt</groupId>
    <artifactId>spring-boot-jest-ssl</artifactId>
    <version>0.2.7</version>
</dependency>
```

Getting Started
---------------

In your application.yml you will need to add

```yaml

spring:
    ...
    data:
        jest:
            uri: http://localhost:9200
            ssl:
                enabled: false
                keyStorePath: /key/path
                keyPassword: mypassword
```

License
=======

[See license](LICENSE.md)
