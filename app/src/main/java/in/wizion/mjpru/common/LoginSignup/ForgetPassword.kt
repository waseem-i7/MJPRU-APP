package `in`.wizion.mjpru.common.LoginSignup

import `in`.wizion.mjpru.R
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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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


class ForgetPassword : AppCompatActivity() {

    //variables
    private lateinit var forgetPasswordNextBtn : Button
    private lateinit var forgetPasswordCountryPicker : CountryCodePicker
    private lateinit var forgetPasswordPhoneNo : TextInputLayout
    private lateinit var forgetPasswordProgressBar: ProgressBar
    private lateinit var forgetPasswordIcon : ImageView
    private lateinit var forgetPasswordTitle : TextView
    private lateinit var forgetPasswordDesc : TextView
    private lateinit var animationbottom : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        //To Remove Status Bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        //Hooks
        forgetPasswordCountryPicker = findViewById(R.id.forget_password_country_code_picker)
        forgetPasswordProgressBar = findViewById(R.id.forget_password_progress_bar)
        forgetPasswordNextBtn = findViewById(R.id.forget_password_next_btn)
        forgetPasswordPhoneNo = findViewById(R.id.forget_password_phone_number)
        forgetPasswordTitle = findViewById(R.id.forget_password_tittle)
        forgetPasswordIcon = findViewById(R.id.forget_password_icon)
        forgetPasswordDesc = findViewById(R.id.forget_password_desc)

        //Animation Hooks
        animationbottom = AnimationUtils.loadAnimation(this,R.anim.bottom_anim)

        //Set animation to all the elements
        forgetPasswordIcon.animation=animationbottom
        forgetPasswordTitle.animation=animationbottom
        forgetPasswordDesc.animation=animationbottom
        forgetPasswordPhoneNo.animation=animationbottom
        forgetPasswordCountryPicker.animation=animationbottom
        forgetPasswordNextBtn.animation=animationbottom

    }



    /*
        call the OTP screen and pass Phone Number for verification
    */
    fun verifyPhoneNumber(view: android.view.View) {

        //To check Internet Connection
        if (!isConnected(this)){
            showCustomDialog()
            return
        }

        //validate phone Number
        if (!validatePhoneNo()){
            return
        }

        //showing progress bar
        forgetPasswordProgressBar.visibility= View.VISIBLE


        //Get data from fields
        var _phoneNumber = forgetPasswordPhoneNo.editText?.text.toString().trim()
        if(_phoneNumber[0]=='0'){
            _phoneNumber = _phoneNumber.substring(1)
        }
        val _completePhoneNumber = "${forgetPasswordCountryPicker.defaultCountryCodeWithPlus}${_phoneNumber}"

        //Check weather User exists or not in database
        val checkUser : Query = Firebase.database.getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber)

        checkUser.addListenerForSingleValueEvent(object  : ValueEventListener {
            /**
             * This method will be called with a snapshot of the data at this location. It will also be called
             * each time that data changes.
             *
             * @param snapshot The current data at the location
             */
            override fun onDataChange(snapshot: DataSnapshot) {
                //if phone Number exists then call OTP to varify that it is his/her phone number
                if (snapshot.exists()){
                    forgetPasswordPhoneNo.error=null
                    forgetPasswordPhoneNo.isErrorEnabled=false

                    val intent = Intent(applicationContext,VerifyOTP::class.java)
                    intent.putExtra("phoneNo",_completePhoneNumber)
                    intent.putExtra("whatToDo","updateData")
                    startActivity(intent)
                    finish()

                    forgetPasswordProgressBar.visibility=View.GONE

                }else{
                    Toast.makeText(this@ForgetPassword,"No Such User Exist!", Toast.LENGTH_LONG).show()
                    forgetPasswordPhoneNo.error="No such homeDashboard exist!"
                    forgetPasswordPhoneNo.requestFocus()

                    forgetPasswordProgressBar.visibility=View.GONE
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
                Toast.makeText(this@ForgetPassword,error.message, Toast.LENGTH_LONG).show()
                forgetPasswordProgressBar.visibility=View.GONE
                forgetPasswordPhoneNo.error=null
                forgetPasswordPhoneNo.isErrorEnabled=false
            }

        })

    }

    //Check Internet Connection
    private fun isConnected(forgetPassContext : ForgetPassword): Boolean {
        val connectivityManager : ConnectivityManager = forgetPassContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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

        }

        val ad : AlertDialog = adb.create()
        ad.show()
    }

    //To validate Phone Number
    private fun validatePhoneNo(): Boolean {

        val _phoneNumber = forgetPasswordPhoneNo.editText?.text.toString().trim()


        if (_phoneNumber.isEmpty()){
            forgetPasswordPhoneNo.error="Phone number con not be empty"
            forgetPasswordPhoneNo.requestFocus()
            return false
        }else{
            return true
        }
    }
}












