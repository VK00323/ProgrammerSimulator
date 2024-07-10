package com.example.programmergame.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("AAAAA", "onMessageReceived")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("AAAAA", "onNewToken")
    }
}