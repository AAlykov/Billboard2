package com.tomsk.alykov.billboard.dialogs

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tomsk.alykov.billboard.R
import com.tomsk.alykov.billboard.act.EditAdsAct

class RcViewDialogSpinnerAdapter(var tvSelection: TextView, var dialog: AlertDialog): RecyclerView.Adapter<RcViewDialogSpinnerAdapter.SpViewHolder>() {

    val mainList = ArrayList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false)
        return SpViewHolder(view, tvSelection, dialog)
    }
    override fun onBindViewHolder(holder: SpViewHolder, position: Int) {
        holder.setData(mainList[position])

    }
    override fun getItemCount(): Int {
        return mainList.size
    }

    class SpViewHolder(itemView: View, var tvSelection: TextView, var dialog: AlertDialog) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var itemText = ""
        val tvSpItem = itemView.findViewById<TextView>(R.id.tvSpItem)

        fun setData(text: String) {
            tvSpItem.text = text
            itemText = text
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            //(context as EditAdsAct).rootElement.tvSelectCountry.text = itemText
            tvSelection.text = itemText
            dialog.dismiss()
            Log.d("AADebug", "onClick: class SpViewHolder")
            Log.d("AADebug", p0.toString())


        }
    }

    fun updateAdapter(list: ArrayList<String>) {
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()
    }
}