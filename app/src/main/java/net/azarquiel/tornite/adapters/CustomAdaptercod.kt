package net.azarquiel.tornite.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowcod.view.*
import net.azarquiel.tornite.model.Cod




/**
 * Created by diego10mf on 25/05/20.
 */
class CustomAdaptercod(val context: Context,
                       val layout: Int
                    ) : RecyclerView.Adapter<CustomAdaptercod.ViewHolder>() {

    private var dataList: List<Cod> = emptyList()

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

    internal fun setCodigos(codigos: ArrayList<Cod>) {
        this.dataList = codigos
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Cod){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            itemView.tvnumpar.text = dataItem.partida
            itemView.tvcod.text = dataItem.codigo

            itemView.tag = dataItem

        }

    }
}