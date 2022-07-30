package `in`.wizion.mjpru.LoginSignup

import `in`.wizion.mjpru.R
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {

    //variable
    lateinit var backBtn : ImageView
    lateinit var next : Button
    lateinit var login : Button
    lateinit var titleText : TextView


    //Get data Variables
    lateinit var fullName : TextInputLayout
    lateinit var username : TextInputLayout
    lateinit var email: TextInputLayout
    lateinit var password: TextInputLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailer_sign_up)

        //to remove status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        //Hooks
        backBtn=findViewById(R.id.signup_back_button)
        next=findViewById(R.id.signup_next_button)
        login=findViewById(R.id.signup_login_button)
        titleText=findViewById(R.id.signup_title_text)

        //Hooks for getting data
        fullName = findViewById(R.id.signup_fullname)
        email = findViewById(R.id.signup_email)
        username = findViewById(R.id.signup_username)
        password = findViewById(R.id.signup_password)

        //To check Internet Connection
        if (!isConnected(this)){
            showCustomDialog()
        }
    }


    //Check Internet Connection
    private fun isConnected(signUp : SignUp): Boolean {
        val connectivityManager : ConnectivityManager = signUp.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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
            finish()
        }

        adb.setNegativeButton("Cancel") { _, _ ->
            startActivity(Intent(applicationContext,RetailerStartUpScreen::class.java))
            finish()
        }

        val ad : AlertDialog = adb.create()
        ad.show()
    }



    fun callNextSignupScreen(view: android.view.View) {

        if (!validateFullName() || !validateUsername() || !validateEmail() || !validatePassword()){
            return
        }

        val intent = Intent(this,Signup2ndClass::class.java)
        //send data to signup2 activity
        intent.putExtra("fullName",fullName.editText?.text.toString().trim())
        intent.putExtra("email",email.editText?.text.toString().trim())
        intent.putExtra("username",username.editText?.text.toString().trim())
        intent.putExtra("password",password.editText?.text.toString().trim())

        //Add Transition
        val pair1= Pair<View,String>(backBtn,"transition_back_arrow_btn")
        val pair2= Pair<View,String>(next,"transition_next_btn")
        val pair3= Pair<View,String>(login,"transition_login_btn")
        val pair4= Pair<View,String>(titleText,"transition_title_text")
        val pairs  = arrayOf(pair1,pair2,pair3,pair4)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val options =  ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
            startActivity(intent,options.toBundle())
        } else {
            startActivity(intent)
        }
    }


/*
    * validation Function
    * */

    private fun validateFullName() : Boolean{
        val value = fullName.editText?.text.toString().trim()
        if (value.isEmpty()){
            fullName.error="Field can not be empty"
            return false
        }
        else{
            fullName.error=null
            fullName.isErrorEnabled=false
            return true
        }
    }

    private fun validateUsername() : Boolean{
        val value = username.editText?.text.toString().trim()
        val checkspaces = Pattern.compile("\\A\\w{1,20}\\z")

        if (value.isEmpty()){
            username.error="Field can not be empty"
            return false
        }else if(value.length>20){
            username.error="Username is too large!"
            return false
        }else if (!checkspaces.matcher(value).matches()){
            username.error="No White spaces are allowed!"
            return false
        }
        else{
            username.error=null
            username.isErrorEnabled=false
            return true
        }
    }

    private fun validateEmail() : Boolean{
        val value = email.editText?.text.toString().trim()
        val checkEmail =Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

        if (value.isEmpty()){
            email.error="Field can not be empty"
            return false
        }else if (!checkEmail.matcher(value).matches()){
            email.error="Invalide Email!"
            return false
        }
        else{
            email.error=null
            email.isErrorEnabled=false
            return true
        }
    }

    private fun validatePassword() : Boolean{
        val value = password.editText?.text.toString().trim()
        val checkPassword =Pattern.compile("^" +
                  "(?=.*[0-9])" +        //at least 1 digit
                  "(?=.*[a-z])" +       //at least 1 lower case letter
                  "(?=.*[A-Z])" +       // at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$")

        if (value.isEmpty()){
            password.error="Field can not be empty"
            return false
        }else if (!checkPassword.matcher(value).matches()){
            password.error="Please Enter Strong Password"
            return false
        }
        else{
            password.error=null
            password.isErrorEnabled=false
            return true
        }
    }

}