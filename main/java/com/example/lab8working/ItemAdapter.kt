package com.example.lab8working

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ItemAdapter(
    private var items: List<Item>,
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        val itemDescriptionTextView: TextView = itemView.findViewById(R.id.itemDescriptionTextView)
        val itemCategoryTextView: TextView = itemView.findViewById(R.id.itemCategoryTextView)
        val itemTimeTextView: TextView = itemView.findViewById(R.id.itemTimeTextView)
        val itemDistanceTextView: TextView = itemView.findViewById(R.id.itemDistanceTextView)
        val editImageView: ImageButton = itemView.findViewById(R.id.editImageView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]
        holder.itemNameTextView.text = currentItem.name
        holder.itemDescriptionTextView.text = currentItem.description
        holder.itemCategoryTextView.text = currentItem.category // Add this
        holder.itemTimeTextView.text = currentItem.time // Add this
        holder.itemDistanceTextView.text = currentItem.distance.toString() // Add this
        holder.editImageView.setOnClickListener {
            val context = holder.itemView.context // Get context from itemView
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("itemId", currentItem.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size
}
