/*
 * Created by Samyak kamble on 9/4/24, 9:55 PM
 *  Copyright (c) 2024 . All rights reserved.
 *  Last modified 9/4/24, 9:55 PM
 */

package com.samyak2403.whatsappweb

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3-second delay
    }
}