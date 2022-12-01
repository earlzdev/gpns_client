package com.earl.gpns.data

interface WebSocketService {

    companion object {
        // default
//        const val BASE_URL = "ws://10.0.2.2:8080/"
        // genymotion
        const val BASE_URL = "ws://10.0.3.2:8080/"
    }

    sealed class Endpoints(val url: String) {
        object Rooms: Endpoints("$BASE_URL/socketRooms")
        object Chat: Endpoints("$BASE_URL/chat")
        object Messaging: Endpoints("$BASE_URL/messaging")
    }
}