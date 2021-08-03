package com.thedramaticcolumnist.appdistributor.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.myProfile
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.databinding.HomeScreenBinding

class HomeScreen : AppCompatActivity(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: HomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHomeScreen.toolbar)

        binding.appBarHomeScreen.fab.setOnClickListener(this)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_home_screen)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_dash,
            R.id.nav_order,
            R.id.nav_add_new_product,
            R.id.nav_admin_panel,
            R.id.nav_message,
            R.id.nav_category,
            R.id.nav_products,
            R.id.nav_registration,
            R.id.nav_profile,
            R.id.nav_setting,
            R.id.nav_account,
            R.id.nav_customer_service
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setUpProfileData()
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home_screen)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> {
                navController.navigateUp()
                navController.navigate(R.id.home_to_AddProducts)
            }
        }
    }
    private fun setUpProfileData() {
        val header = binding.navView.getHeaderView(0);
        header.findViewById<TextView>(R.id.email).text =
            FirebaseAuth.getInstance().currentUser?.email

        myProfile?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("name")) {
                    header.findViewById<TextView>(R.id.name).text =
                        snapshot.child("name").value.toString()
                }
                if (snapshot.hasChild("profile_image")) {
                    Glide.with(this@HomeScreen)
                        .load(snapshot.child("profile_image").value.toString())
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(header.findViewById(R.id.imageView))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                mToast(this@HomeScreen, error.message)
            }
        })


    }
}