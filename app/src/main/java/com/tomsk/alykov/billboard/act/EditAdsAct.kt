package com.tomsk.alykov.billboard.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.tomsk.alykov.billboard.R
import com.tomsk.alykov.billboard.databinding.ActivityEditAdsBinding
import com.tomsk.alykov.billboard.dialogs.DialogSpinnerHelper
import com.tomsk.alykov.billboard.utils.CityHelper

class EditAdsAct : AppCompatActivity() {


    private lateinit var rootElement: ActivityEditAdsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        //setContentView(R.layout.activity_edit_ads)

        val listCountry = CityHelper.getAllCountries(this)
        val dialog = DialogSpinnerHelper()
        dialog.showSpinnerDialog(this, listCountry)

        /*
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, CityHelper.getAllCountries(this))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootElement.spCountry.adapter = adapter

        val cityNames = CityHelper.getAllCountries(this)
        val spinner = rootElement.spinner
        val adapter2 = ArrayAdapter(this,android.R.layout.simple_spinner_item, cityNames)
        spinner.adapter = adapter2 */


    }
}