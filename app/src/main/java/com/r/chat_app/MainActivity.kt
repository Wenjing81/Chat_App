package com.r.chat_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

            val userEmailAddress = user_email_address.text.toString().trim()
            val resultEmail = checkUsernameValid(userEmailAddress)
            if (!resultEmail) {
                toastMaker("Your userName has no @")
                return@setOnClickListener
            }
            val password = user_password.text.toString().trim()
            val resultPassword = checkPasswordValid(password)
            if (!resultPassword) {
                toastMaker("Your password must be at least 6 characters.")
                return@setOnClickListener
            }

            /* read in user Email address and password to the firebase*/
            auth.createUserWithEmailAndPassword(userEmailAddress, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        //successful signin or create a new user!
                        toastMaker("Successful SignUp with:$userEmailAddress")
                        addUserToDB()
                        val current_user = auth.currentUser!!
                        startNextActivity(current_user.email!!)
                    } else {
                        val x = "Failed to SignUp with:$userEmailAddress!"
                        toastMaker(x)
                        logMaker(x)
                    }
                }
                .addOnFailureListener {
                    logMaker("Failed to SignUp($it)")
                }
        }

        /* Button 2:Log in */
        login_button.setOnClickListener {

            val userEmailAddress = user_email_address.text.toString().trim()
            val result_email = checkUsernameValid(userEmailAddress)
            if (!result_email) {
                toastMaker("Your userName has no @")
                return@setOnClickListener
            }

            val password = user_password.text.toString().trim()
            val result_password = checkPasswordValid(password)
            if (!result_password) {
                toastMaker("Your password must be at least 6 characters.")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(userEmailAddress, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // successful log in, with the signed-in user's information
                        val user = auth.currentUser
                        toastMaker("Successful Login with:${user?.email}")

                        val intent = Intent(this, ChatPageActivity::class.java)
                        intent.putExtra(CURRENTUSER, user)
                        startActivity(intent)
                    } else {
                        toastMaker("Failed to login")
                    }
                }
                .addOnFailureListener { exception ->
                    logMaker("Failed to login($exception)")
                }
        }
    }

    private fun checkUsernameValid(userEmailAddress: String): Boolean {
        if (userEmailAddress.contains("@")) {
            return true
        }
        return false
    }

    private fun checkPasswordValid(password: String): Boolean {
        if (password.length >= 6) {
            return true
        }
        return false
    }

    private fun toastMaker(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun logMaker(text: String) {
        Log.d(CHAT_TAG, text)
    }

    private fun startNextActivity(current_user: String) {
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
                logMaker("successful to add user to DB")
            }
            .addOnFailureListener {
                logMaker("failed to add a user.$it")
            }
    }

    companion object {
        const val CURRENTUSER = "Currentuser"
        const val CHAT_TAG = "chat_app_log"
    }
}



