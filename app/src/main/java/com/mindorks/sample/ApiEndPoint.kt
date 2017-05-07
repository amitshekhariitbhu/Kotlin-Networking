package com.mindorks.sample

/**
 * Created by aamir on 05/05/17.
 */
class ApiEndPoint {

    companion object {
        private val BASE_URL = "https://fierce-cove-29863.herokuapp.com"
        val GET_JSON_ARRAY = BASE_URL + "/getAllUsers/{pageNumber}"
        val GET_JSON_OBJECT = BASE_URL + "/getAnUserDetail/{userId}"
        val CHECK_FOR_HEADER = BASE_URL + "/checkForHeader"
        val POST_CREATE_AN_USER = BASE_URL + "/createAnUser"
        val UPLOAD_IMAGE = BASE_URL + "/uploadImage"
    }
}