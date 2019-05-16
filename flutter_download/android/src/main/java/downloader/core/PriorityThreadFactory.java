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

package downloader.core;

import android.os.Process;


import java.util.concurrent.ThreadFactory;

import other.Loger;

/**
 * Created by kevin on 2018/3/7.
 * 优先线程工厂
 */

public class PriorityThreadFactory implements ThreadFactory {

    private final int mThreadPriority;//优先级

    PriorityThreadFactory(int threadPriority) {
        mThreadPriority = threadPriority;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        Runnable wrapperRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Process.setThreadPriority(mThreadPriority);
                } catch (Throwable ignored) {

                }
                Loger.e("Runnable=" + Thread.currentThread().getName());
                runnable.run();
            }
        };
        return new Thread(wrapperRunnable);
    }
}