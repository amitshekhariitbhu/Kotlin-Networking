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

package com.mindorks.kotnetworking.core

import com.mindorks.kotnetworking.common.Priority
import com.mindorks.kotnetworking.internal.KotRunnable
import java.util.concurrent.FutureTask

/**
 * Created by amitshekhar on 30/04/17.
 */
class KotFutureTask(var runnable: KotRunnable) : FutureTask<KotRunnable>(runnable, null), Comparable<KotFutureTask> {

    override fun compareTo(other: KotFutureTask): Int {
        var p1: Priority? = runnable.priority
        var p2: Priority? = other.runnable.priority
        return if (p1 == p2) runnable.sequence - other.runnable.sequence else p2!!.ordinal - p1!!.ordinal
    }

}