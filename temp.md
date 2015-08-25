```java
package com.nwwsolutions.cordova.bonjour

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.net.nsd.NsdManager;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;

class BonjourPlugin extends CordovaPlugin {

  Context mContext;

  NsdManager mNsdManager;
  NsdManager.ResolveListener mResolveListener;
  NsdManager.DiscoveryListener mDiscoveryListener;
  NsdManager.RegistrationListener mRegistrationListener;

  public BonjourPlugin(Context context) {
      mContext = context;
      mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
  }

  public void startServiceDiscovery() {
    initializeResolveListener();
    initializeDiscoveryListener();
    initializeRegistrationListener();
  }

  public void stopServiceDiscovery() {
    mNsdManager.stopServiceDiscovery(mDiscoveryListener);
  }

  public void initializeDiscoveryListener() {
    mDiscoveryListener = new NsdManager.DiscoveryListener() {

      @Override
        public void onDiscoveryStarted(String regType) {
          Log.d(TAG, "Service discovery started");
        }

      @Override
      public void onServiceFound(NsdServiceInfo service) {
        Log.d(TAG, "Service discovery success" + service);
        if (!service.getServiceType().equals(SERVICE_TYPE)) {
          Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
        } else if (service.getServiceName().equals(mServiceName)) {
          Log.d(TAG, "Same machine: " + mServiceName);
        } else if (service.getServiceName().contains(mServiceName)){
          mNsdManager.resolveService(service, mResolveListener);
        }
      }

      @Override
      public void onServiceLost(NsdServiceInfo service) {
        Log.e(TAG, "service lost" + service);
        if (mService == service) {
          mService = null;
        }
      }

      @Override
      public void onDiscoveryStopped(String serviceType) {
        Log.i(TAG, "Discovery stopped: " + serviceType);
      }

      @Override
      public void onStartDiscoveryFailed(String serviceType, int errorCode) {
        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
        mNsdManager.stopServiceDiscovery(this);
      }

      @Override
      public void onStopDiscoveryFailed(String serviceType, int errorCode) {
          Log.e(TAG, "Discovery failed: Error code:" + errorCode);
          mNsdManager.stopServiceDiscovery(this);
      }
    };
  }



  public void initializeResolveListener() {
    mResolveListener = new NsdManager.ResolveListener() {

      @Override
      public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
        Log.e(TAG, "Resolve failed" + errorCode);
      }

      @Override
      public void onServiceResolved(NsdServiceInfo serviceInfo) {
        Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

        if (serviceInfo.getServiceName().equals(mServiceName)) {
          Log.d(TAG, "Same IP.");
          return;
        }
        mService = serviceInfo;
      }
    };
  }

  public void initializeRegistrationListener() {
    mRegistrationListener = new NsdManager.RegistrationListener() {

      @Override
      public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
        mServiceName = NsdServiceInfo.getServiceName();
      }

      @Override
      public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
      }

      @Override
      public void onServiceUnregistered(NsdServiceInfo arg0) {
      }

      @Override
      public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
      }

    };
  }
}
```
