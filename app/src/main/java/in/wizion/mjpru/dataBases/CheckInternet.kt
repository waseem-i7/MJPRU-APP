package `in`.wizion.mjpru.dataBases

import `in`.wizion.mjpru.common.LoginSignup.SetNewPassword
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class CheckInternet {

    fun isConnected(context: Context): Boolean {
        val connectivityManager : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val wifiConn : NetworkInfo? = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileConn : NetworkInfo? = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

        if((wifiConn != null && wifiConn.isConnected) || (mobileConn !=null && mobileConn.isConnected)){
            return true
        }else{
            return false
        }

    }


}