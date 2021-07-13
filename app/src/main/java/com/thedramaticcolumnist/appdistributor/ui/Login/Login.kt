package com.thedramaticcolumnist.appdistributor.ui.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.isValidText
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.databinding.LoginBinding
import com.thedramaticcolumnist.appdistributor.ui.HomeScreen
import com.thedramaticcolumnist.appdistributor.ui.SignUp.SignUp

class Login : AppCompatActivity(), View.OnClickListener {
    private lateinit var bind: LoginBinding
    private lateinit var auth: FirebaseAuth

    private val TAG: String = "LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = LoginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initAllComponents()
    }

    private fun initAllComponents() {
        auth = Firebase.auth
        bind.signUp.setOnClickListener(this)
        bind.login.setOnClickListener(this)
        bind.forgetPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signUp -> {
                intent = Intent(this@Login, SignUp::class.java)
                startActivity(intent)
            }
            R.id.login -> {
                if (isValidText(bind.email.text.toString().trim(),
                        bind.email) && isValidText(bind.password.text.toString().trim(),
                        bind.password)
                ) {
                    bind.progressBar.visibility = View.VISIBLE
                    auth.signInWithEmailAndPassword(bind.email.text.toString().trim(),
                        bind.password.text.toString().trim())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                bind.progressBar.visibility = View.GONE
                                intent = Intent(this@Login, HomeScreen::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                bind.progressBar.visibility = View.GONE
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, task.exception?.message,
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                }
            }
            R.id.forgetPassword -> {
                if (isValidText(bind.email.text.toString().trim(), bind.email)
                ) {
                    bind.progressBar.visibility = View.VISIBLE
                    auth.sendPasswordResetEmail(bind.email.text.toString().trim())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                bind.progressBar.visibility = View.GONE
                                mToast(this, "Reset link send successfully")
                            } else {
                                bind.progressBar.visibility = View.GONE
                                Toast.makeText(baseContext, task.exception?.message,
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}
