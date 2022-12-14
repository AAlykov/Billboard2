package com.tomsk.alykov.billboard.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomsk.alykov.billboard.R
import com.tomsk.alykov.billboard.utils.CityHelper

class DialogSpinnerHelper {
    fun showSpinnerDialog(context:Context, list:ArrayList<String>) {
        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null)
        val adapter = RcViewDialogSpinner()
        val rcView = rootView.findViewById<RecyclerView>(R.id.rcSpView)

        //var searchView: SearchView = rootView.findViewById(R.id.sv1)
        val svView = rootView.findViewById<SearchView>(R.id.svSpinner)

        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
        adapter.updateAdapter(list)

        setSearchViewListener(adapter, list, svView)

        builder.setView(rootView)
        builder.show()
    }

    private fun setSearchViewListener(adapter: RcViewDialogSpinner, list: ArrayList<String>, svView: SearchView?) {
        svView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val tempList = CityHelper.filterListData(list, newText)
                adapter.updateAdapter(tempList)
                return true
            }
        })

    }

}