package com.example.planktracker

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter


class PlankAdapter(data: OrderedRealmCollection<Plank>):
    RealmRecyclerViewAdapter<Plank, PlankAdapter.ViewHolder>(data, true){

    init {
        setHasStableIds(true)
    }

    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell) {
        val date: TextView = cell.findViewById(android.R.id.text1)
        val sec: TextView = cell.findViewById(android.R.id.text2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plank: Plank? = getItem(position)
        holder.date.text = DateFormat.format("yyyy/MM/dd HH:mm", plank?.date)
        holder.sec.text = plank?.sec.toString()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: 0
    }

}