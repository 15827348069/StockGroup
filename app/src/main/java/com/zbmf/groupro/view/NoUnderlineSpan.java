package com.zbmf.groupro.view;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by xuhao on 2016/12/24.
 */

public class NoUnderlineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
//        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
