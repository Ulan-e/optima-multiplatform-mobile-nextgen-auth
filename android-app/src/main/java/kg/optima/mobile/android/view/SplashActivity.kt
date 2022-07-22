/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package kg.optima.mobile.android.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kg.optima.mobile.android.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onPostResume() {
        super.onPostResume()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
