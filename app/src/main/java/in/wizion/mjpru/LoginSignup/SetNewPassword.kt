package `in`.wizion.mjpru.LoginSignup

import `in`.wizion.mjpru.R
import `in`.wizion.mjpru.common.CheckInternet
import android.content.Intent
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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SetNewPassword : AppCompatActivity() {


    //variables
    private lateinit var setNewPasswordOkBtn : Button
    private lateinit var setNewPasswordField : TextInputLayout
    private lateinit var setNewPasswordFieldConform : TextInputLayout
    private lateinit var setNewPasswordProgressBar: ProgressBar
    private lateinit var setNewPasswordIcon : ImageView
    private lateinit var setNewPasswordTitle : TextView
    private lateinit var setNewPasswordDesc : TextView
    private lateinit var animationbottom : Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_password)


        //To Remove Status Bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        //Hooks
        setNewPasswordProgressBar = findViewById(R.id.set_new_pass_progressBar)
        setNewPasswordOkBtn = findViewById(R.id.set_new_pass_ok_btn)
        setNewPasswordField= findViewById(R.id.set_new_pass_textfield)
        setNewPasswordFieldConform = findViewById(R.id.set_new_pass_textfield_conform)
        setNewPasswordIcon = findViewById(R.id.set_new_pass_icon)
        setNewPasswordDesc = findViewById(R.id.set_new_pass_desc)
        setNewPasswordTitle = findViewById(R.id.set_new_pass_title)

        //Animation Hooks
        animationbottom = AnimationUtils.loadAnimation(this,R.anim.bottom_anim)

        //Set animation to all the elements
        setNewPasswordOkBtn.animation=animationbottom
        setNewPasswordField.animation=animationbottom
        setNewPasswordFieldConform.animation=animationbottom
        setNewPasswordIcon.animation=animationbottom
        setNewPasswordDesc.animation=animationbottom
        setNewPasswordTitle.animation=animationbottom

    }

    /*
      Update Users Password on Button Click
    */
    fun setNewPasswordBtn(view: android.view.View) {

        //To check Internet Connection
        if (!(CheckInternet().isConnected(this))){
            showCustomDialog()
            return
        }

        //validate both text field having same password and Password must be strong
        if (!validateTextFields() || !validatePassword()){
            return
        }

        //showing progress bar
        setNewPasswordProgressBar.visibility= View.VISIBLE

        //Get data from fields
        val _newPassword = setNewPasswordField.editText?.text.toString().trim()
        val bundle : Bundle? = intent.extras
        val _phoneNumber = bundle!!.getString("phoneNo")!!


        //Update Data in Firebase and in Sessions
        val reference : DatabaseReference = Firebase.database.getReference("Users")
        reference.child(_phoneNumber).child("password").setValue(_newPassword)

        startActivity(Intent(applicationContext,ForgetPasswordSuccessMessage::class.java))
        finish()

    }


    //Check Internet Connection
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


    //To validate both text field having same password
    private fun validateTextFields(): Boolean {

        val _firstFieldPass = setNewPasswordField.editText?.text.toString().trim()
        val _secondFieldPass = setNewPasswordFieldConform.editText?.text.toString().trim()


        if (_firstFieldPass==_secondFieldPass){
            return true
        }else{
            setNewPasswordField.error="password should be same"
            setNewPasswordField.requestFocus()
            setNewPasswordFieldConform.error="password should be same"
            setNewPasswordFieldConform.requestFocus()
            return false
        }
    }
    //To validate strong password
    private fun validatePassword() : Boolean{
        val value1 = setNewPasswordField.editText?.text.toString().trim()
        val value2 = setNewPasswordFieldConform.editText?.text.toString().trim()
        val checkPassword = Pattern.compile("^" +
                "(?=.*[0-9])" +        //at least 1 digit
                "(?=.*[a-z])" +       //at least 1 lower case letter
                "(?=.*[A-Z])" +       // at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$")

        if (value1.isEmpty()){
            setNewPasswordField.error="Field can not be empty"
            return false
        }else if(value2.isEmpty()){
            setNewPasswordFieldConform.error="Field can not be empty"
            return false
        }else if (!checkPassword.matcher(value1).matches()){
            setNewPasswordField.error="Please Enter Strong Password"
            return false
        } else if (!checkPassword.matcher(value2).matches()){
            setNewPasswordField.error="Please Enter Strong Password"
            return false
        }
        else{
            setNewPasswordField.error=null
            setNewPasswordField.isErrorEnabled=false
            setNewPasswordFieldConform.error=null
            setNewPasswordFieldConform.isErrorEnabled=false
            return true
        }
    }

}
















