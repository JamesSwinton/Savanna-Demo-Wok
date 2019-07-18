package com.zebra.jamesswinton.savannademowok.utilities;

import android.app.Activity;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionsHelper {

  public static boolean hasPermission(Activity context, String[] permissions, String rational) {
    if (EasyPermissions.hasPermissions(context, permissions)) {
      return true;
    } else {
      // Do not have permissions, request them now
      EasyPermissions.requestPermissions(context, rational, 0, permissions);
      return false;
    }
  }

}
