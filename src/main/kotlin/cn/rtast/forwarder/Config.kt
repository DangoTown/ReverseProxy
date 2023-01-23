/*
 * Copyright 2023 RTAkland
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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