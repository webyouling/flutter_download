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

package downloader.httpclient;

import java.io.IOException;
import java.io.InputStream;

import downloader.request.DownloadRequest;

/**
 * Created by kevin on 2018/3/7.
 */

public interface HttpClient extends Cloneable {

    HttpClient clone();

    void connect(DownloadRequest request) throws IOException;

    void connect(String url) throws IOException;

    int getResponseCode() throws IOException;

    InputStream getInputStream() throws IOException;

    long getContentLength();

    String getContentType();

    String getResponseHeader(String name);

    void close();

}
