package forwarder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException


class PortForwarder(
    private val localhost: Any, private val localPort: Int, private val targetHost: String, private val targetPort: Int
) {
    private val maxConnectedClient: Int = 50

    fun start() = runBlocking {
        try {
            println("开始转发 $localhost:$localPort => $targetHost:$targetPort")
            println("最多允许${maxConnectedClient / 2}个客户端同时连接此线路")
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
                launch { forward(client, connection) }/*
                * 使用两个协程将数据交换
                *
                *         客户端  =>  代理服务器(协程1)  => 服务器
                *           ↑           (交换数据)          ↓
                *                <=  代理服务器(协程2)  <=
                * */
                println(
                    "${connection.inetAddress.hostAddress}:${connection.port} 已连接"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun forward(reader: Socket, writer: Socket) {
        val buffer = ByteArray(2048)
        try {
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
                    if (length == -1) break  // 退出协程释放内存
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
                    return   // 游戏客户端断开连接, 直接return退出循环
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    if (args.isNotEmpty() && args.size == 4) {
        Config().write(args)
        println(
            "已在config.txt中保存了本次的配置文件\n" + "可直接使用java -jar <JARFile>\n" + "不添加参数直接启动"
        )
        PortForwarder(args[0], args[1].toInt(), args[2], args[3].toInt()).start()
    } else if (File("./config.txt").exists()) {
        println("使用文件内的配置进行转发")
        val config = Config().read()
        if (config.isNotEmpty()) {
            PortForwarder(config[0], config[1].toInt(), config[2], config[3].toInt()).start()
        } else {
            println(
                "配置文件不存在\n" + "使用命令行启动一次则会自动创建配置文件"
            )
            return
        }
    } else {
        println("请输入四个完整的参数")
        return
    }
}