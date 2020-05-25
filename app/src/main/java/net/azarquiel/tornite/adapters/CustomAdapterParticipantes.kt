package net.azarquiel.tornite.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowparti.view.*
import net.azarquiel.tornite.model.Participante


/**
 * Created by diego10mf on 25/05/20.
 */
class CustomAdapterParticipantes(
    val context: Context,
    val layout: Int
                    ) : RecyclerView.Adapter<CustomAdapterParticipantes.ViewHolder>() {

    private var dataList: List<Participante> = emptyList()


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

    internal fun setParti(particips: List<Participante>) {
        this.dataList = particips
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Participante){

            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem

            itemView.tvnumpar.text = dataItem.nick
            itemView.tvequipo.text = dataItem.equipo


        }

    }
}