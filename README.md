## properties-maven-plugin
properties-maven-plugin是一个maven插件，致力于提高开发效率，降低配置出错的概率。

## 简单描述
插件出发点：单工程多环境配置(如：jdbc)，多工程多环境配置

存在的问题：

       1 项目中存在大量重复的配置信息(常见的有properties文件中)； 
       2 开发也好部署也好，一不小心忘记修改一个值，直接导致正式环境异常重来
       3 想要查询某一配置都应用于哪些项目困难

本插件解决了以上所有问题，发挥想象可以实现更多功能

描述： 配置信息可以不配置到工程中了，可以从电脑中的某一路径或文件，也可以从数据库中获取
支持多级继承
如果现在数据库中已有字典表或系统参数表，就可以支持使用，相当方便

## Quick Start
```xml
<plugins>
    <plugin>
        <groupId>com.dplugin.maven.plugins</groupId>
        <artifactId>properties-maven-plugin</artifactId>
        <version>1.0.0</version>
        <dependencies>...</dependencies>
        <configuration>...</configuration>
    </plugin>
</plugins>
```

## maven命令
```shell
mvn properties:create
mvn properties:replace
```

## dependencies
如果要使用数据库，需要指定驱动依赖，如下
mysql:
```xml
<dependencies>
	<dependency>
	    <groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>5.1.18</version>
	</dependency>
</dependencies>
```

## configuration
普通配置项：
```xml
<configuration>
    <skip>false</skip><!--可选 默认:false 是否启用插件-->
	<encoding>UTF-8</encoding><!--可选 默认:工程编码 文件编码-->
	<packSeparator>.</packSeparator><!--可选 默认:. pack分割符-->
	<packKeySeparator>@</packKeySeparator><!--可选 默认:@ pack与key的分割符-->
	
	<skipCreate>false</skipCreate><!--可选 默认:false 是否启用生成文件功能-->
	<isCover>true</isCover><!--可选 默认:true 如果文件存在是否覆盖-->
	
	<skipReplace>false</skipReplace><!--可选 默认:false 是否启用替换功能-->
    ...
</configuration>
```

数据源(文件或数据库)：
```xml
<configuration>
    <!--绝对路径， 可以是目录或文件， 可以包含 ** 或 * -->
    <directory>${basedir}/src/main/**/demo.properties</directory>
    <dataSource>
		<url>jdbc:mysql://localhost:3306/system?useUnicode=true&amp;characterEncoding=utf-8</url>
		<username>root</username>
		<password>root</password>

        <!--根packId-->
		<rootId>0</rootId>
		<!--获取pack对应的ID, 字段名必须保持一致, 参数2个-->
		<sqlGetIdByPidAndPack>select id from t1 where pid=? and pack=?</sqlGetIdByPidAndPack>
		<!--获取属性列表sql, 字段名必须保持一致，参数1个, tid就是packId-->
		<sqlGetPropertiesByPid>select id,title,`key`,value from t2 where tid=?</sqlGetPropertiesByPid>
	</dataSource>
    ...
</configuration>
```

生成文件规则配置：
```xml
<createRules>
	<rule>
		<filtering>true</filtering><!--必须 默认:false 是否启用-->
		<!--必须 要创建的文件，相对工程路径-->
		<!-- 目前只能生成.properties属性文件 -->
		<file>src/main/resources/jdbc.properties</file>
		<includePackes>
		    <!--有继承功能，需要数据支持，如下：通用.工程.数据库.环境.人-->
			<pack>common.project.jdbc.test.person</pack>
		</includePackes>
	</rule>
</createRules>
```

替换文件中的属性规则配置(强烈建议只替换编译过后的文件，除properties文件，其它文件替换后不可逆)：
```xml
<replaceRules>
	<rule>
		<filtering>true</filtering>
		<!--有继承功能，需要数据支持，如下：通用.工程.数据库.环境.人-->
		<pack>common.project.jdbc.test.person</pack>
		<includes>
		    <!-- 要替换属性的文件或目录，相对工程路径，可以包含 ** 或 * -->
		    <!-- 1. properties文件以key为查找目录，替换value的值 -->
			<include>src/main/**/*.properties</include>
			<!-- 2. 其他类型的文件(xml, java, ...)，搜索${key}直接替换成值 -->
			<include>src/main/**/*.xml</include>
		</includes>
		<excludes>
		    <!-- 要排除的文件或目录，相对工程路径，可以包含 ** 或 * -->
			<!--<exclude></exclude>-->
		</excludes>
	</rule>
</replaceRules>
```
