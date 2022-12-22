package cn.rtast.forwarder

import java.io.File

class Config {
    fun read(): List<String> {
        val config: MutableList<String> = mutableListOf()
        val file = File("./config.txt")
        if (!file.exists()) {
            return listOf()
        }
        val content = file.readLines()
        content.forEach {
            config.add(it.replace("\\s".toRegex(), "").split(':')[1])
        }
        return config.toList()
    }

    fun write(config: Array<String>) {
        val file = File("./config.txt")
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.writeText(
            """localHost: ${config[0]}
                |localPort: ${config[1]}
                |remoteHost: ${config[2]}
                |remotePort: ${config[3]}
        """.trimMargin()
        )
    }
}