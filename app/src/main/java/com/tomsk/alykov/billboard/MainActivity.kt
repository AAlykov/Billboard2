package com.tomsk.alykov.billboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tomsk.alykov.billboard.act.EditAdsAct
import com.tomsk.alykov.billboard.databinding.ActivityMainBinding
import com.tomsk.alykov.billboard.dialoghelper.DialogConst
import com.tomsk.alykov.billboard.dialoghelper.DialogHelper
import com.tomsk.alykov.billboard.dialoghelper.GoogleAccConst
import com.tomsk.alykov.billboard.dialoghelper.GoogleAccConst.TAG


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var tvAccountEmail: TextView

    private lateinit var rootElement: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: Start")
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        //setContentView(R.layout.activity_main)
        setContentView(view)

        init()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE) {
            //Log.d(TAG, "if (requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!) //!! - мы взяли отвкественность за нулл выше if (account != null)
                }

            } catch (e: ApiException) {
                Log.d(TAG, "onActivityResult: ${e.message}")
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }




    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun init() {
        setSupportActionBar(rootElement.mainContent.toolbar)
        var toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout, rootElement.mainContent.toolbar, R.string.open, R.string.close)
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener(this)
        tvAccountEmail = rootElement.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
    }

    fun uiUpdate(user: FirebaseUser?) { //может быть null

        //val acct = GoogleSignIn.getLastSignedInAccount(this)
        //Toast.makeText(this, "Sign id done email is: " + acct?.email, Toast.LENGTH_LONG).show()

        tvAccountEmail.text = if (user == null) {
            resources.getString(R.string.not_reg)
        } else {
            //Log.d(TAG, "uiUpdate: ${user}")
            //Log.d(TAG, "uiUpdate: ${user.email}")
            //Log.d(TAG, "uiUpdate: ${acct?.email}")
            //Log.d(TAG, "uiUpdate: ${user.email.toString()}")
            user.email//.toString()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show()
        when(item.itemId) {
            R.id.id_my_ads -> {
            }
            R.id.id_car -> {
            }
            R.id.id_pc -> {
            }

            R.id.id_sign_up -> {
                dialogHelper.createSignDialog(DialogConst.SING_UP_STATE)
            }
            R.id.id_sign_in -> {
                dialogHelper.createSignDialog(DialogConst.SING_IN_STATE)
            }
            R.id.id_sign_out -> {
                uiUpdate(null)
                mAuth.signOut()
                dialogHelper.accHelper.signOutGoogle()
            }


        }
        rootElement.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_new_ads) {
            val i = Intent(this, EditAdsAct::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }



}