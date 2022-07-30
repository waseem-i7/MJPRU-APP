package `in`.wizion.mjpru.homeDashboard

import `in`.wizion.mjpru.R
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    companion object{
        private val SPLASH_TIMER : Long = 5000
    }

    //Variables
    lateinit var splashScreen : ImageView
    lateinit var poweredBy : TextView

    //Animations
    lateinit var sideAnim : Animation
    lateinit var bottomAnim : Animation

    lateinit var onBoardingScreen : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_splash)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        //Hooks
        splashScreen = findViewById(R.id.backgroudImage)
        poweredBy = findViewById(R.id.powered_by_line)

        //Animations
        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim)
        bottomAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_anim)

        //set Animation on elements
        splashScreen.animation=sideAnim
        poweredBy.animation=bottomAnim

        Looper.myLooper()?.let {
            Handler(it).postDelayed(Runnable {
              kotlin.run {

                    onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE)
                  val isFirstTime : Boolean = onBoardingScreen.getBoolean("firstTime",true)

                  if (isFirstTime){
                      val editor = onBoardingScreen.edit()
                      editor.putBoolean("firstTime",false)
                      editor.apply()
                      val intent = Intent(this@SplashScreen, OnBoarding::class.java)
                      startActivity(intent)
                      finish()
                  }else{
                        val intent = Intent(this@SplashScreen,UserDashboard::class.java)
                       startActivity(intent)
                       finish()
                  }
              }
            }, SPLASH_TIMER)
        }

    }
}