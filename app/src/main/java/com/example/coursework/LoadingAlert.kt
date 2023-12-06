package com.example.coursework

import android.app.Activity
import androidx.appcompat.app.AlertDialog

class LoadingAlert(private val myActivity: Activity) {

    private lateinit var dialog: AlertDialog

    fun startAlertDialog(){
        dialog = AlertDialog.Builder(myActivity)
            .setView(myActivity.layoutInflater.inflate(R.layout.loading_dialog, null))
            .setCancelable(false)
            .create()

        dialog.show()
    }

    fun closeAlertDialog(){dialog.dismiss()}
}