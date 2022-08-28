package com.tomsk.alykov.billboard.dialoghelper

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.tomsk.alykov.billboard.MainActivity
import com.tomsk.alykov.billboard.R
import com.tomsk.alykov.billboard.accounthelper.AccountHelper
import com.tomsk.alykov.billboard.databinding.SignDialogBinding

class DialogHelper(act:MainActivity) {
    private val act = act
    val accHelper = AccountHelper(act) //private val accHelper = AccountHelper(act)

    fun createSignDialog(index:Int) { //по умолчанию функция будет паблик
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root

        builder.setView(view)

       setDialogState(index, rootDialogElement)

        val dialog = builder.create()
        rootDialogElement.btSignUpIn.setOnClickListener {
            setOnClickSignUpIn(index, rootDialogElement, dialog)
            }

        rootDialogElement.btForgetPass.setOnClickListener {
            setOnResetPassword(rootDialogElement, dialog)
        }

        rootDialogElement.btGoogleSignIn.setOnClickListener {
            accHelper.signInWithGoogle()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setOnResetPassword(rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        if (rootDialogElement.edSignEmail.text.isNotEmpty()) {
            act.mAuth.sendPasswordResetEmail(rootDialogElement.edSignEmail.text.toString()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act, "Письмо было отправлено", Toast.LENGTH_LONG ).show()
                }
            }
            dialog?.dismiss()
        } else
            Toast.makeText(act, "Укажите ваш email", Toast.LENGTH_LONG ).show()
    }

    private fun setOnClickSignUpIn(index: Int, rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        dialog?.dismiss()
        if (index == DialogConst.SING_UP_STATE) {
            accHelper.signUpWithEmail(rootDialogElement.edSignEmail.text.toString(),rootDialogElement.edSignPassword.text.toString())
        } else {
            accHelper.signInWithEmail(rootDialogElement.edSignEmail.text.toString(),rootDialogElement.edSignPassword.text.toString())
        }
    }

    private fun setDialogState(index: Int, rootDialogElement: SignDialogBinding) {
        if (index == DialogConst.SING_UP_STATE) {
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_up)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
        } else { //SING_UP_STATE
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_in)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
            rootDialogElement.btForgetPass.visibility = View.VISIBLE
        }
    }
} 