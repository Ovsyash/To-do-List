package com.ovsyannikov.to_dolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ovsyannikov.to_dolist.databinding.ListItemBinding
import com.ovsyannikov.to_dolist.item.Item

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val items: ArrayList<Item> = ArrayList()
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder, position: Int
    ) {
        val currentItem = items[position]
        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description

        holder.priorityImageView.visibility = if (
            currentItem.priority
        ) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Item>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): Item {
        return items[position]
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val bindingClass = ListItemBinding.bind(itemView)
        val titleTextView: TextView = bindingClass.titleTextView
        val descriptionTextView: TextView = bindingClass.descriptionTextView
        val priorityImageView: ImageView = bindingClass.priorityImageView

        init {
            itemView.setOnClickListener(View.OnClickListener {
                listener?.let { listener ->
                    val position: Int = adapterPosition
                    if (position in 0..itemCount) {
                        listener.onItemClick(items[position])
                    }
                }
            })
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(item: Item?)
    }
}