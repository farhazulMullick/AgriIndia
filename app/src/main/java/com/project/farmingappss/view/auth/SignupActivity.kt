package com.project.farmingappss.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.farmingappss.R
import com.project.farmingappss.databinding.ActivitySignupBinding
import com.project.farmingappss.utilities.hide
import com.project.farmingappss.utilities.show
import com.project.farmingappss.utilities.toast
import com.project.farmingappss.view.dashboard.DashboardActivity
import com.project.farmingappss.viewmodel.AuthListener
import com.project.farmingappss.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity(), AuthListener {

    lateinit var googleSignInClient: GoogleSignInClient
    val firebaseAuth = Firebase.auth
    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySignupBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_signup)
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.authViewModel = viewModel
        viewModel.authListener = this

        loginRedirectTextSignup.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        signGoogleBtnSignup.setOnClickListener {
            signIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.returnActivityResult(requestCode, resultCode, data)
    }

    fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    override fun onStarted() {
        progressSignup.show()
    }

    override fun onSuccess(authRepo: LiveData<String>) {
        authRepo.observe(this, Observer {
            progressSignup.hide()
            if (it.toString() == "Success") {
                toast("Account Created")
                Intent(this, DashboardActivity::class.java).also {
                    startActivity(it)
                }
            }
        })
    }

    override fun onFailure(message: String) {
        progressSignup.hide()
        toast("Failure")
    }
}