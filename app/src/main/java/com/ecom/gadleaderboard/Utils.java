package com.ecom.gadleaderboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class Utils {

    public static void createAlert(Context context, View content){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(true)
                .setView(content).show();
    }
}
