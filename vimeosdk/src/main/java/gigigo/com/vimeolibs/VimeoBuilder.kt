package gigigo.com.vimeolibs

/**
 * Created by nubor on 05/10/2017.
 */

class VimeoBuilder(token: String) {
  var noket = ""
  var clientId = ""
  var clientSecret = ""
  var scope = "private public video_files"

  init {
    this.noket = token
  }
}
/*
public class VimeoBuilder {
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

 */