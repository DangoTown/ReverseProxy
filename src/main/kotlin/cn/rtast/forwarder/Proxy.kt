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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException


class Proxy(
    private val localhost: Any,
    private val localPort: Int,
    private val targetHost: String,
    private val targetPort: Int
) {

    fun start() = runBlocking {
        println("开始转发 $localhost:$localPort => $targetHost:$targetPort\n")
        val serverSocket = withContext(Dispatchers.IO) {
            ServerSocket(localPort)
        }
        while (true) {
            val connection = withContext(Dispatchers.IO) {
                serverSocket.accept()
            }
            val client = withContext(Dispatchers.IO) {
                Socket(targetHost, targetPort)
            }
            launch { forward(connection, client) }  // 启动两个协程
            launch { forward(client, connection) }
            println(
                "${connection.inetAddress.hostAddress}:${connection.port} 已连接"
            )
        }
    }

    private suspend fun forward(reader: Socket, writer: Socket) {
        val buffer = ByteArray(2048)
        val inputStream = withContext(Dispatchers.IO) {
            reader.getInputStream()
        }
        val outputStream = withContext(Dispatchers.IO) {
            writer.getOutputStream()
        }
        while (!reader.isClosed && !writer.isClosed) {
            try {
                val length = withContext(Dispatchers.IO) {
                    inputStream.read(buffer)
                }
                if (length == -1) break
                withContext(Dispatchers.IO) {
                    outputStream.write(buffer, 0, length)
                    outputStream.flush()
                }
            } catch (e: SocketException) {
                println("${writer.inetAddress} 断开连接...")
                withContext(Dispatchers.IO) {
                    reader.close()
                    writer.close()
                }
                return
            }
        }
    }
}
