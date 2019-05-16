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

package downloader.request;


import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import downloader.Priority;

/**
 * Created by kevin on 2018/3/7.
 */

public class DownloadRequestBuilder implements RequestBuilder {

    String url;
    /**
     * ts临时路径,mp4路径是固定的
     */
    String dirPath;
    String fileName;
    Priority priority = Priority.MEDIUM;
    Object tag;
    int readTimeout;
    int connectTimeout;
    String userAgent;
    HashMap<String, List<String>> headerMap;
    Activity activity;

    public DownloadRequestBuilder(String url, String dirPath, String fileName, Activity activity) {
        this.url = url;
        this.dirPath = dirPath;
        this.fileName = fileName;
        this.activity = activity;
    }

    public DownloadRequestBuilder(String url) {
        this.url = url;
    }

    @Override
    public DownloadRequestBuilder setHeader(String name, String value) {
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        List<String> list = headerMap.get(name);
        if (list == null) {
            list = new ArrayList<>();
            headerMap.put(name, list);
        }
        if (!list.contains(value)) {
            list.add(value);
        }
        return this;
    }

    @Override
    public DownloadRequestBuilder setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public DownloadRequestBuilder setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public DownloadRequestBuilder setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    @Override
    public DownloadRequestBuilder setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    @Override
    public DownloadRequestBuilder setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public DownloadRequest build() {
        return new DownloadRequest(this,activity);
    }

    public DownloadRequestBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
