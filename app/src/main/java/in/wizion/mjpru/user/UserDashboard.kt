package `in`.wizion.mjpru.user

import `in`.wizion.mjpru.R
import `in`.wizion.mjpru.dataBases.SessionManager
import `in`.wizion.mjpru.user.bottomNavigationFragments.ResultFragment
import `in`.wizion.mjpru.user.bottomNavigationFragments.SyllabusFragment
import `in`.wizion.mjpru.user.bottomNavigationFragments.UserDashboardFragment
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.fragment_result.*

class UserDashboard : AppCompatActivity() {

    //Bottom Navigation Variable
    private lateinit var chipNavigationBar: ChipNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailer_dashboard)

        //to remove status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        //Bottom Navigation Bar Hook
        chipNavigationBar = findViewById(R.id.bottom_nav_menu)

        //Bottom Navigation Bar
        chipNavigationBar.setItemSelected(R.id.bottom_nav_dashboard)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, UserDashboardFragment()).commit()
        bottomMenu()




        //Use Later
        val sessionManager = SessionManager(this,SessionManager.SESSION_USERSESSION)
        val userDetails : HashMap<String,String> = sessionManager.getUserDetailFromSession()
        val fullName = userDetails.get(SessionManager.KEY_FULLNAME)
        val username = userDetails.get(SessionManager.KEY_USERNAME)
        val email = userDetails.get(SessionManager.KEY_EMAIL)
        val phonenumber = userDetails.get(SessionManager.KEY_PHONENUMBER)
        val password = userDetails.get(SessionManager.KEY_PASSWORD)
        val date = userDetails.get(SessionManager.KEY_DATE)
        val gender = userDetails.get(SessionManager.KEY_GENDER)


    }

    override fun onBackPressed() {

        if(webView.canGoBack()){
            webView.goBack()
        }else {
            super.onBackPressed()
        }
    }

    //Bottom Navigation Bar Function
    private fun bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener( object :
            ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                val fragment : Fragment? = when(id){
                    R.id.bottom_nav_result -> ResultFragment()
                    R.id.bottom_nav_dashboard -> UserDashboardFragment()
                    R.id.bottom_nav_syllabus-> SyllabusFragment()
                    else -> null
                }
                if (fragment != null) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
                }
            }

        })
    }

}