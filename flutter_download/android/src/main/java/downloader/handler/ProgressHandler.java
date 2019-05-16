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

package downloader.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import downloader.OnProgressListener;
import other.Constants;

/**
 * Created by kevin on 2018/3/7.
 */

public class ProgressHandler extends Handler {

    private final OnProgressListener listener;

    public ProgressHandler(OnProgressListener listener) {
        super(Looper.getMainLooper());
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.UPDATE_PROGRESS:
                if (listener != null)
                    listener.onProgress(msg.arg1);
                break;
            case Constants.UPDATE_OTHER:
                if (listener != null)
                    listener.onOtherUpdate(msg.arg1);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }
}
