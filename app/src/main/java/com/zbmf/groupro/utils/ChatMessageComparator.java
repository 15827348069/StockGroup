package com.zbmf.groupro.utils;

import com.zbmf.groupro.beans.ChatMessage;

import java.util.Comparator;

/**
 * Created by iMac on 2016/8/8.
 */

public class ChatMessageComparator implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        ChatMessage c1 = (ChatMessage)lhs;
        ChatMessage c2 = (ChatMessage)rhs;
        Double value1 = Double.valueOf(c1.getTime());
        Double value2 = Double.valueOf(c2.getTime());
        return new Double(value1).compareTo(value2);
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }
}
