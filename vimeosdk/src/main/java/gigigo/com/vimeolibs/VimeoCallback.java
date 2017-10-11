package gigigo.com.vimeolibs;

/**
 * Created by nubor on 04/10/2017.
 */

public interface VimeoCallback {

  void onSuccess(VimeoInfo vimeoInfo);

  void onError(Exception e);
}
