package com.octatech.bansosapp.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.octatech.bansosapp.R
import com.octatech.bansosapp.core.domain.model.Bansos
import com.octatech.bansosapp.databinding.ItemListBansosBinding
import java.util.ArrayList

class HomeBansosAdapter : RecyclerView.Adapter<HomeBansosAdapter.ListViewHolder>() {

    private var listData = ArrayList<Bansos>()
    var onItemClick: ((Bansos) -> Unit)? = null

    fun setData(newListData: List<Bansos>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_bansos, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListBansosBinding.bind(itemView)
        fun bind(data: Bansos) {
            with(binding) {
                val circularProgressDrawable = CircularProgressDrawable(itemView.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()
                Glide.with(itemView.context)
                    .load(data.bansosGambar)
                    .placeholder(circularProgressDrawable)
                    .into(cardListBansosImage)
                cardListBansosTitle.text = data.bansosName
                cardListBansosDesc.text = data.bansosDeskripsi
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}