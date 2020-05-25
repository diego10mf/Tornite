package net.azarquiel.tornite.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.rowtorneo.view.*
import net.azarquiel.tornite.model.Torneo

/**
 * Created by diego10mf on 25/05/20.
 */
class CustomAdapter(val context: Context,
                    val layout: Int
                    ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var dataList: List<Torneo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setTorneos(torneos: List<Torneo>) {
        this.dataList = torneos
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Torneo){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            itemView.tvnombre.text = dataItem.titulo
            itemView.tvfecha.text = dataItem.fecha
            itemView.tvhorario.text = dataItem.comienzo
            itemView.tvpremio.text = dataItem.premio.toString()
            itemView.tag = dataItem

        }

    }
}