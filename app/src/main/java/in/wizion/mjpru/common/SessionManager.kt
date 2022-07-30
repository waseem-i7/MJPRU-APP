package `in`.wizion.mjpru.common

import android.content.Context
import android.content.SharedPreferences

class SessionManager(val context : Context,val sessionName : String) {


    //Variables
    var usersSession : SharedPreferences
    var editor: SharedPreferences.Editor


    init {
           usersSession  = context.getSharedPreferences(sessionName , Context.MODE_PRIVATE)
           editor = usersSession.edit()

    }

    companion object{

        //Session names
        var SESSION_USERSESSION : String = "userLoginSession"
        var SESSION_REMEMBERME : String = "rememberMe"

        //User Session Variables
        var IS_LOGIN : String = "IsLoggedIn"
        var KEY_FULLNAME : String = "fullName"
        var KEY_USERNAME : String = "userName"
        var KEY_EMAIL : String = "email"
        var KEY_PHONENUMBER : String = "phoneNumber"
        var KEY_PASSWORD : String = "password"
        var KEY_DATE : String = "date"
        var KEY_GENDER : String = "gender"


        //Remember Me variables
        var IS_REMEMBERME = "IsRememberMe"
        var KEY_SESSIONPHONENUMBER : String = "phoneNumber"
        var KEY_SESSIONPASSWORD : String = "password"

    }


    /*
       User
       Login Session
     */
    fun createLoginSession(fullName : String , userName : String, email : String, phoneNo : String, password : String, age : String, gender : String){
        editor.putBoolean(IS_LOGIN,true)
        editor.putString(KEY_FULLNAME,fullName)
        editor.putString(KEY_USERNAME,userName)
        editor.putString(KEY_EMAIL,email)
        editor.putString(KEY_PHONENUMBER,phoneNo)
        editor.putString(KEY_PASSWORD,password)
        editor.putString(KEY_DATE, age)
        editor.putString(KEY_GENDER,gender)
        editor.commit()
    }

    fun getUserDetailFromSession() : HashMap<String,String>{
        var userData = HashMap<String,String>()

        usersSession.getString(KEY_FULLNAME,null)?.let { userData.put(KEY_FULLNAME, it) }
        usersSession.getString(KEY_USERNAME,null)?.let { userData.put(KEY_USERNAME, it) }
        usersSession.getString(KEY_EMAIL,null)?.let { userData.put(KEY_EMAIL, it) }
        usersSession.getString(KEY_PHONENUMBER,null)?.let { userData.put(KEY_PHONENUMBER, it) }
        usersSession.getString(KEY_PASSWORD,null)?.let { userData.put(KEY_PASSWORD, it) }
        usersSession.getString(KEY_DATE,null)?.let { userData.put(KEY_DATE, it) }
        usersSession.getString(KEY_GENDER,null)?.let { userData.put(KEY_GENDER, it) }

        return userData
    }

   public fun checkLogin() : Boolean{
        if (usersSession.getBoolean(IS_LOGIN,false)){
            return true
        }else{
            return false
        }
    }

   public fun logoutUserFromSession(){
        editor.clear()
        editor.commit()

    }


    /*
    Remember Me
    Session Functions
     */

    fun createRememberMeSession(phoneNo : String, password : String,){
        editor.putBoolean(IS_REMEMBERME,true)
        editor.putString(KEY_SESSIONPHONENUMBER,phoneNo)
        editor.putString(KEY_SESSIONPASSWORD,password)
        editor.commit()
    }

    fun getRememberMeDetailFromSession() : HashMap<String,String>{
        val userData = HashMap<String,String>()


        usersSession.getString(KEY_SESSIONPHONENUMBER,null)?.let { userData.put(
            KEY_SESSIONPHONENUMBER, it) }
        usersSession.getString(KEY_SESSIONPASSWORD,null)?.let { userData.put(KEY_SESSIONPASSWORD, it) }

        return userData
    }

    fun checkRememberMe() : Boolean{
        if (usersSession.getBoolean(IS_REMEMBERME,false)){
            return true
        }else{
            return false
        }
    }

}

















