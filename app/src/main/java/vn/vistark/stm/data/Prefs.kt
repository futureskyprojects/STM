package vn.vistark.stm.data

import android.content.Context

const val IS_FIRST_USE = "IS_FIRST_USE"
const val USER_NAME = "USER_NAME"
const val SECURE_PASS = "SECURE_PASS"

class Prefs {
    companion object {
        fun getIsFirstUse(context: Context): Boolean {
            return context.getSharedPreferences(IS_FIRST_USE, Context.MODE_PRIVATE).getBoolean(
                IS_FIRST_USE, true
            )
        }

        fun setIsFirstUse(context: Context, isFirstUse: Boolean) {
            context.getSharedPreferences(IS_FIRST_USE, Context.MODE_PRIVATE).edit().putBoolean(
                IS_FIRST_USE, isFirstUse
            ).apply()
        }

        fun getUserName(context: Context): String {
            return context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
                .getString(USER_NAME, "")!!
        }

        fun setUserName(context: Context, userName: String) {
            context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE).edit().putString(
                USER_NAME, userName
            ).apply()
        }

        fun getSecurePass(context: Context): String {
            return context.getSharedPreferences(SECURE_PASS, Context.MODE_PRIVATE).getString(
                SECURE_PASS, ""
            )!!
        }

        fun setSecurePass(context: Context, securePass: String) {
            return context.getSharedPreferences(SECURE_PASS, Context.MODE_PRIVATE).edit()
                .putString(
                    SECURE_PASS, securePass
                ).apply()
        }
    }
}