package com.smartdo.suxin.allinv2.Util;

import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by suxin on 9/9/16.
 */
public class UIUtil {
    public static void setTextViewStrikeThrough(TextView tv, boolean strikeThrough) {
        if (strikeThrough) {
            // strikeThrough effect on the text
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // no strike through effect
            tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
