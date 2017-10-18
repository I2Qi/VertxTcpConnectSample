package jp.i2qi.vertxsample.server

import com.sun.beans.TypeResolver
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.net.NetServer
import io.vertx.core.net.NetSocket
import io.vertx.ext.web.Router


open class ListenVerticle() : AbstractVerticle() {


    override fun start() {
        val netserver: NetServer =
                vertx.createNetServer().connectHandler {
                    println(this.javaClass.name + "Connected!")
                    it.write("fugafugafuga")
                    vertx.eventBus().consumer("verticle.listen",{ message : Message<String> -> it.write(message.body()) })

                }.listen(18080, "localhost", {

                    if (it.succeeded()) {
                        println(this.javaClass.name + "listening...")
                    } else {
                        println(this.javaClass.name + "failed!")
                    }
                })

    }


}

open class ConnectVerticle() : AbstractVerticle() {
    override fun start() {

        vertx.createNetClient().connect(18080, "localhost", { event ->
            var socket: NetSocket = event.result()
            socket.handler({ it ->
                println(this.javaClass.name + it)
            })
        })
    }
}

class RestServerVerticle : AbstractVerticle() {

    override fun start() {
        var router = Router.router(vertx)
        router.route("/rest/hoge").handler { event ->
            event.request().bodyHandler { buf ->
                println(buf)
            }
            vertx.eventBus().send("verticle.listen" , "event message!" )
            var response = event.response()
            response.putHeader("content-type", "text/html")
                    .end("<h1> fugafugafugafuga </h1>")

        }
        vertx.createHttpServer().requestHandler(router::accept).listen(8080)
    }
}

    fun main(args: Array<String>) {
        val vertx: Vertx = Vertx.vertx()
        vertx.deployVerticle(ListenVerticle())
        vertx.deployVerticle(ConnectVerticle())
        vertx.deployVerticle(RestServerVerticle())
    }
