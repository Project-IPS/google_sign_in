package com.example.googlesign
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private lateinit var googleSignInClient: GoogleSignInClient
    var   serverClientId = "711249246810-2carbbcvvkn47nktpu6f3mae5r3rtj79.apps" +
    ".googleusercontent.com"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.signInButton)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

            .requestEmail()

            .build();
        // Build a GoogleSignInClient with the options specified
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        button.setOnClickListener {
            signIn()
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            task.addOnFailureListener { exception ->
                Log.d("SignIn",   exception.message.toString())
            }

            handleSignInResult(task)

        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, send token to server
            account?.let {
                // Get user details
                val email = account.email
                val displayName = account.displayName
                val photoUrl = account.photoUrl

                // Log user details
                Log.d("User Info", "Email: $email")
                Log.d("User Info", "Display Name: $displayName")
                Log.d("User Info", "Photo URL: $photoUrl")

                // If you want to display this information in the UI:
                // Update your UI components with the user details
                // For instance, if you have a TextView named userInfoTextView:
                // userInfoTextView.text = "Email: $email\nDisplay Name: $displayName"
            } ?: run {
                Log.e("handleSignInResult", "Google sign-in account is null")
            }


        } catch (e: ApiException) {
        // Sign in failed

            Log.e("handleSignInResult", "Google sign-in failed with exception: ${e.message}")
            e.printStackTrace()
        }
    }
    private fun signOut() {
        googleSignInClient.signOut()
    }


}