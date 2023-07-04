package com.example.tugasreal

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import java.text.NumberFormat

class ExchangeAdapter(private val mList: List<Exchange>) :
    RecyclerView.Adapter<ExchangeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaksi, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val ItemsViewModel = mList[position]
        /*holder.image.setImageResource(ItemsViewModel.type)*/
        holder.title.text  = ItemsViewModel.type
        holder.amount.text = if (ItemsViewModel.type == "income") "+RP. ${formatter.format(ItemsViewModel.amount)}" else "-RP. ${formatter.format(ItemsViewModel.amount)}"
        holder.date.text   = ItemsViewModel.date

        holder.itemView.setOnClickListener {
            val intent:Intent = Intent(it.context, DetailTransaksi::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("exchangeId", ItemsViewModel.id)
            intent.putExtra("amount", ItemsViewModel.amount)
            intent.putExtra("date", ItemsViewModel.date)
            intent.putExtra("desc", ItemsViewModel.desc)
            intent.putExtra("category", ItemsViewModel.category)
            intent.putExtra("type", ItemsViewModel.type)
            startActivity(it.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        val image: ImageView = itemView.findViewById(R.id.exchangeItemImage)
        val title:TextView   = itemView.findViewById(R.id.exchangeItemTitle)
        val amount:TextView  = itemView.findViewById(R.id.exchageItemAmount)
        val date:TextView    = itemView.findViewById(R.id.exchangeItemDate)
    }
}