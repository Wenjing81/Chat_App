package com.r.chat_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
        initFirebaseToken()

    }

    override fun onStart() {
        super.onStart()
    }

    private fun initFirebaseAuth(){
        auth = Firebase.auth
    }

    private fun initListener(){
        sign_up_button.setOnClickListener {
            val userName=user_email_address.text.toString()
            //CheckUsernameValid()
            val password=user_password.text.toString()
            //CheckPasswordValid()

            // Check if user is signed in (non-null) and update UI accordingly.
            val currentUser = auth.currentUser
            if (currentUser != null){
                //Todo: 1.force him to log out
                Firebase.auth.signOut()
                Toast.makeText(this,"logout current user, ${currentUser.email}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(userName,password).addOnCompleteListener(this){
                if(it.isSuccessful){
                    //successful signin or create a new user!
                    Toast.makeText(this,"You succeed to create a user:$userName",Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    Toast.makeText(this,"456 ${user?.email}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ChatPageActivity::class.java)
                    intent.putExtra(CURRENTUSER,user)
                    startActivity(intent)

                }else{
                    Log.d("!!!","createUserWithEmail:failure")
                    Toast.makeText(this,"Authentication failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        login_button.setOnClickListener{
            val userName=user_email_address.text.toString()
            //CheckUsernameValid()
            val password=user_password.text.toString()
            //CheckPasswordValid()

            auth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // successful log in, with the signed-in user's information
                        Log.d("sss", "logInWithEmail:success $userName")
                        val user = auth.currentUser
                        Toast.makeText(baseContext, "logInWithEmail123:success ${user?.email}",
                            Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ChatPageActivity::class.java)
                        intent.putExtra(CURRENTUSER,user)
                        startActivity(intent)
                    } else {
                        // If log in fails, display a message to the user.
                        Log.w("!!!", "logInWithEmail:failure")
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    private fun initFirebaseToken(){
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
companion object{
    const val CURRENTUSER="Currentuser"
}
}



