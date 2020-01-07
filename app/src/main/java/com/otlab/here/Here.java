package com.otlab.here;

public class Here {
    public static boolean checkVersion(){
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT;
    }
    public static boolean checkPermission(){

        return true;
    }
}
