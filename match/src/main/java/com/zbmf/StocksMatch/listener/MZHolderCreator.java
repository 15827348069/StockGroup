package com.zbmf.StocksMatch.listener;

/**
 * Created by pq
 * on 2018/3/20.
 */

public interface MZHolderCreator<VH extends MZViewHolder> {
    /**
     * 创建ViewHolder
     * @param imgs 学校图片的数量
     * @return
     */
     VH createViewHolder(int imgs);
}
