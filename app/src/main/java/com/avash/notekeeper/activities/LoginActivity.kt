package com.avash.notekeeper.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import com.avash.notekeeper.R
import com.avash.notekeeper.databinding.ActivityLoginBinding
import com.avash.notekeeper.utlis.MSPTextView
import com.avash.notekeeper.utlis.MSPTextViewBold
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener{

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        binding.btnLogin.setOnClickListener(this)

        binding.tvForgotPassword.setOnClickListener(this)

        binding.tvRegister.setOnClickListener(this)

    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please write your email", true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                true
            }
        }
    }

    // TODO Step 3: Implement the View.OnClickListener and assign the onclick events of respective components in the onClick function.
    // START
    // In Login screen the clickable components are Login Button, ForgotPassword text and Register Text.
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.tv_forgot_password -> {

                    val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {

                    // TODO Step 6: Call the validate function.
                    // START
                    logInRegisteredUser()
                    // END
                }

                R.id.tv_register -> {
                    // Launch the register screen when the user clicks on the text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Get the text from editText and trim the space
            val email = binding.etEmail.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // Hide the progress dialog
                    hideProgressDialog()

                    if (task.isSuccessful) {

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    // END
}