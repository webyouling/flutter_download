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

package downloader;

/**
 * Created by kevin on 2018/3/7.
 */

public class Response {

    public Error error;
    /**
     * 成功
     */
    public boolean isSuccessful;
    /**
     * 暂停
     */
    public boolean isPaused;
    /**
     * 线程数改变
     */
    public boolean isChangeThread;
}
