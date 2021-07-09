package aleksey.vasiliev.server

import aleksey.vasiliev.helpers.Coder
import aleksey.vasiliev.helpers.SharedLogic.ip
import aleksey.vasiliev.helpers.SharedLogic.port
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

/**
 * Тестовое приложение для Kauri. Представляет собой клиент-серверное приложение,
 * далее - серверная часть. Используется движок Netty, полученные данные в POST
 * выводятся в консоль.
 * @author <a href="mailto:enthusiastic.programmer@yandex.ru">Алексей Васильев</a>
 * @version 1.0
 */

fun main() {
    val coder = Coder()
    println("Server started.")
    embeddedServer(Netty, port = port, host = ip) {
        install(ContentNegotiation) {
            jackson()
        }
        routing {
            post("/") {
                val data = call.receive<Coder.TLVInstance>()
                call.response.status(HttpStatusCode.OK)
                val decodedData = coder.decode(data)
                println("Data received: ${decodedData.first}")
                println("Data length in bytes: ${decodedData.second}")
                println("Data type: ${decodedData.third}")
                println()
            }
        }
    }.start(wait = true)
    println("Server stopped.")
}