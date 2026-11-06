package com.example.recallviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private var data: List<RecallMessage>) :
    RecyclerView.Adapter<MessageAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val sender: TextView = view.findViewById(R.id.tvSender)
        val content: TextView = view.findViewById(R.id.tvContent)
        val time: TextView = view.findViewById(R.id.tvTime)
        val recalled: TextView = view.findViewById(R.id.tvRecalled)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val msg = data[position]
        holder.sender.text = msg.sender
        holder.content.text = msg.content
        holder.time.text = SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date(msg.timestamp))
        holder.recalled.visibility = if (msg.isRecalled) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = data.size

    fun updateData(newData: List<RecallMessage>) {
        data = newData
        notifyDataSetChanged()
    }
}
