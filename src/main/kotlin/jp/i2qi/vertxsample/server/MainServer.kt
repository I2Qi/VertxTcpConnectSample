package jp.i2qi.vertxsample.server

import io.vertx.core.Vertx

/**
 * Created by Itsuki Hatori on 2017/10/17.
 */

    fun main(args: Array<String>) {
        val vertx: Vertx = Vertx.vertx()
        vertx.deployVerticle(ListenVerticle())
        vertx.deployVerticle(ConnectVerticle())
        vertx.deployVerticle(RestServerVerticle())
    }
