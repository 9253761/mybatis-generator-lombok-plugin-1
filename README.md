# mybatis-generator-lombok-plugin

## 功能

- 整合lombok插件实现`@Data`自动注解，取消`getter`、`setter`方法的自动生成。
- 自定义注释生成器，抓取数据库表的列注释作为实体类注释。

## 用法

- `git clone`到IDEA，根据自己需要进行更改，再添加到本地maven仓库。
- `git clone`到本地，通过`maven clean install`添加到本地maven仓库。

## 使用

#### 1. pom添加插件

- 添加插件依赖，mybatis-generator-lombok-plugin
- 根据项目的类型选择数据库依赖，mysql-connector-java或oracle

```xml
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.7</version>
    <configuration>
        <configurationFile>src/main/resources/generatorConfig.xml</configurationFile>
        <overwrite>true</overwrite>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.16</version>
        </dependency>
        <dependency>
            <groupId>com.cy</groupId>
            <artifactId>mybatis-generator-lombok-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</plugin>
```

#### 2. 项目添加配置文件

添加mybatis-generator配置文件`generatorConfig.xml`、`generatorConfig.properties`到项目，并在`generatorConfig.xml`中引入本插件。

```xml
<generatorConfiguration>
    <properties resource="generatorConfig.properties"/>

    <context id="mysql" targetRuntime="MyBatis3">
        <!-- 本地插件，自动注解 @Data -->
        <plugin type="com.cy.mybatis.generator.lombok.plugins.LombokPlugin"/>
        <!-- 本地插件，生成数据库注释 -->
        <plugin type="com.cy.mybatis.generator.lombok.plugins.CommentPlugin"/>
        
        xxx
        xxx
        
    </context>
</generatorConfiguration>
```

#### 3. 其他配置及修改

- 在项目中建好：xml文件目录、domain（pojo）文件目录、dao（mapper接口）文件目录
- 修改`generatorConfig.properties`中的属性，如数据库链接、密码、各路径、表名等

#### 4. maven插件执行

执行插件：mybatis-generator:generate，在各目录下生成xml、domain、dao三类文件。