import java.io.File


fun main(args: Array<String>) {
    if (args.isNotEmpty() && args.size == 4) {
        Config().write(args)
        println(
            "已在config.txt中保存了本次的配置文件\n" + "可直接使用java -jar <JARFile>\n" + "不添加参数直接启动"
        )
        StreamForwarder(args[0], args[1].toInt(), args[2], args[3].toInt()).start()
    } else if (File("./config.txt").exists()) {
        println("使用文件内的配置进行转发")
        val config = Config().read()
        if (config.isNotEmpty()) {
            StreamForwarder(config[0], config[1].toInt(), config[2], config[3].toInt()).start()
        } else {
            println(
                "配置文件不存在\n" + "使用命令行启动一次则会自动创建配置文件"
            )
            return  // 直接退出
        }
    } else {
        println("请输入四个完整的参数")
        return  // 直接退出
    }
}