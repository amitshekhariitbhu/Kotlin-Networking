package com.mindorks.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.mindorks.kotnetworking.KotNetworking
import com.mindorks.kotnetworking.common.Priority
import com.mindorks.sample.model.User
import org.json.JSONException
import org.json.JSONObject

class PostApiTestActivity : AppCompatActivity() {

    companion object {

        private val TAG: String? = "PostApiTestActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_api_test)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_post_as_json_object -> {
                KotNetworking.post(ApiEndPoint.POST_CREATE_AN_USER)
                        .addBodyParameter("firstname", "Suman")
                        .addBodyParameter("lastname", "Shekhar")
                        .setTag(this)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject { response, error ->
                            if (error != null) {
                                Log.d(PostApiTestActivity.TAG, error.toString())
                            } else {
                                Log.d(PostApiTestActivity.TAG, response.toString())
                            }
                        }
            }

            R.id.btn_post_as_json_array -> {
                KotNetworking.post(ApiEndPoint.POST_CREATE_AN_USER)
                        .addBodyParameter("firstname", "Suman")
                        .addBodyParameter("lastname", "Shekhar")
                        .setTag(this)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray { response, error ->
                            if (error != null) {
                                Log.d(PostApiTestActivity.TAG, error.toString())
                            } else {
                                Log.d(PostApiTestActivity.TAG, response.toString())
                            }
                        }
            }

            R.id.btn_post_as_string -> {
                KotNetworking.post(ApiEndPoint.POST_CREATE_AN_USER)
                        .addBodyParameter("firstname", "Suman")
                        .addBodyParameter("lastname", "Shekhar")
                        .setTag(this)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString { response, error ->
                            if (error != null) {
                                Log.d(PostApiTestActivity.TAG, error.toString())
                            } else {
                                Log.d(PostApiTestActivity.TAG, response.toString())
                            }
                        }
            }

            R.id.btn_check_header -> {
                KotNetworking.post(ApiEndPoint.CHECK_FOR_HEADER)
                        .addHeaders("token", "1234")
                        .setTag(this)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString { response, error ->
                            if (error != null) {
                                Log.d(PostApiTestActivity.TAG, error.toString())
                            } else {
                                Log.d(PostApiTestActivity.TAG, response.toString())
                            }
                        }
            }

            R.id.btn_create_user -> {
                val jsonObject: JSONObject = JSONObject()
                try {
                    jsonObject.put("firstname", "Rohit")
                    jsonObject.put("lastname", "Kumar")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                KotNetworking.post(ApiEndPoint.POST_CREATE_AN_USER)
                        .addApplicationJsonBody(jsonObject)
                        .setTag(this)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString { response, error ->
                            if (error != null) {
                                Log.d(PostApiTestActivity.TAG, error.toString())
                            } else {
                                Log.d(PostApiTestActivity.TAG, response.toString())
                            }
                        }
            }

            R.id.btn_create_new_user -> {
                val user: User = User()
                user.firstname = "aamir"
                user.lastname = "khan"
                KotNetworking.post(ApiEndPoint.POST_CREATE_AN_USER)
                        .addApplicationJsonBody(user)
                        .setTag(this)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString { response, error ->
                            if (error != null) {
                                Log.d(PostApiTestActivity.TAG, error.toString())
                            } else {
                                Log.d(PostApiTestActivity.TAG, response.toString())
                            }
                        }
            }


        }
    }
}
