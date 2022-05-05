package `in`.wizion.mjpru.homeDashboard


import `in`.wizion.mjpru.R
import `in`.wizion.mjpru.common.LoginSignup.RetailerStartUpScreen
import `in`.wizion.mjpru.common.SplashScreen
import `in`.wizion.mjpru.helperClasses.homeAdapter.*
import `in`.wizion.mjpru.navigationitems.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class UserDashboard : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //Variables
    lateinit var featuredRecycler : RecyclerView
    lateinit var adapter : FeaturedAdapter
    lateinit var categoriesRecyclerView: RecyclerView
    lateinit var categoriesAdapter: CategoriesAdapter
    lateinit var mostViewedRecyclerView: RecyclerView
    lateinit var mostViewedAdapter: MostViewedAdpater


    //Drawer Menu
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView : NavigationView
    private lateinit var menuIcon : ImageView
    private lateinit var contentView : LinearLayout
    companion object{
        const val  END_SCALE = 0.7f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        //to remove status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        //Hooks
        featuredRecycler =findViewById(R.id.featured_recycler)
        categoriesRecyclerView=findViewById(R.id.categories_recycler_view)
        mostViewedRecyclerView=findViewById(R.id.most_viewed_Recycler_view)

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView=findViewById(R.id.navigation_view)
        menuIcon=findViewById(R.id.menu_icon)
        contentView = findViewById(R.id.content)


        //To check Internet Connection
        if (!isConnected(this)){
            showCustomDialog()
        }

        //Navigation Drawer
        navigationDrawer()

        //Recycler View Function calls
        featuredRecycler()
        mostViewedRecycler()
        categoriesRecycler()

    }



    //Check Internet Connection
    private fun isConnected(userDashboard: UserDashboard): Boolean {
        val connectivityManager : ConnectivityManager = userDashboard.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val wifiConn : NetworkInfo? = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileConn : NetworkInfo? = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

         if((wifiConn != null && wifiConn.isConnected) || (mobileConn !=null && mobileConn.isConnected)){
             return true
         }else{
             return false
         }

    }
    private fun showCustomDialog() {
        val adb = AlertDialog.Builder(this)
        adb.setTitle("Alert")
        adb.setIcon(R.mipmap.ic_launcher)
        adb.setMessage("please connect to the internet to proceed further")
        adb.setCancelable(false)
        adb.setPositiveButton("Connect") { _, _ ->
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        adb.setNegativeButton("Cancel") { _, _ ->
           startActivity(Intent(applicationContext,SplashScreen::class.java))
            finish()
        }

        val ad : AlertDialog = adb.create()
        ad.show()
    }


    //Navigation Drawer Functions
    private fun navigationDrawer() {
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)

        menuIcon.setOnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START)
            }else{
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        animateNavigationDrawer()
    }
    private fun animateNavigationDrawer() {
        drawerLayout.setScrimColor(resources.getColor(R.color.purple_500))
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                //Scale the View based on current slide offset
                val diffScaledOffset : Float = slideOffset*(1-END_SCALE)
                val offsetScale : Float = 1-diffScaledOffset
                contentView.setScaleX(offsetScale)
                contentView.setScaleY(offsetScale)

                //Translate the View , accounting for the scaled width
                val xoffset : Float = drawerView.width*slideOffset
                val xoffsetDiff : Float= contentView.getWidth()*diffScaledOffset/2
                val xTranslation : Float = xoffset-xoffsetDiff
                contentView.setTranslationX(xTranslation)
            }
        })
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }
    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

       if (item.itemId==R.id.nav_home){
           startActivity(Intent(applicationContext,UserDashboard::class.java))
        }else if(item.itemId==R.id.nav_under_graduate_form){
           startActivity(Intent(applicationContext,UnderGraduateForm::class.java))

       }else if(item.itemId==R.id.nav_post_graduate_form){
           startActivity(Intent(applicationContext,PostGraduateForm::class.java))

       }else if(item.itemId==R.id.nav_online_examination){
           startActivity(Intent(applicationContext,OnlineExamination::class.java))

       }else if(item.itemId==R.id.nav_entrance_examination){
           startActivity(Intent(applicationContext,EntranceExamination::class.java))

       }else if(item.itemId==R.id.nav_exam_notice){
           startActivity(Intent(applicationContext,ExamNotice::class.java))

       }else if (item.itemId==R.id.nav_exam_scheme){
           startActivity(Intent(applicationContext,ExamScheme::class.java))

       }
        return true
    }


    //Recycler Views Functions
    private fun featuredRecycler(){
        featuredRecycler.setHasFixedSize(true)
        featuredRecycler.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        val featuredLocations : ArrayList<FeaturedModelClass> = ArrayList()
        featuredLocations.add(FeaturedModelClass(R.drawable.splash_screen,"McDonald's","sfsfssfl ls fsfsl sdsfl sf sfsf"))
        featuredLocations.add(FeaturedModelClass(R.drawable.splash_screen,"Sweet and Bakers","sfsfssfl ls fsfsl sdsfl sf sfsf"))
        featuredLocations.add(FeaturedModelClass(R.drawable.splash_screen,"Edenrobe","sfsfssfl ls fsfsl sdsfl sf sfsf"))

        adapter = FeaturedAdapter(featuredLocations)

        featuredRecycler.adapter = adapter
    }
    private fun mostViewedRecycler() {
        mostViewedRecyclerView.setHasFixedSize(true)
        mostViewedRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val mostViewedLocations: ArrayList<MostViewModelClass> = ArrayList()
        mostViewedLocations.add(MostViewModelClass(R.drawable.splash_screen, "McDonald's"))
        mostViewedLocations.add(MostViewModelClass(R.drawable.splash_screen, "Edenrobe"))
        mostViewedLocations.add(MostViewModelClass(R.drawable.splash_screen, "J."))
        mostViewedLocations.add(MostViewModelClass(R.drawable.splash_screen, "Walmart"))
        mostViewedAdapter = MostViewedAdpater(mostViewedLocations)
        mostViewedRecyclerView.adapter = mostViewedAdapter
    }
    private fun categoriesRecycler() {

        //All Gradients
        val gradient2 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(-0x2b341b, -0x2b341b))
        val gradient1 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(-0x852331, -0x852331))
        val gradient3 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(-0x83a61, -0x83a61))
        val gradient4 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(-0x47280b, -0x47280b))

        val categoriesModelClasses: ArrayList<CategoriesModelClass> = ArrayList()
        categoriesModelClasses.add(CategoriesModelClass(gradient1,R.drawable.splash_screen, "Education"))
        categoriesModelClasses.add(CategoriesModelClass(gradient2, R.drawable.splash_screen, "HOSPITAL"))
        categoriesModelClasses.add(CategoriesModelClass(gradient3, R.drawable.splash_screen, "Restaurant"))
        categoriesModelClasses.add(CategoriesModelClass(gradient4, R.drawable.splash_screen, "Shopping"))
        categoriesModelClasses.add(CategoriesModelClass(gradient1, R.drawable.splash_screen, "Transport"))

        categoriesRecyclerView.setHasFixedSize(true)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoriesAdapter= CategoriesAdapter(categoriesModelClasses)
        categoriesRecyclerView.adapter = categoriesAdapter
    }

    //Button Click
    public fun callRetailerScreen(view : View){
        startActivity(Intent(this,RetailerStartUpScreen::class.java))
    }
}





