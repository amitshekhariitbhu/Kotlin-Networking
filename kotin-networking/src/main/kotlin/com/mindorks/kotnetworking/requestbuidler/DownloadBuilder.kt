package com.mindorks.kotnetworking.requestbuidler

import com.mindorks.kotnetworking.request.KotRequest

/**
 * Created by aamir on 13/06/17.
 */
class DownloadBuilder(val url: String, val dirPath: String, val fileName: String) : RequestBuilderImpl() {

    var percentageThresholdForCancelling: Int = 0

    fun setPercentageThresholdForCancelling(percentageThresholdForCancelling: Int): DownloadBuilder {
        this.percentageThresholdForCancelling = percentageThresholdForCancelling
        return this
    }

    override fun build(): KotRequest {
        return KotRequest(this)
    }

}