package com.r.chat_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MessageAdapter(
    private var messageList: MutableList<ChatLine>
) : RecyclerView.Adapter<MessageAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var oneMessage: TextView = view.findViewById(R.id.text_view_messages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.messages, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val messages = messageList[position]

        holder.apply {
            oneMessage.text = messages.text_message
        }
    }

    override fun getItemCount(): Int = messageList.size

    fun updateDataList() {
        notifyDataSetChanged()
    }
}