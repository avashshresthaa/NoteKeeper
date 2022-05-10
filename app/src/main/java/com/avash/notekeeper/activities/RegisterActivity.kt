package com.avash.notekeeper.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toolbar
import com.avash.notekeeper.R
import com.avash.notekeeper.databinding.ActivityRegisterBinding
import com.avash.notekeeper.utlis.MSPTextView
import com.avash.notekeeper.utlis.MSPTextViewBold
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        val log = findViewById<MSPTextViewBold>(R.id.tv_login)
        log.setOnClickListener {
            // Launch the register screen when the user clicks on the text.
            onBackPressed()

        }

        binding.btnRegister.setOnClickListener{
             registerUser()
        }

    }

    private fun setupActionBar() {
        val tol = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_register_activity)
        setSupportActionBar(tol)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        tol.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            binding.etPassword.text.toString().trim { it <= ' ' } != binding.etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !binding.cbTermsAndCondition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
                   /* showErrorSnackBar("Your details are valid.", false)*/
                true
            }
        }
    }

    private fun registerUser() {

        // Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // Hide the progress dialog
                        hideProgressDialog()

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            showErrorSnackBar(
                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )

                            /**
                             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                             * and send him to Login Screen.
                             */

                            /**
                             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                             * and send him to Login Screen.
                             */
                                FirebaseAuth.getInstance().signOut()
                            // Finish the Register Screen
                            finish()
                        } else {
                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

}