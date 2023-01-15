package com.udacity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val nameOfFile = intent.getStringExtra("nameOfFile")
        val status = intent.getStringExtra("status")

        if (status == "Fail") txvStatus.setTextColor(Color.RED)

        txvNameOfFile.text = nameOfFile
        txvStatus.text = status

        button.setOnClickListener{
            finish()
        }
    }

}
