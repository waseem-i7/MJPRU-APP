package `in`.wizion.mjpru.common.LoginSignup

import `in`.wizion.mjpru.R
import `in`.wizion.mjpru.dataBases.UserModelClass
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.arch.core.executor.TaskExecutor
import com.chaos.view.PinView
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class VerifyOTP : AppCompatActivity() {

    lateinit var pinFromUser: PinView
    lateinit var codeBySystem : String
    lateinit var otpDescriptionText : TextView
    lateinit var mAuth : FirebaseAuth

    lateinit var _fullName : String
    lateinit var _email : String
    lateinit var _username : String
    lateinit var _password : String
    lateinit var _date : String
    lateinit var _gender : String
    lateinit var _phoneNo : String
    lateinit var _whatToDo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        //to remove status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        //Hooks
        pinFromUser = findViewById(R.id.pin_view)
        otpDescriptionText = findViewById(R.id.otp_description_text)

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        //Get all values passed from previous screen using Intent
        val bundle : Bundle? = intent.extras
        _whatToDo = bundle!!.getString("whatToDo")!!
        if (_whatToDo == "createNewUser"){
            _fullName = bundle!!.getString("fullName")!!
            _email = bundle.getString("email")!!
            _username = bundle.getString("username")!!
            _password = bundle.getString("password")!!
            _date = bundle.getString("date")!!
            _gender = bundle.getString("gender")!!
            _phoneNo =bundle.getString("phoneNo")!!

        }else if (_whatToDo == "updateData"){
            _phoneNo=bundle.getString("phoneNo")!!
        }



        otpDescriptionText.text="Enter One Time Password Send On $_phoneNo"

        sendVerificationCodeToUser(_phoneNo)
    }


    private fun sendVerificationCodeToUser(_phoneNo: String) {
      val options : PhoneAuthOptions = PhoneAuthOptions.newBuilder(mAuth)
          .setPhoneNumber(_phoneNo)
          .setTimeout(60L,TimeUnit.SECONDS)
          .setActivity(this)
          .setCallbacks(mCallBacks)
          .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)

            codeBySystem = p0
        }

        override fun onVerificationCompleted(phoneAuthCredential : PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code!=null){
                pinFromUser.setText(code)
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@VerifyOTP,e.message,Toast.LENGTH_SHORT).show()
        }

    }

    private fun verifyCode(code: String) {
       val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(codeBySystem,code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in homeDashboard's information
                    //Verification completed successfully here Either
                    //store the data or do whatever desire
                    //Log.d(TAG, "signInWithCredential:success")
                    //Toast.makeText(this,"Verification",Toast.LENGTH_SHORT).show()
                   // val homeDashboard = task.result?.homeDashboard
                       if (_whatToDo == "createNewUser"){
                           storeNewUsersData()
                       }else if (_whatToDo == "updateData"){
                           updateOldUsersData()
                       }


                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"Verification Not Completed! Try again.",Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

    //function for calling update phone number
    private fun updateOldUsersData() {
        val intent = Intent(applicationContext,SetNewPassword::class.java)
        intent.putExtra("phoneNo",_phoneNo)
        startActivity(intent)
        finish()
    }

    //function for store new homeDashboard data in firebase
    private fun storeNewUsersData() {
        val rootNode : FirebaseDatabase = Firebase.database
        val reference : DatabaseReference = rootNode.getReference("Users")

        val addNewUser : UserModelClass = UserModelClass(_fullName,_username,_email,_phoneNo,_password,_date,_gender)
        reference.child(_phoneNo).setValue(addNewUser)
        startActivity(Intent(this,Login::class.java))
        finish()
    }


    //First check the call and then redirect homeDashboard accordingly to the Profile or to set New Password Screen
    fun callNextScreenFromOTP(view: android.view.View) {

      val code = pinFromUser.text.toString()
        if(code.isNotEmpty()){
            verifyCode(code)
        }
    }

}






