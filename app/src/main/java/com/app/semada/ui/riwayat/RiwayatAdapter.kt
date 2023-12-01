package com.app.semada.ui.riwayat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.semada.R

class RiwayatAdapter (private val riwayatList: List<RiwayatItem>) :
    RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusTextView: TextView = itemView.findViewById(R.id.riwayat_status)
        val tanggalTextView: TextView = itemView.findViewById(R.id.riwayat_tanggal)
        val cardView: CardView = itemView.findViewById(R.id.riwayat_CardView)
        val statusImageView: ImageView = itemView.findViewById(R.id.riwayat_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_riwayat, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = riwayatList[position]

        holder.statusTextView.text = currentItem.status
        holder.tanggalTextView.text = currentItem.tanggal

        if (currentItem.status.equals("sakit", ignoreCase = true)) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.jam_masuk))
            holder.statusImageView.setImageResource(R.drawable.riwayat_sakit)

        } else if (currentItem.status.equals("izin", ignoreCase = true)){
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.jam_masuk))
            holder.statusImageView.setImageResource(R.drawable.riwayat_ijin)

        } else if (currentItem.status.equals("alpha", ignoreCase = true)){
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red_theme))
            holder.statusImageView.setImageResource(R.drawable.riwayat_alpa)
        }
    }

    override fun getItemCount() = riwayatList.size
}