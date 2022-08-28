package com.tomsk.alykov.billboard.accounthelper

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.tomsk.alykov.billboard.MainActivity
import com.tomsk.alykov.billboard.R
import com.tomsk.alykov.billboard.constants.FirebaseAuthConstants
import com.tomsk.alykov.billboard.dialoghelper.GoogleAccConst
import com.tomsk.alykov.billboard.dialoghelper.GoogleAccConst.TAG

class AccountHelper(act:MainActivity) {

    private val act = act
    //val signInRequestCode = 123
    private lateinit var googleSignInClient: GoogleSignInClient

    fun signUpWithEmail(email:String, password:String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task->


                if (task.isSuccessful) {
                    sendEmailVerification(task.result?.user!!)
                    act.uiUpdate(task.result.user)
                } else {
                    Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                    //Log.d(TAG, "signUpWithEmail: Exception ${task.exception}")
                    if (task.exception is FirebaseAuthUserCollisionException) {//через is проверяем пришедший класс похож на этот?
                        val exception = task.exception as FirebaseAuthUserCollisionException
                        //Log.d(TAG, "signUpWithEmail: Exception ${exception.errorCode}")
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                            //тут будем соединять аккаунты
                            //Toast.makeText(act, FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE, Toast.LENGTH_LONG).show()
                            linkEmailToG(email, password)
                        }
                    } else if (task.exception is FirebaseAuthInvalidCredentialsException) {//через is проверяем пришедший класс похож на этот?
                        val exception = task.exception as FirebaseAuthInvalidCredentialsException
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                        }
                    }


                }


            }
        }
    }

    private fun linkEmailToG(email:String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        if (act.mAuth.currentUser != null) {
            act.mAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act, act.resources.getString(R.string.link_done), Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(act, "У вас уже есть аккаунт с этим Email адресом. Входите по нему.", Toast.LENGTH_LONG).show()
        }



    }

    fun signInWithEmail(email:String, password:String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                if (task.isSuccessful) {
                    //sendEmailVerification(task.result?.user!!)
                    act.uiUpdate(task.result.user)
                } else {
                    //Toast.makeText(act, act.resources.getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()

                        if (task.exception is FirebaseAuthInvalidCredentialsException) {//через is проверяем пришедший класс похож на этот?
                            val exception = task.exception as FirebaseAuthInvalidCredentialsException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                                Toast.makeText(act, FirebaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                            } else  if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_PASSWORD) {
                                Toast.makeText(act, FirebaseAuthConstants.ERROR_INVALID_PASSWORD, Toast.LENGTH_LONG).show()
                                //Log.d(TAG, "signInWithEmail: ${task.exception}")
                            }
                        }

                }
            }
        }
    }

    private fun sendEmailVerification(user:FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task->
            if (task.isSuccessful) {
                Toast.makeText(act, act.resources.getString(R.string.send_verification_email_done), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(act, act.resources.getString(R.string.send_verification_email_error), Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun getSignInClient():GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(act.getString(R.string.ac_sign_in)) //специальная строка, отправляем ее системе android
                .requestEmail()
                .build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun signInWithGoogle() {
        googleSignInClient = getSignInClient()
        val intent = googleSignInClient.signInIntent
        act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signOutGoogle() {
        googleSignInClient = getSignInClient()
        googleSignInClient.signOut()
    }

    fun signInFirebaseWithGoogle(token:String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener { task->
            if (task.isSuccessful) {
                Toast.makeText(act, "Sign in done", Toast.LENGTH_LONG).show()
                act.uiUpdate(task.result?.user)
            } else {
                Log.d(TAG, "signInFirebaseWithGoogle: ${task.exception} ")
            }
        }
    }
}