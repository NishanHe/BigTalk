package com.example.bigtalk.bigtalk.chat.adapter.base_adapter;

import java.util.List;

/**
 * Created by Jacqueline on 2018/2/6.
 */

public interface IMutlipleItem<T> {
    /**
     * 多种布局的layout文件
     * @param viewtype
     * @return
     */
    int getItemLayoutId(int viewtype);

    /**
     * 多种布局类型
     * @param postion
     * @param t
     * @return
     */
    int getItemViewType(int postion, T t);

    /**
     * 返回布局个数
     * @param list
     * @return
     */
    int getItemCount(List<T> list);
}
