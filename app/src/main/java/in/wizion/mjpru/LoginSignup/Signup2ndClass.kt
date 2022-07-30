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
import android.widget.*
import java.util.*

class Signup2ndClass : AppCompatActivity() {

    //variable
    private lateinit var backBtn : ImageView
    lateinit var next : Button
    lateinit var login : Button
    private lateinit var titleText : TextView
    lateinit var radioGroup: RadioGroup
    lateinit var selectedGender : RadioButton
    lateinit var datePicker: DatePicker



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailer_signup2nd_class)

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
        radioGroup =findViewById(R.id.radio_group)
        datePicker=findViewById(R.id.age_picker)
    }

    fun call3rdSignupScreen(view: android.view.View) {

        if ( !validateGender() || !validateAge()){
            return
        }


        //data come from signup activity
        val bundle : Bundle? = intent.extras
        val _fullName = bundle!!.getString("fullName")
        val _email = bundle.getString("email")
        val _username = bundle.getString("username")
        val _password = bundle.getString("password")

        selectedGender=findViewById(radioGroup.checkedRadioButtonId)
        val _gender=selectedGender.text.toString().trim()

        val day = datePicker.dayOfMonth
        val month=datePicker.month+1
        val year = datePicker.year
        val date = "$day/$month/$year"


        val intent = Intent(this,Signup3rdClass::class.java)

        //send data to signup3rdclass
        intent.putExtra("fullName",_fullName)
        intent.putExtra("email",_email)
        intent.putExtra("username",_username)
        intent.putExtra("password",_password)
        intent.putExtra("gender",_gender)
        intent.putExtra("date",date)


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

    // validation function
    private fun validateGender() : Boolean {
        if (radioGroup.checkedRadioButtonId == -1){
            Toast.makeText(this,"Please Select Gender",Toast.LENGTH_SHORT).show()
            return false
        }else{
            return true
        }
    }

    private fun validateAge() : Boolean{
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val userAge = datePicker.year
        val isAgeValid = currentYear - userAge

        if (isAgeValid<18){
            Toast.makeText(this,"you are not eligible to apply",Toast.LENGTH_SHORT).show()
            return false
        }else{
            return true
        }
    }
}