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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import downloader.request.DownloadRequest;
import other.Constants;

/**
 * Created by kevin on 2018/3/7.
 */

public class DefaultHttpClient implements HttpClient {

    private URLConnection connection;

    public DefaultHttpClient() {

    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public HttpClient clone() {
        return new DefaultHttpClient();
    }

    @Override
    public void connect(DownloadRequest request) throws IOException {
        connection = new URL(request.url).openConnection();
        connection.setReadTimeout(request.readTimeout);
        connection.setConnectTimeout(request.connectTimeout);
        connection.addRequestProperty(Constants.RANGE, String.format(Locale.ENGLISH, "bytes=%d-", request.currentSize));
        connection.addRequestProperty(Constants.USER_AGENT, Constants.IPHONE_UA);
        connection.connect();
    }


    @Override
    public void connect(String url) throws IOException {
        connection = new URL(url).openConnection();
        connection.setReadTimeout(Constants.DEFAULT_READ_TIMEOUT_IN_MILLS);
        connection.setConnectTimeout(Constants.DEFAULT_CONNECT_TIMEOUT_IN_MILLS);
        connection.addRequestProperty(Constants.USER_AGENT, Constants.IPHONE_UA);
        connection.connect();
    }

    @Override
    public int getResponseCode() throws IOException {
        int responseCode = 0;
        if (connection instanceof HttpURLConnection) {
            responseCode = ((HttpURLConnection) connection).getResponseCode();
        }
        return responseCode;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return connection.getInputStream();
    }

    @Override
    public long getContentLength() {
        String length = connection.getHeaderField("Content-Length");
        try {
            return Long.parseLong(length);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String getContentType() {
        return connection.getContentType();
    }

    @Override
    public String getResponseHeader(String name) {
        return connection.getHeaderField(name);
    }

    @Override
    public void close() {
        // no operation
    }

}
