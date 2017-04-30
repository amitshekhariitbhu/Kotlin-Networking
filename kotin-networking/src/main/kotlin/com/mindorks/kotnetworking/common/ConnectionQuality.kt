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

/**
 * Created by amitshekhar on 30/04/17.
 */
enum class ConnectionQuality {
    /**
     * Bandwidth under 150 kbps.
     */
    POOR,
    /**
     * Bandwidth between 150 and 550 kbps.
     */
    MODERATE,
    /**
     * Bandwidth between 550 and 2000 kbps.
     */
    GOOD,
    /**
     * EXCELLENT - Bandwidth over 2000 kbps.
     */
    EXCELLENT,
    /**
     * Placeholder for unknown bandwidth. This is the initial value and will stay at this value
     * if a bandwidth cannot be accurately found.
     */
    UNKNOWN
}