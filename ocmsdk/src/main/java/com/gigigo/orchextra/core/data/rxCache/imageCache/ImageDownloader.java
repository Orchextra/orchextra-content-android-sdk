package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


//asv jajaja no me jodas, las inner class a no ser q sea static conservan una instancia del fader,
// así = x esta chuminá se va de memoria
public class ImageDownloader implements LowPriorityRunnable {
  private final ImageData imageData;
  private final Context mContext;
  private final OcmImageCacheImp.Callback callback;

  ImageDownloader(ImageData imageData, OcmImageCacheImp.Callback callback, Context mContext) {
    this.imageData = imageData;
    this.mContext = mContext;
    this.callback = callback;
  }

  @Override public void run() {
    downloadImage(imageData);
  }

  private void downloadImage(final ImageData imageData) {
    if (imageData == null || imageData.getPath() == null) {
      GGGLogImpl.log("ERROR | URL IMAGE IS NULL :S", LogLevel.ERROR);
      callback.onSuccess(imageData);
      return;
    }
    String filename = OcmImageLoader.md5(imageData.getPath());

    GGGLogImpl.log("GET -> " + imageData.getPath(), LogLevel.INFO);

    // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
    // thumbnails
    File cacheDir = OcmImageLoader.getCacheDir(mContext);
    // Create a path in that dir for a file, named by the default hash of the url
    final File cacheFile = OcmImageLoader.getCacheFile(mContext, filename);
    final File tempCacheFile = OcmImageLoader.getCacheFile(mContext, filename + ".temp");
    if (!cacheDir.exists()) cacheDir.mkdir();
    if (cacheFile.exists()) {
      GGGLogImpl.log("SKIPPED -> " + imageData.getPath(), LogLevel.INFO);
      callback.onSuccess(
          imageData);//asv se supone q si lo tiene bajao, riau ya no lo vuelve a bajar
      return;
    }

    int count;
    InputStream input = null;
    OutputStream output = null;

    try {
      URL url = new URL(imageData.getPath());
      URLConnection connection = url.openConnection();
      connection.connect();

      // getting file length
      int lenghtOfFile = connection.getContentLength();
      //asv
      // input stream to read file - with 8k buffer
      input = new BufferedInputStream(url.openStream(), 8192);

      // Output stream to write file
      output = new FileOutputStream(tempCacheFile);

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
      // totalDownloadSize += total;

      try {
        input.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      try {
        output.flush();
        output.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      GGGLogImpl.log("GET (" + total / 1024 + "kb) <- " + imageData.getPath(),
          (total / 1024) > 150 ? LogLevel.WARN : LogLevel.INFO);

      copy(tempCacheFile, cacheFile);
      callback.onSuccess(imageData);
    } catch (Throwable e) {
      GGGLogImpl.log("ERROR <- " + imageData.getPath(), LogLevel.ERROR);
      e.printStackTrace();
      if (cacheFile.exists()) cacheFile.delete();
      callback.onError(imageData, new Exception(e.getMessage(), e));
    } finally {
      if (tempCacheFile.exists()) tempCacheFile.delete();

    }

    //GGGLogImpl.log(OcmImageCacheImp.totalDownloadSize / 1024 / 1024 + "MB", LogLevel.ASSERT, "TOTAL DOWNLOAD");
  }

  private void copy(File source, File target) throws IOException {

    InputStream in = new FileInputStream(source);
    OutputStream out = new FileOutputStream(target);

    // Copy the bits from instream to outstream
    byte[] buf = new byte[1024];
    int len;

    while ((len = in.read(buf)) > 0) {
      out.write(buf, 0, len);
    }

    in.close();
    out.close();
  }
}