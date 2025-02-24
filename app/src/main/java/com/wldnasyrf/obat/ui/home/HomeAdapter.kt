package com.wldnasyrf.obat.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wldnasyrf.obat.R
import com.wldnasyrf.obat.data.response.Obat
import com.wldnasyrf.obat.databinding.ObatItemBinding
import com.wldnasyrf.obat.ui.create.CreateActivity

class HomeAdapter : ListAdapter<Obat, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    class HomeViewHolder(val binding: ObatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(obat: Obat) {
            binding.apply {
                tvObatNama.text = obat.nama
                tvObatHarga.text = root.context.getString(R.string.obat_harga_placeholder, obat.harga.toString())
                tvObatKategori.text = root.context.getString(R.string.obat_kategori_placeholder, obat.kategori)

                root.setOnClickListener {
                    val intent = Intent(root.context, CreateActivity::class.java).apply {
                        putExtra("EXTRA_OBAT_ID", obat.id)
                    }
                    root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ObatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Obat>() {
            override fun areItemsTheSame(oldItem: Obat, newItem: Obat): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Obat, newItem: Obat): Boolean {
                return oldItem == newItem
            }
        }
    }
}
