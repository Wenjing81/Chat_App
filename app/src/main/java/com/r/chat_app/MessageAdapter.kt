package com.r.chat_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val text_view_messages: TextView =view.findViewById(R.id.text_view_messages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.messages,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



       // val messages =
       //     holder.text_view_messages.text=
    }

    override fun getItemCount(): Int = 0

}