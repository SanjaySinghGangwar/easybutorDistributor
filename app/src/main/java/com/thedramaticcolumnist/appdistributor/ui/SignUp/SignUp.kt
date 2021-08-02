package com.thedramaticcolumnist.appdistributor.ui.SignUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.isValidText
import com.thedramaticcolumnist.appdistributor.databinding.SignUpBinding
import com.thedramaticcolumnist.appdistributor.ui.HomeScreen

class SignUp : AppCompatActivity() , View.OnClickListener {

    private lateinit var bind: SignUpBinding
    private lateinit var auth: FirebaseAuth

    private val TAG: String = "SIGN UP"
    var hashMap: HashMap<String, String> = HashMap()
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = SignUpBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initAllComponents()
        getToken()
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result
        })
    }

    private fun initAllComponents() {
        auth = Firebase.auth
        bind.signUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signUp -> {
                if (isValidText(bind.name.text.toString().trim(),
                        bind.email) && isValidText(bind.email.text.toString().trim(),
                        bind.email) && isValidText(bind.password.text.toString().trim(),
                        bind.password)
                ) {
                    hashMap["name"]=bind.name.text.toString().trim()
                    hashMap["token"]= token.toString()
                    bind.progressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(bind.email.text.toString().trim(),
                        bind.password.text.toString().trim())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                FirebaseDatabase.getInstance().reference.
                                child(applicationContext.getString(R.string.app_name))
                                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                    .setValue(hashMap)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "createUserWithEmail:success")
                                        bind.progressBar.visibility = View.GONE
                                        intent = Intent(this@SignUp, HomeScreen::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                // Sign in success, update UI with the signed-in user's information

                            } else {
                                bind.progressBar.visibility = View.GONE
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext,
                                    "Authentication failed." + task.exception?.message,
                                    Toast.LENGTH_SHORT).show()

                            }
                        }
                }
            }
        }
    }
}