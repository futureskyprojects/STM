package vn.vistark.stm.utils

import android.os.AsyncTask
import java.net.InetAddress
import java.net.UnknownHostException

class Internet {
    class isAvailable : AsyncTask<Unit, Unit, Boolean>() {
        override fun doInBackground(vararg params: Unit?): Boolean {
            try {
                val address: InetAddress = InetAddress.getByName("www.google.com")
                return !address.equals("")
            } catch (e: UnknownHostException) { // Log error
            }
            return false
        }
    }
}