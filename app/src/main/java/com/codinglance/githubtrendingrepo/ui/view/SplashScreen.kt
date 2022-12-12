package com.codinglance.githubtrendingrepo.ui.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.codinglance.githubtrendingrepo.R

class SplashScreen : AppCompatActivity() {
    var mText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        actionBar?.hide()
        mText = findViewById<View>(R.id.textV) as TextView


        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            val pairs: Array<Pair<View, String>?> =
                arrayOfNulls(1)
            pairs[0] = Pair(mText!!, "image")
            val options =
                ActivityOptions.makeSceneTransitionAnimation(
                    this@SplashScreen,
                    *pairs
                )
            startActivity(intent, options.toBundle())
            finish()

        }, 7000)

    }
}