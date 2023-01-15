package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.util.Constants
import com.udacity.util.createNotificationChannel
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private var url: String? = null


    private var mStatus = ""
    private var nameOfFile = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createNotificationChannel(
            Constants.CHANNEL_ID,
            resources.getString(R.string.channel_name),
            this
        )


        custom_button.setOnClickListener {

            if (url == null) {
                Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            custom_button.buttonState = ButtonState.Loading
            download(url!!)
        }
    }


    private val receiver = object : BroadcastReceiver() {


        override fun onReceive(context: Context?, intent: Intent?) {

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID == id) {

                Toast.makeText(context, "Downloded", Toast.LENGTH_SHORT).show()
                custom_button.buttonState = ButtonState.Completed
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

                val cursor =
                    downloadManager.query(DownloadManager.Query().setFilterById(downloadID))

                if (cursor.moveToFirst()) {
                    val status =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        mStatus = "Success"
                    }else {mStatus = "Fail"}
                }
                sendNotification()
            }
        }
    }

        private fun sendNotification() {
           val notificationManager =
                getSystemService(NotificationManager::class.java) as NotificationManager
               notificationManager.sendNotification(
                resources.getString(R.string.notification_description),
                this,
                nameOfFile,
                mStatus
            )
        }


        private fun download(url: String) {

            val request =
                DownloadManager.Request(Uri.parse(url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }


        fun onRadioButtonClicked(view: View) {

            if (view is RadioButton) {

                val isChecked = view.isChecked

                when (view.id) {
                    R.id.btnDowGlide -> if (isChecked) {
                        url = Constants.URLGLIDE
                        nameOfFile = resources.getString(R.string.txvDownloadGlide)
                    }

                    R.id.btnLoadApp -> if (isChecked) {
                        url = Constants.URLLAODAPP
                        nameOfFile = resources.getString(R.string.txvLoadApp)

                    }
                    else -> if (isChecked) {
                        url = Constants.URLRETROFIT
                        nameOfFile = resources.getString(R.string.txvRetrofit)
                    }
                }
            }
        }

        override fun onStop() {
            super.onStop()
            unregisterReceiver(receiver)
        }

    }
