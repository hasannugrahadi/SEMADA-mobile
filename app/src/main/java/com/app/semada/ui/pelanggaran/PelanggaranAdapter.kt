package com.app.semada.ui.pelanggaran

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.semada.R

class PelanggaranAdapter (private val pelanggaranList: List<PelanggaranItem>) :
    RecyclerView.Adapter<PelanggaranAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tanggalTextView: TextView = itemView.findViewById(R.id.pelanggaran_tanggal)
        val namaTextView: TextView = itemView.findViewById(R.id.pelanggaran_nama)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_pelanggaran, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = pelanggaranList[position]

        holder.tanggalTextView.text = currentItem.tanggal
        holder.namaTextView.text = currentItem.nama

    }

    override fun getItemCount() = pelanggaranList.size
}