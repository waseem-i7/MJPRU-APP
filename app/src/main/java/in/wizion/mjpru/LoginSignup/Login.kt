package `in`.wizion.mjpru.LoginSignup

import `in`.wizion.mjpru.R
import `in`.wizion.mjpru.common.SessionManager
import `in`.wizion.mjpru.admin.AdminDashboard
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker

class Login : AppCompatActivity() {

    // variables
    lateinit var loginCountryCodePicker: CountryCodePicker
    lateinit var phoneNumber : TextInputLayout
    lateinit var password: TextInputLayout
    lateinit var progressbar : ProgressBar
    lateinit var rememberMe : CheckBox
    lateinit var phoneNumberEditText : TextView
    lateinit var passwordEditText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailer_login)

        //To Remove Status Bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        //Hooks
        loginCountryCodePicker = findViewById(R.id.login_country_code_picker)
        phoneNumber=findViewById(R.id.login_phone_number)
        password=findViewById(R.id.login_password)
        progressbar=findViewById(R.id.login_progress_bar)
        rememberMe = findViewById(R.id.login_remember_me)
        phoneNumberEditText = findViewById(R.id.login_phone_number_editText)
        passwordEditText = findViewById(R.id.login_password_editText)

        //To check Internet Connection
        if (!isConnected(this)){
            showCustomDialog()
        }

        //check weather phone number and password is already is saved in shared preferences or not
        val sessionManager = SessionManager(this, SessionManager.SESSION_REMEMBERME)
        if (sessionManager.checkRememberMe()){
            val rememberMeDetails : HashMap<String,String> = sessionManager.getRememberMeDetailFromSession()
            phoneNumberEditText.text = rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENUMBER)
            passwordEditText.text = rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD)
        }

    }

    //Check Internet Connection
    private fun isConnected(login : Login): Boolean {
        val connectivityManager : ConnectivityManager = login.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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


    //To validate Fields
    private fun validateFields(): Boolean {

         val _phoneNumber = phoneNumber.editText?.text.toString().trim()
        val _password = password.editText?.text.toString().trim()

        if (_phoneNumber.isEmpty()){
            phoneNumber.error="Phone number con not be empty"
            phoneNumber.requestFocus()
            return false
        }else if (_password.isEmpty()){
            password.error="Password can not be empty"
            password.requestFocus()
            return false
        }else{
            return true
        }
    }


    //Button for Forget Password
    fun callForgetPassword(view: android.view.View) {
        startActivity(Intent(applicationContext,ForgetPassword::class.java))
    }


    //login the homeDashboard in app!
    fun letTheUserLoggedIn(view: android.view.View) {


        //To check Internet Connection
        if (!isConnected(this)){
            showCustomDialog()
        }

        //validate phone number and password
        if (!validateFields()){
            return
        }

        progressbar.visibility=View.VISIBLE

        //Get values from fields
        val _password = password.editText?.text.toString().trim()
        var _phoneNumber = phoneNumber.editText?.text.toString().trim()
        //To Remove 0 at the start if entered by the homeDashboard
        if(_phoneNumber[0]=='0'){
            _phoneNumber = _phoneNumber.substring(1)
        }
        val _completePhoneNumber = "${loginCountryCodePicker.defaultCountryCodeWithPlus}${_phoneNumber}"


        //Storing Phone Number and Password in SharePreferances
        if(rememberMe.isChecked){
            val sessionManager = SessionManager(this, SessionManager.SESSION_REMEMBERME)
            sessionManager.createRememberMeSession(_phoneNumber, _password)
        }

        //Database
        val checkUser : Query = Firebase.database.getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber)
        checkUser.addListenerForSingleValueEvent(object  : ValueEventListener{
            /**
             * This method will be called with a snapshot of the data at this location. It will also be called
             * each time that data changes.
             *
             * @param snapshot The current data at the location
             */
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    phoneNumber.error=null
                    phoneNumber.isErrorEnabled=false

                    val systemPassword = snapshot.child(_completePhoneNumber).child("password").getValue<String>()
                    if (systemPassword==_password){
                        password.error=null
                        password.isErrorEnabled=false
                        progressbar.visibility=View.GONE

                        val _fullnamefb = snapshot.child(_completePhoneNumber).child("fullName").getValue<String>()
                        val _phoneNumberfb = snapshot.child(_completePhoneNumber).child("phoneNo").getValue<String>()
                        val _userNamefb = snapshot.child(_completePhoneNumber).child("userName").getValue<String>()
                        val _genderfb = snapshot.child(_completePhoneNumber).child("gender").getValue<String>()
                        val _passwordfb= snapshot.child(_completePhoneNumber).child("password").getValue<String>()
                        val _emailfb = snapshot.child(_completePhoneNumber).child("email").getValue<String>()
                        val _datefb = snapshot.child(_completePhoneNumber).child("date").getValue<String>()


                        //Create a Session
                        val sessionManager = SessionManager(this@Login, SessionManager.SESSION_USERSESSION)
                        sessionManager.createLoginSession(_fullnamefb!!,_userNamefb!!,_emailfb!!,_phoneNumberfb!!,_passwordfb!!,_datefb!!,_genderfb!!)
                        startActivity(Intent(this@Login, AdminDashboard::class.java))
                        finish()


                        //Toast.makeText(this@Login, "$_fullnamefb \n $_phoneNumberfb \n $_userNamefb \n $_genderfb \n $_passwordfb \n $_emailfb \n $_datefb",Toast.LENGTH_LONG).show()

                    }else{
                        progressbar.visibility=View.GONE
                        Toast.makeText(this@Login,"Password Does Not Match!",Toast.LENGTH_LONG).show()
                        password.error=null
                        password.isErrorEnabled=false
                        phoneNumber.error=null
                        phoneNumber.isErrorEnabled=false
                    }

                }else{
                    progressbar.visibility=View.GONE
                    Toast.makeText(this@Login,"No Such User Exist!",Toast.LENGTH_LONG).show()
                    password.error=null
                    password.isErrorEnabled=false
                    phoneNumber.error=null
                    phoneNumber.isErrorEnabled=false
                }
            }

            /**
             * This method will be triggered in the event that this listener either failed at the server, or
             * is removed as a result of the security and Firebase Database rules. For more information on
             * securing your data, see: [ Security
 * Quickstart](https://firebase.google.com/docs/database/security/quickstart)
             *
             * @param error A description of the error that occurred
             */
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Login,error.message,Toast.LENGTH_LONG).show()
                progressbar.visibility=View.GONE
                password.error=null
                password.isErrorEnabled=false
                phoneNumber.error=null
                phoneNumber.isErrorEnabled=false
            }

        })
    }


}










