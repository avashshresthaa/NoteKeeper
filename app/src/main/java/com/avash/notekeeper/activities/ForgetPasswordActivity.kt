package com.avash.notekeeper.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.avash.notekeeper.R
import com.avash.notekeeper.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

    }
    private fun setupActionBar() {
        val tol = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_forgot_password_activity)
        setSupportActionBar(tol)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        tol.setNavigationOnClickListener { onBackPressed() }

    binding.btnSubmit.setOnClickListener {
        val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
         } else {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // This piece of code is used to send the reset password link to the user's email id if the user is registered.
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->

                    // Hide the progress dialog
                    hideProgressDialog()

                    if (task.isSuccessful) {
                        // Show the toast message and finish the forgot password activity to go back to the login screen.
                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            resources.getString(R.string.email_sent_success),
                            Toast.LENGTH_LONG
                        ).show()

                        finish()
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }

    }

    }
}