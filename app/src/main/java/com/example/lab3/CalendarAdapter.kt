package com.example.lab3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private var items: List<DayCell>,
    private val onDayClick: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayVH>() {

    class DayVH(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = view.findViewById(R.id.tvDay)
        val dot: View = view.findViewById(R.id.dot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_cell, parent, false)
        return DayVH(view)
    }

    override fun onBindViewHolder(holder: DayVH, position: Int) {
        val cell = items[position]

        if (cell.dayNumber == null) {
            holder.tvDay.text = ""
            holder.tvDay.background = null
            holder.dot.visibility = View.GONE
            holder.itemView.isClickable = false
            holder.itemView.alpha = 0.3f
            holder.itemView.setOnClickListener(null)
            return
        }

        holder.itemView.alpha = 1f
        holder.itemView.isClickable = true
        holder.tvDay.text = cell.dayNumber.toString()

        holder.tvDay.background = if (cell.isSelected) {
            ContextCompat.getDrawable(holder.tvDay.context, R.drawable.bg_day_selected)
        } else {
            null
        }

        holder.tvDay.setTextColor(
            ContextCompat.getColor(
                holder.tvDay.context,
                if (cell.isSelected) R.color.on_selected else R.color.text
            )
        )

        holder.dot.visibility = if (cell.hasEvents) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            onDayClick(cell.dayNumber)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<DayCell>) {
        items = newItems
        notifyDataSetChanged()
    }
}