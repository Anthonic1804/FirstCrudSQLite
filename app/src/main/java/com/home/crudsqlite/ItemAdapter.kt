package com.home.crudsqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    val context: Context,
    val items: ArrayList<EmpModelClass>
) : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.MyViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.items_rows, parent, false)
        return MyViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ItemAdapter.MyViewHolder, position: Int) {
        val item =items.get(position)

        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        if(position % 2 == 0){
            holder.container.setBackgroundColor(
                ContextCompat.getColor(
                    context, R.color.lightgray
                )
            )
        }else{
            holder.container.setBackgroundColor(
                ContextCompat.getColor(
                    context, R.color.white
                )
            )
        }

        holder.ivEdit.setOnClickListener {
            if(context is MainActivity){
                context.updateRecordDialog(item)
            }
        }

        holder.ivDelete.setOnClickListener {
            if(context is MainActivity){
                context.deleteRecordDialog(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        internal var tvName: TextView
        internal var tvEmail: TextView
        internal var ivEdit : ImageView
        internal var ivDelete: ImageView
        internal var container: LinearLayout


        init {
            tvName = itemView.findViewById(R.id.tvNameList)
            tvEmail = itemView.findViewById(R.id.tvEmailList)
            ivEdit = itemView.findViewById(R.id.ivEdit)
            ivDelete = itemView.findViewById(R.id.ivDelete)
            container = itemView.findViewById(R.id.lyMain)
        }
    }
}
