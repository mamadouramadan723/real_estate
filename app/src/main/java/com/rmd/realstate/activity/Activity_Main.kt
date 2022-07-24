package com.rmd.realstate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.rmd.realstate.R
import com.rmd.realstate.databinding.ActivityMainBinding






class Activity_Main : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(com.rmd.realstate.R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                com.rmd.realstate.R.id.navigation_home,
                com.rmd.realstate.R.id.navigation_post,
                com.rmd.realstate.R.id.navigation_saved,
                com.rmd.realstate.R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == com.rmd.realstate.R.id.navigation_filter || destination.id == com.rmd.realstate.R.id.navigation_post ||
                destination.id == com.rmd.realstate.R.id.navigation_post_modify || destination.id == com.rmd.realstate.R.id.navigation_view_apart) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }
        }

        //to Handle email link verified to be authenticated
        if (AuthUI.canHandleIntent(intent)) {
            val link = intent.data.toString()
            val providers: List<AuthUI.IdpConfig> = listOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )
            Log.d("++++++link = ", link)
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setEmailLink(link)
                    .setAvailableProviders(providers)
                    .build(),
                Activity_Login_or_Register.AUTH_REQUEST_CODE
            )
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val item = menu.findItem(R.id.action_signout)
        if (FirebaseAuth.getInstance().currentUser == null) {
            item.title = "Sign In"
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.itemId
        if (id == R.id.action_signout) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                Toast.makeText(this, "Deconnection", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
            } else {
                startActivity(Intent(this, Activity_Login_or_Register::class.java))
            }
        } else if (id == R.id.action_settings) {
            startActivity(Intent(this, Activity_Settings::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}