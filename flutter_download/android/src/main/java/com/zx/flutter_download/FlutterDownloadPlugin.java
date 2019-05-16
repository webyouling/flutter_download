package com.zx.flutter_download;

import android.app.Activity;

import downloader.Manager;
import downloader.PRDownloader;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterDownloadPlugin */
public class FlutterDownloadPlugin implements MethodCallHandler {

  private Activity _mActivity;

  FlutterDownloadPlugin(Activity activity){
    this._mActivity = activity;
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_download");
    channel.setMethodCallHandler(new FlutterDownloadPlugin(registrar.activity()));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {

      PRDownloader.initialize(_mActivity);
      String o = "https://r5---sn-ogueln7r.googlevideo.com/";
      String v= "https://hls-hw.xvideos-cdn.com/videos/hls/f6/b4/b7/f6b4b74c53fba7c956c5e20d8136ca9f/hls.m3u8?e=1552644760&l=0&h=198bf9ab4db6c1ab6599c9cf736dc95a";
//      DownloadTaskInfo data = Converter.fromJsonString(call.arguments["value"]);
      Manager.getInstance(this._mActivity).buildStart(v,"1212",v).startDownload();
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else {
      result.notImplemented();
    }
  }
}
