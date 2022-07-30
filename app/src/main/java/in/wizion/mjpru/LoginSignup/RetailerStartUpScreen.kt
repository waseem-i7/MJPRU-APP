package `in`.wizion.mjpru.LoginSignup

import `in`.wizion.mjpru.R
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager

class RetailerStartUpScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailer_start_up_screen)

        //to remove status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }



    }

    //login or signup btn click Handled
    fun callLoginScreen(view: View) {
        val intent = Intent(this,Login::class.java)
        val pairs : Pair<View,String> = Pair<View,String>(findViewById(R.id.login_btn), "transition_login")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val options= ActivityOptions.makeSceneTransitionAnimation(this,pairs)
            startActivity(intent,options.toBundle())
            finish()
        }else{
            startActivity(intent)
            finish()
        }
    }
    fun callSignupScreen(view: View) {
        val intent = Intent(this,SignUp::class.java)
        val pairs : Pair<View,String> = Pair<View,String>(findViewById(R.id.signup_btn), "transition_signup")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val options= ActivityOptions.makeSceneTransitionAnimation(this,pairs)
            startActivity(intent,options.toBundle())
        }else{
            startActivity(intent)
        }
    }
}