package com.example.lab3.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.R
import com.example.lab3.data.model.EventItem

class EventAdapter(
    private var items: List<EventItem>,
    private val onEditClick: (EventItem) -> Unit,
    private val onDeleteClick: (EventItem) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventVH>() {

    class EventVH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvEventTitle)
        val desc: TextView = view.findViewById(R.id.tvEventDesc)
        val time: TextView = view.findViewById(R.id.tvEventTime)
        val address: TextView = view.findViewById(R.id.tvEventAddress)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventVH(view)
    }

    override fun onBindViewHolder(holder: EventVH, position: Int) {
        val item = items[position]

        holder.title.text = item.title
        holder.desc.text = item.desc
        holder.time.text = item.time
        holder.address.text = item.appointmentType

        holder.btnEdit.setOnClickListener {
            onEditClick(item)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<EventItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}