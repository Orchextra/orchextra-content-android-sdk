package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 6/6/17.
 */

@Singleton public class OcmImageCacheImp implements OcmImageCache {

  private static final String TAG = OcmImageCache.class.getSimpleName();

  private final Context mContext;
  private final ThreadExecutor threadExecutor;
  private final ImageQueue imageQueue;
  private boolean running = false;

  private long totalDownloadSize = 0;

  @Inject public OcmImageCacheImp(Context mContext, ThreadExecutor executor) {
    this.mContext = mContext;
    this.threadExecutor = executor;
    this.imageQueue = new ImageQueueImp(mContext);
  }

  @Override public void start() {
    if (running) return;

    running = true;
    downloadFirst();
  }

  @Override public void stop() {
    running = false;
  }

  @Override public void add(ImageData imageData) {
    imageQueue.add(imageData);
  }

  private void downloadFirst() {
    if (running && imageQueue.hasImages()) {
      executeAsynchronously(new ImageDownloader(imageQueue.getImage(), new Callback() {
        @Override public void onSuccess(ImageData imageData) {
          downloadFirst();
        }

        @Override public void onError(ImageData imageData, Exception e) {
          imageData.consumeRetry();
          if (imageData.getRetriesLeft() > 0) {
            imageQueue.add(imageData);
            GGGLogImpl.log(
                "RETRIES LEFT (" + imageData.getRetriesLeft() + ") <- " + imageData.getPath(),
                LogLevel.ERROR, TAG);
          }
          try {
            Thread.sleep(1000 * 10);
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
          downloadFirst();
        }
      }, mContext));
    } else {
      GGGLogImpl.log("FINISHED", LogLevel.INFO, TAG);
    }
  }

  private void executeMainThread(Runnable runnable) {
    Handler uiHandler = new Handler(Looper.getMainLooper());
    uiHandler.post(runnable);
  }

  private void executeAsynchronously(Runnable runnable) {
    this.threadExecutor.execute(runnable);
  }

  interface Callback {
    void onSuccess(ImageData imageData);

    void onError(ImageData imageData, Exception e);
  }

  private class ImageDownloader implements Runnable {
    private final ImageData imageData;
    private final Context mContext;
    private final Callback callback;

    public ImageDownloader(ImageData imageData, Callback callback, Context mContext) {
      this.imageData = imageData;
      this.mContext = mContext;
      this.callback = callback;
    }

    @Override public void run() {
      downloadImage(imageData);
    }

    private void downloadImage(final ImageData imageData) {
      Log.v("START OcmImageCache -> ", imageData.getPath());
      String filename = OcmImageLoader.md5(imageData.getPath());

      GGGLogImpl.log("GET -> " + imageData.getPath(), LogLevel.INFO, TAG);

      // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
      // thumbnails
      File cacheDir = OcmImageLoader.getCacheDir(mContext);
      // Create a path in that dir for a file, named by the default hash of the url
      File cacheFile = OcmImageLoader.getCacheFile(mContext, filename);
      if (!cacheDir.exists()) cacheDir.mkdir();
      if (cacheFile.exists()) {
        GGGLogImpl.log("SKIPPED -> " + imageData.getPath(), LogLevel.INFO, TAG);
        callback.onSuccess(imageData);
        return;
      }
      int count;
      InputStream input = null;
      OutputStream output = null;
      URLConnection conection = null;
      try {
        URL url = new URL(imageData.getPath());
        conection = url.openConnection();
        conection.connect();
        // getting file length
        int lenghtOfFile = conection.getContentLength();

        // input stream to read file - with 8k buffer
        input = new BufferedInputStream(url.openStream(), 8192);

        // Output stream to write file
        output = new FileOutputStream(cacheFile);

        byte data[] = new byte[1024];

        long total = 0;

        while ((count = input.read(data)) != -1) {
          total += count;
          // publishing the progress....
          // After this onProgressUpdate will be called
          //publishProgress(""+(int)((total*100)/lenghtOfFile));

          // writing data to file
          output.write(data, 0, count);
        }
        totalDownloadSize += total;

        //// flushing output
        //output.flush();
        //
        //// closing streams
        //output.close();
        //input.close();
        GGGLogImpl.log("GET (" + total / 1024 + "kb) <- " + imageData.getPath(),
            (total / 1024) > 2000 ? LogLevel.WARN : LogLevel.INFO, TAG);
        callback.onSuccess(imageData);
      } catch (Exception e) {
        GGGLogImpl.log("ERROR <- " + imageData.getPath(), LogLevel.ERROR, TAG);
        e.printStackTrace();
        callback.onError(imageData, e);
      } finally {
        if (input != null) {
          try {
            input.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if (output != null) {
          try {
            output.flush();
            output.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      GGGLogImpl.log(totalDownloadSize / 1024 / 1024 + "MB", LogLevel.WARN, "TOTAL DOWNLOAD");
    }
  }
}
