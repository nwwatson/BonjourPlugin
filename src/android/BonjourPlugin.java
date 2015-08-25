package com.nwwsolutions.plugins;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class BonjourPlugin extends CordovaPlugin {

  public static final String ACTION_START_SERVICE_DISCOVERY = "startServiceDiscovery";

  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (ACTION_START_SERVICE_DISCOVERY.equals(action)){
      String serviceType = args.getString(0);
      String searchDomain = args.getString(1);

      callbackContext.success(serviceType + searchDomain);

    }
    return true;
  }

}
