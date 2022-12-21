# StreamForwarder

## 概述

> 使用`kotlin`编写程序, 并打包为`jar`运行
> 使用了`kotlin-coroutines` 协程来提高效率

## 实现

* 使用`Socket`连接到目标服务器, 使用`SocketServer`来监听本地服务
* 创建两个协程来交换数据

## 编译

* 本项目使用`gradle`来编译项目, 并使用`ShadowJar`将代码编译为 `Jar`
* 使用`./gradlew build` (`Linux`, `Unix`) | `.\gradlew.bat build` (Windows) 编译
* 编译后的jar文件在`./build/libs/<ArtifactsName>-all.jar` ***请运行`-all`包含在文件名内的jar文件***
* > 在 `IntelliJ IDEA`中使用 `Ctrl` + `F9` 编译项目并不出出现 `-all` 字样在文件名内, 请直接运行输出的jar文件

## 运行

* 你可以在jar文件同级目录下创建`config.txt` 里面包含以下内容:
    * localHost: `<Local Host>`
    * localPort: `<Local Port>`
    * remoteHost: `<Remote Host>`
    * remotePort: `<Remote Port>`
* ***请将冒号后的配置替换为你所需要的配置***