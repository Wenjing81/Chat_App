package com.r.chat_app

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.r.chat_app.MainActivity.Companion.CURRENTUSER
import kotlinx.android.synthetic.main.activity_chat_page.*

class ChatPageActivity : AppCompatActivity() {

    private lateinit var adapter: MessageAdapter
    private lateinit var messageRef: CollectionReference
    private var messageList = mutableListOf<ChatLine>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        val username = intent?.getStringExtra(CURRENTUSER)
        //prepareTestData()
        initDataBase()
        initListener()
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        getChatListData()
    }

    private fun initDataBase() {

        val db = Firebase.firestore
        messageRef = db
            .collection("Messages").document("A-B")
            .collection("chatRecord")
    }

    private fun getChatListData() {
        messageRef
            .get()
            .addOnSuccessListener { result ->
                messageList.clear()
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                    val it = document.data["text_message"] as String
                    messageList.add(ChatLine(it))
                }

                if (recyclerView.adapter != null) {
                    (recyclerView.adapter as MessageAdapter).updateDataList()
                    Log.d("TAG", "adapter is not null now!!! ohohoh")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListener() {

        send_button.setOnClickListener {
            val message = send_message_area.text.toString()
            if (message.isBlank()) {
                return@setOnClickListener
            }

            val x = messageRef.document("chatLine${System.currentTimeMillis()}")
            //Add the information to Firestore from the "send" button.
            val chatLine = ChatLine(message)
            x.set(chatLine, SetOptions.merge())
                .addOnSuccessListener { logMaker("DocumentSnapshot successfully written") }
                .addOnFailureListener { exception -> logMaker("Error writing document, $exception") }

            toastMaker("Successful sending message $message")
            send_message_area.setText("")
            getChatListData()
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = MessageAdapter(messageList)
        recyclerView.adapter = adapter
    }

    private fun toastMaker(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun logMaker(text: String) {
        Log.d("TAG", text)
    }

    private fun prepareTestData() {
        messageList.add(ChatLine("hi1"))
        messageList.add(ChatLine("hi2"))
        messageList.add(ChatLine("hi3"))
        messageList.add(ChatLine("hi4"))
        messageList.add(ChatLine("hi5"))
        messageList.add(ChatLine("hi6"))
        messageList.add(ChatLine("hi7"))
        messageList.add(ChatLine("hi8"))
        messageList.add(ChatLine("hi9"))
        messageList.add(ChatLine("hi10"))
        messageList.add(ChatLine("hi11"))
        messageList.add(ChatLine("hi12"))
        messageList.add(ChatLine("hi13"))
        messageList.add(ChatLine("hi14"))
        messageList.add(ChatLine("hi15"))
        messageList.add(ChatLine("hi16"))
    }
}