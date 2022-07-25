/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package kg.optima.mobile.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kg.optima.mobile.android.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onPostResume() {
        super.onPostResume()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
