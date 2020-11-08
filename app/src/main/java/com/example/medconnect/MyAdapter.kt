package com.example.medconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medconnect.room.MessageEntity

class MyAdapter(val data:List<MessageEntity>):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_row,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val textView=holder.view.findViewById<TextView>(R.id.medicineInfo)
        textView.text=data.get(position).message
    }

    override fun getItemCount()=data.size
}