package `in`.wizion.mjpru.common.LoginSignup

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
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import java.util.regex.Pattern

class Signup3rdClass : AppCompatActivity() {

    //variable
    lateinit var scrollView : ScrollView
    lateinit var phoneNumber : TextInputLayout
    lateinit var countryCodePicker : CountryCodePicker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailer_signup3rd_class)

        //to remove status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        //Hooks
        scrollView = findViewById(R.id.signup3_scrollview)
        phoneNumber=findViewById(R.id.signup_phone_number)
        countryCodePicker = findViewById(R.id.country_code_picker)
    }

    fun callVerifyOTPScreen(view: android.view.View) {

//        //validate fields
//        if ( !validatePhone()){
//            return
//        }//validation succeded and now move to next screen to varify phone number and save data



        //Get all values passed from previous screen using Intent
        val bundle : Bundle? = intent.extras
        val _fullName = bundle!!.getString("fullName")
        val _email = bundle.getString("email")
        val _username = bundle.getString("username")
        val _password = bundle.getString("password")
        val _date = bundle.getString("date")
        val _gender = bundle.getString("gender")

        var _getUserEnteredPhoneNumber = phoneNumber.editText?.text.toString().trim() //Get Phone Number
        if(_getUserEnteredPhoneNumber[0]=='0'){
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1)
        }
        val _phoneNo = "${countryCodePicker.defaultCountryCodeWithPlus}${_getUserEnteredPhoneNumber}"


        val intent = Intent(this,VerifyOTP::class.java)

        //send data to signup3rdclass
        intent.putExtra("fullName",_fullName)
        intent.putExtra("email",_email)
        intent.putExtra("username",_username)
        intent.putExtra("password",_password)
        intent.putExtra("gender",_gender)
        intent.putExtra("date",_date)
        intent.putExtra("phoneNo",_phoneNo)
        intent.putExtra("whatToDo","createNewUser") //This is to identify that which action should OTP perform after verification.

        //Add Transition
        val pair  = Pair<View,String>(scrollView,"transition_next_btn")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options =  ActivityOptions.makeSceneTransitionAnimation(this, pair)
            startActivity(intent,options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    private fun validatePhone() : Boolean{
        val value = phoneNumber.editText?.text.toString().trim()
        val checkPhoneNumber = Pattern.compile("Aw{1,20}z")

        if (value.isEmpty()){
            phoneNumber.error="Enter A Valid Phone Number"
            return false
        }else if (!checkPhoneNumber.matcher(value).matches()){
            phoneNumber.error="No White spaces are allowed"
            return false
        }
        else{
            phoneNumber.error=null
            phoneNumber.isErrorEnabled=false
            return true
        }
    }


}