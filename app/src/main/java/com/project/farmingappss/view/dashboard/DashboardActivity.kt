package com.project.farmingappss.view.dashboard


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.farmingappss.R
import com.project.farmingappss.databinding.ActivityDashboardBinding
import com.project.farmingappss.model.data.WeatherRootList
import com.project.farmingappss.view.apmc.ApmcFragment
import com.project.farmingappss.view.articles.ArticleListFragment
import com.project.farmingappss.view.auth.LoginActivity
import com.project.farmingappss.view.ecommerce.*
import com.project.farmingappss.view.introscreen.IntroActivity
import com.project.farmingappss.view.socialmedia.SMCreatePostFragment
import com.project.farmingappss.view.socialmedia.SocialMediaPostsFragment
import com.project.farmingappss.view.user.UserFragment
import com.project.farmingappss.view.weather.WeatherFragment
import com.project.farmingappss.viewmodel.UserDataViewModel
import com.project.farmingappss.viewmodel.UserProfilePostsViewModel
import com.project.farmingappss.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, com.google.android.gms.location.LocationListener  {
    lateinit var cartFragment: CartFragment
    lateinit var ecommerceItemFragment: EcommerceItemFragment
    lateinit var paymentFragment: PaymentFragment
    lateinit var dashboardFragment: dashboardFragment
    lateinit var ecommerceFragment: EcommerceFragment
    lateinit var weatherFragment: WeatherFragment
    lateinit var navController: NavController
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var blankFragment1: WeatherFragment
    lateinit var apmcFragment: ApmcFragment
    lateinit var articleListFragment: ArticleListFragment
    lateinit var myOrdersFragment: MyOrdersFragment
    lateinit var userFragment: UserFragment
    lateinit var socialMediaPostFragment: SocialMediaPostsFragment
    lateinit var smCreatePostFragment: SMCreatePostFragment
    private lateinit var viewModel: UserDataViewModel
    private lateinit var viewModel2: UserProfilePostsViewModel
    private lateinit var weatherViewModel: WeatherViewModel
    lateinit var sharedPreferences: SharedPreferences


    val firebaseFireStore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    var userName = ""
    var data: WeatherRootList? = null
    var firstTime: Boolean? = null

    private var job: Job? = null

    private var REQUEST_LOCATION_CODE = 101
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val binding: ActivityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        viewModel = ViewModelProviders.of(this).get(UserDataViewModel::class.java)
        binding.userDataViewModel = viewModel


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

         weatherViewModel = ViewModelProviders.of(this)
            .get<WeatherViewModel>(WeatherViewModel::class.java)

        viewModel2 = ViewModelProviders.of(this)
            .get<UserProfilePostsViewModel>(UserProfilePostsViewModel::class.java)
//        viewModel2.getAllPosts(firebaseAuth.currentUser!!.email.toString())

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .build()

        mGoogleApiClient!!.connect()

        buildGoogleApiClient()

        val currentUser = firebaseAuth.currentUser

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        firstTime =sharedPreferences.getBoolean("firstTime", true);


        if(firstTime!!){
            Intent(this, IntroActivity::class.java).also {
                startActivity(it)
            }
//            val editor = sharedPreferences.edit()
//            firstTime = false;
//            editor.putBoolean("firstTime", firstTime!!)
//            editor.apply()
            finish()
            return
        } else{
            if(currentUser == null){
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish()
                return
            } else{

            }
        }



        viewModel.getUserData(firebaseAuth.currentUser!!.email as String)

        navView.setNavigationItemSelectedListener(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Farming App"

        ecommerceItemFragment=EcommerceItemFragment()
        dashboardFragment = dashboardFragment()
        weatherFragment = WeatherFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, dashboardFragment, "dashFrag")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .setReorderingAllowed(true)
            .commit()

        bottomNav.selectedItemId = R.id.bottomNavHome

        val something = navView.getHeaderView(0);

        if (dashboardFragment.isVisible) {
            bottomNav.selectedItemId = R.id.bottomNavHome
        }

        something.setOnClickListener {
            Toast.makeText(this, "You Clicked Slider", Toast.LENGTH_LONG).show()

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
            userFragment = UserFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout, userFragment, "userFrag")
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                setReorderingAllowed(true)
                addToBackStack("userFrag")
                commit()
            }
        }

        apmcFragment = ApmcFragment()
        socialMediaPostFragment = SocialMediaPostsFragment()
        ecommerceFragment=EcommerceFragment()
        paymentFragment = PaymentFragment()
        cartFragment= CartFragment()
        myOrdersFragment=MyOrdersFragment()

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottomNavAPMC -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_layout, apmcFragment, "apmcFrag")
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        setReorderingAllowed(true)
                        addToBackStack("apmcFrag")
                        commit()
                    }
                }
                R.id.bottomNavHome -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_layout, dashboardFragment, "dashFrag")
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        setReorderingAllowed(true)
                        addToBackStack("dashFrag")
                        commit()
                    }
                }
                R.id.bottomNavEcomm -> {
                    ecommerceFragment = EcommerceFragment()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_layout, ecommerceFragment, "ecommItemFrag")
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        setReorderingAllowed(true)
                        addToBackStack("ecommItemFrag")
                        commit()
                    }
                }
                R.id.bottomNavPost -> {
                    socialMediaPostFragment = SocialMediaPostsFragment()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_layout, socialMediaPostFragment, "socialFrag")
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        setReorderingAllowed(true)
                        addToBackStack("socialFrag")
                        commit()
                    }
                }
            }
            true
        }

        viewModel.userliveData.observe(this, Observer {
            if (!it.exists())return@Observer
            val something = navView.getHeaderView(0);
            val posts = it.get("posts") as List<String>
            val city = it.get("city")
            userName = it.get("name").toString()

//            val allPosts = viewModel2.liveData3.value as ArrayList<DocumentSnapshot>

            if(city == null){
                something.cityTextNavHeader.text ="City: "
            } else{
                something.cityTextNavHeader.text ="City: " +  it.get("city").toString()
            }

            something.navbarUserName.text = userName
            something.navbarUserEmail.text = firebaseAuth.currentUser!!.email
            Glide.with(this).load(it.get("profileImage")).into(something.navbarUserImage)

            Log.d("User Data from VM", it.getString("name"))

            something.navBarUserPostCount.text = "Posts Count: " + posts.size.toString()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)

            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            setReorderingAllowed(true)
            addToBackStack("name")
            commit()
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        bottomNav.selectedItemId = R.id.bottomNavHome
        when (item.itemId) {

            R.id.miItem1 -> {
                    ecommerceFragment = EcommerceFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, ecommerceFragment, "ecommListFrag")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .setReorderingAllowed(true)
                        .addToBackStack("ecommListFrag")
                        .commit()
            }
            R.id.miItem2 -> {
                apmcFragment = ApmcFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, apmcFragment, "apmcFrag")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setReorderingAllowed(true)
                    .addToBackStack("apmcFrag")
                    .commit()
            }
            R.id.miItem3 ->{
                smCreatePostFragment = SMCreatePostFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, smCreatePostFragment, "createPostFrag")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setReorderingAllowed(true)
                    .addToBackStack("createPostFrag")
                    .commit()
            }
            R.id.miItem4 -> {
                socialMediaPostFragment = SocialMediaPostsFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, socialMediaPostFragment, "socialFrag")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setReorderingAllowed(true)
                    .addToBackStack("socialFrag")
                    .commit()
            }
            R.id.miItem5 -> {
                weatherFragment = WeatherFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, weatherFragment, "weatherFrag")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setReorderingAllowed(true)
                    .addToBackStack("weatherFrag")
                    .commit()
            }
            R.id.miItem6 -> {
                articleListFragment = ArticleListFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, articleListFragment, "articleListFrag")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setReorderingAllowed(true)
                    .addToBackStack("articleListFrag")
                    .commit()
            }
            R.id.miItem7 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, myOrdersFragment, "myOrdersFrag")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setReorderingAllowed(true)
                    .addToBackStack("myOrdersFrag")
                    .commit()
            }
            R.id.miItem8 -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Log Out")
                    .setMessage("Do you want to logout?")
                    .setPositiveButton("Yes") { dialogInterface, i ->
                        firebaseAuth.signOut()
                        Toast.makeText(this, "Logged Out", Toast.LENGTH_LONG).show()
                        Intent(this, LoginActivity::class.java).also {
                            startActivity(it)
                        }
                    }
                    .setNegativeButton("No") { dialogInterface, i ->
                    }
                    .show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (dashboardFragment.isVisible) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                
            }
        }
    }


    fun automatedClick(){

        if (!checkGPSEnabled()) {
            return
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                getLocation();

            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            getLocation();
//            buildGoogleApiClient()
        }
    }

    override fun onClick(v: View?) {

        job = GlobalScope.launch (Dispatchers.IO){
            if (!checkGPSEnabled()) {
                return@launch
            }

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this@DashboardActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    getLocation();
                } else {
                    //Request Location Permission
                    checkLocationPermission()
                }
            } else {
                getLocation();
            }
        }

    }
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {
            Toast.makeText(this, "Lat: " + mLocation!!.latitude.toString(), Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Long: " + mLocation!!.longitude.toString(), Toast.LENGTH_SHORT).show()

            val coords = mutableListOf<String>()
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(mLocation!!.latitude, mLocation!!.longitude, 1)

            coords.add(mLocation!!.latitude.toString())
            coords.add(mLocation!!.longitude.toString())
            coords.add(addresses[0].locality.toString())
            weatherViewModel.updateCoordinates(coords)

        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }
    private fun startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (mGoogleApiClient?.isConnected?.not() == true) return
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }
    override fun onLocationChanged(p0: Location?) {
//        automatedClick()
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .build()

        mGoogleApiClient!!.connect()
//        automatedClick()
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun showAlert() {
        val dialog = android.app.AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to use this app!")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                android.app.AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location Permissions!\nPlease accept to use location functionality.")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
                    })
                    .create()
                    .show()

            } else ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                        automatedClick()
                    }
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()
        Handler().postDelayed({
            automatedClick()
        }, 1000)

    }

    override fun onStop() {
        job?.cancel()
        if (mGoogleApiClient!!.isConnected()) {
            mGoogleApiClient!!.disconnect()
        }

        super.onStop()
    }
}