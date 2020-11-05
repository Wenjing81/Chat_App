package com.r.chat_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.r.chat_app.MainActivity.Companion.CURRENTUSER
import kotlinx.android.synthetic.main.activity_chat_page.*

class ChatPageActivity : AppCompatActivity() {

    private lateinit var adapter: MessageAdapter
    private lateinit var messageRef: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        val username = intent?.getStringExtra(CURRENTUSER)

        initDataBase()
        initListener()

    }


    private fun initDataBase() {

            //get db from FirebaseFirestore

        val db = Firebase.firestore
        messageRef = db
            .collection("Messages").document("A-B")
            .collection("chatRecord")
    }

    private fun initListener() {

        send_button.setOnClickListener {
            val message = send_message_area.text.toString()
            if (message.isBlank()) {
                return@setOnClickListener
            }
            val x = messageRef.document("chatLine")
            //Add the information to Firestore from the "send" button.
            val chatLine = ChatLine(message)
            x.set(chatLine, SetOptions.merge())
                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.d("TAG", "Error writing document", e) }

            Toast.makeText(this, "Successful sending message $message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initDocumentName() {

    }
}