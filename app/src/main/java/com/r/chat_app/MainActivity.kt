package com.r.chat_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initial the firebase authentication
        initFirebaseAuth()
        //initial the two buttons,Sign up or Log in?
        initListener()
        //send messages from firebase
        //initFirebaseToken()
        //addUserToDB()
    }

    /*Activity begin to "onStart" */
    override fun onStart() {
        super.onStart()
        /* Check if user is signed in, if yes, force to sign out.*/
        val currentUser = auth.currentUser
        if (currentUser != null) {
            auth.signOut()
        }
    }


    /*Initial firebase authentication*/
    private fun initFirebaseAuth() {
        auth = Firebase.auth
    }

    /* Two buttons to go - Sign up or Log in */
    private fun initListener() {

        /* Button 1:Sign up */

        sign_up_button.setOnClickListener {
            val userEmailAddress = user_email_address.text.toString()
            CheckUsernameValid(userEmailAddress)
            val password = user_password.text.toString()
            CheckPasswordValid(password)

            /* read in user Email address and password to the firebase*/
            auth.createUserWithEmailAndPassword(userEmailAddress, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        //successful signin or create a new user!
                        Toast.makeText(
                            this,
                            "Successful registering with $userEmailAddress",
                            Toast.LENGTH_SHORT
                        ).show()
                        val current_user = auth.currentUser!!
                        StartNextActivity(current_user.email!!)

                    } else {
                        Log.d("!!!", "Failed to register with $userEmailAddress")
                        Toast.makeText(
                            this,
                            "Failed to register with $userEmailAddress!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }


        /* Button 2:Log in */






        login_button.setOnClickListener {

            val userEmailAddress = user_email_address.text.toString()
            //CheckUsernameValid(userEmailAddress)
            val password = user_password.text.toString()
            //CheckPasswordValid()

            auth.signInWithEmailAndPassword(userEmailAddress, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // successful log in, with the signed-in user's information
                        Log.d("sss", "logInWithEmail:success $userEmailAddress")
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext, "logInWithEmail123:success ${user?.email}",
                            Toast.LENGTH_SHORT
                        ).show()


                        val intent = Intent(this, ChatPageActivity::class.java)
                        intent.putExtra(CURRENTUSER, user)
                        startActivity(intent)
                    } else {
                        // If log in fails, display a message to the user.
                        Log.d("TAG", "注册失败！")
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "Error 注册出问题啦", exception)
                }
        }
    }

    private fun CheckUsernameValid(userEmailAddress: String) {
        if (!userEmailAddress.contains("@")) {
            Toast.makeText(this, "Your userName has no @", Toast.LENGTH_SHORT).show()
        }
    }

    private fun CheckPasswordValid(password: String) {
        if (!(password.length < 6)) {
            Toast.makeText(
                this,
                "Your password must be more than 6 characters.",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun StartNextActivity(current_user: String) {
        val intent = Intent(this, ChatPageActivity::class.java)
        intent.putExtra(CURRENTUSER, current_user)
        startActivity(intent)
    }

    private fun addUserToDB() {
        val db = Firebase.firestore
        val email = auth.currentUser?.email!!
        val it = Email(email)
        db.collection("AllUserCollection").document(email).set(it)
            .addOnSuccessListener {
                Log.d("!!!", "successful to add user to DB")
            }
            .addOnFailureListener {
                Log.d("!!!", "failed to add a user.$it")
            }
    }

    private fun initFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {

            if (!it.isSuccessful) {
                Log.w("TAG1", "Fetching FCM registration token failed", it.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = it.result

            // Log and toast

            Toast.makeText(baseContext, "$token", Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        const val CURRENTUSER = "Currentuser"
    }
}



