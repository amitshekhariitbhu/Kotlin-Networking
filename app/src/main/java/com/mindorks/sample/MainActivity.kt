package com.mindorks.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openActivity(view: View) {
        when (view.id) {
            R.id.open_get_api_test_activity_btn -> {
                val intent = Intent(this, GetApiTestActivity::class.java)
                startActivity(intent)
            }
            R.id.open_post_api_test_activity_btn -> {
                val intent = Intent(this, PostApiTestActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
