package gigigo.com.vimeolibs;

/**
 * Created by nubor on 05/10/2017.
 */

public class VimeoBuilder {
  //String videoId = "";
  //boolean isMobileConection = false;
  //boolean isWifiConnection = false;
  //boolean isFastConnection = false;
  //VimeoCallback callback;

  String noket = "";
  String client_id = "";
  String client_secret = "";
  String scope = "private public video_files";

  public VimeoBuilder(String token) {
    this.noket = token;
  }

  public String getNoket() {
    return noket;
  }

  public String getClient_id() {
    return client_id;
  }

  public VimeoBuilder setClient_id(String client_id) {
    this.client_id = client_id;
    return this;
  }

  public String getClient_secret() {
    return client_secret;
  }

  public VimeoBuilder setClient_secret(String client_secret) {
    this.client_secret = client_secret;
    return this;
  }

  public String getScope() {
    return scope;
  }

  public VimeoBuilder setScope(String scope) {
    this.scope = scope;
    return this;
  }
}
