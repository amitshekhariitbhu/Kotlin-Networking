/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mindorks.kotnetworking.common

import com.mindorks.kotnetworking.core.Core

/**
 * Created by aamir on 30/04/17.
 */
class ConnectionClassManager {

    private object Holder {
        val INSTANCE = ConnectionClassManager()
    }

    companion object {

        private const val BYTES_TO_BITS = 8
        private const val DEFAULT_SAMPLES_TO_QUALITY_CHANGE = 5
        private const val MINIMUM_SAMPLES_TO_DECIDE_QUALITY = 2
        private const val DEFAULT_POOR_BANDWIDTH = 150
        private const val DEFAULT_MODERATE_BANDWIDTH = 550
        private const val DEFAULT_GOOD_BANDWIDTH = 2000
        private const val BANDWIDTH_LOWER_BOUND: Long = 10
        val instance: ConnectionClassManager? by lazy { Holder.INSTANCE }

    }

    private var mCurrentConnectionQuality = ConnectionQuality.UNKNOWN
    private var mCurrentBandwidthForSampling = 0
    private var mCurrentNumberOfSample = 0
    private var mCurrentBandwidth = 0
    private var mConnectionQualityCallback: ((connectionQuality: ConnectionQuality,
                                              currentBandWidth: Int) -> Unit)? = null

    @Synchronized fun updateBandwidth(bytes: Long?, timeInMs: Long) {
        if (bytes == null) return

        if (timeInMs == 0L || bytes < 20000
                || bytes * 1.0 / timeInMs * BYTES_TO_BITS < BANDWIDTH_LOWER_BOUND) {
            return
        }

        val bandwidth = bytes * 1.0 / timeInMs * BYTES_TO_BITS

        mCurrentBandwidthForSampling = ((mCurrentBandwidthForSampling * mCurrentNumberOfSample + bandwidth)
                / (mCurrentNumberOfSample + 1)).toInt()
        mCurrentNumberOfSample++

        if (mCurrentNumberOfSample == DEFAULT_SAMPLES_TO_QUALITY_CHANGE
                || mCurrentConnectionQuality === ConnectionQuality.UNKNOWN
                && mCurrentNumberOfSample == MINIMUM_SAMPLES_TO_DECIDE_QUALITY) {

            val lastConnectionQuality = mCurrentConnectionQuality

            mCurrentBandwidth = mCurrentBandwidthForSampling

            mCurrentConnectionQuality = when {
                mCurrentBandwidthForSampling <= 0 -> ConnectionQuality.UNKNOWN
                mCurrentBandwidthForSampling < DEFAULT_POOR_BANDWIDTH -> ConnectionQuality.POOR
                mCurrentBandwidthForSampling < DEFAULT_MODERATE_BANDWIDTH -> ConnectionQuality.MODERATE
                mCurrentBandwidthForSampling < DEFAULT_GOOD_BANDWIDTH -> ConnectionQuality.GOOD
                mCurrentBandwidthForSampling > DEFAULT_GOOD_BANDWIDTH -> ConnectionQuality.EXCELLENT
                else -> ConnectionQuality.UNKNOWN
            }

            if (mCurrentNumberOfSample == DEFAULT_SAMPLES_TO_QUALITY_CHANGE) {
                mCurrentBandwidthForSampling = 0
                mCurrentNumberOfSample = 0
            }
            mConnectionQualityCallback?.let {
                if (mCurrentConnectionQuality !== lastConnectionQuality) {
                    Core.executorSupplier.forMainThreadTasks()
                            .execute {
                                mConnectionQualityCallback?.invoke(mCurrentConnectionQuality, mCurrentBandwidth)
                            }
                }
            }

        }
    }

    fun getCurrentBandWidth(): Int {
        return mCurrentBandwidth
    }

    fun getConnectionQuality(): ConnectionQuality {
        return mCurrentConnectionQuality
    }

    fun setCallback(handler: (connectionQuality: ConnectionQuality, currentBandWidth: Int) -> Unit) {
        mConnectionQualityCallback = handler
    }

    fun removeCallback() {
        mConnectionQualityCallback = null
    }

}