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

import com.mindorks.kotnetworking.internal.KotRunnable
import java.util.concurrent.*

/**
 * Created by amitshekhar on 30/04/17.
 */
class KotExecutor(maxNumThreads: Int, threadFactory: ThreadFactory) :
        ThreadPoolExecutor(maxNumThreads, maxNumThreads, 0, TimeUnit.MILLISECONDS,
                PriorityBlockingQueue<Runnable>(), threadFactory) {

    override fun submit(task: Runnable?): Future<*> {
        val futureTask = KotFutureTask(task as KotRunnable)
        execute(futureTask)
        return futureTask
    }

}