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

public class Error {
    /**
     * 其他情况
     */
    public boolean isIOError;
    /**
     * 链接无效
     */
    public boolean isInvalidError;
    /**
     * url失效，responseCode=403；
     */
    public boolean isConnectionError;
    /**
     * ts转mp4失败
     */
    public boolean isDecodeError;
    /**
     * 下载中，客户端断开网络
     */
    public boolean isSocketException;
    /**
     * 无网络
     */
    public boolean isUnknownHostException;
    /**
     * 没有这样的文件或目录
     */
    public boolean isFileNotFoundException;
    /**
     * ts解析失败
     */
    public boolean isParseTsError;
    /**
     * url链接协议错误
     */
    public boolean isProtocolException;
    /**
     * 系统调用期间的I/O错误，软件人为的连接中止
     */
    public boolean isSSLException;
}
