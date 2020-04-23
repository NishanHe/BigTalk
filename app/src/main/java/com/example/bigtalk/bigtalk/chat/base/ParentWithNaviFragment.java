package com.example.bigtalk.bigtalk.chat.base;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigtalk.bigtalk.R;

/**
 * Created by Jacqueline on 2018/2/6.
 */

public abstract class ParentWithNaviFragment extends Fragment {
    protected View rootView = null;
    private ParentWithNaviActivity.ToolBarListener listener;
    private TextView tv_title;
    public ImageView tv_right;
    public ImageView tv_left;
    public ImageView tv_right2;
    public LinearLayout ll_navi;

    /**
     * 初始化导航条
     */
    public void initNaviView(){
        tv_title = getView(R.id.tv_title);
        tv_right = getView(R.id.tv_right);
        tv_left = getView(R.id.tv_left);
        tv_right2 = getView(R.id.tv_right2);
        setListener(setToolBarListener());
        tv_left.setOnClickListener(clickListener);
        tv_right.setOnClickListener(clickListener);
        tv_right2.setOnClickListener(clickListener);
        tv_title.setText(title());
        refreshTop();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_left:
                    if (listener != null){
                        listener.clickLeft();
                    }
                    break;
                //从右数第二个
                case R.id.tv_right2:
                    if (listener != null)
                        listener.clickRight2();
                    break;
                case R.id.tv_right:
                    if (listener != null)
                        listener.clickRight();
                    break;

                default:
                    break;
            }
        }
    };

    private void refreshTop() {
        setLeftView(left());
        setValue(R.id.tv_right, right());
        this.tv_title.setText(title());
    }

    private void setLeftView(Object obj){
        if(obj !=null && !obj.equals("")){
            tv_left.setVisibility(View.VISIBLE);
            if(obj instanceof Integer){
                tv_left.setImageResource(Integer.parseInt(obj.toString()));
            }else{
                tv_left.setImageResource(R.drawable.toolbar_edit_selector);
            }
        }else{
            tv_left.setVisibility(View.INVISIBLE);
        }
    }

    private void setRight2View(Object obj){
        if(obj !=null && !obj.equals("")){
            tv_right2.setVisibility(View.VISIBLE);
            if(obj instanceof Integer){
                tv_right2.setImageResource(Integer.parseInt(obj.toString()));
            }else{
                tv_right2.setImageResource(R.drawable.toolbar_settings_selector);
            }
        }else{
            tv_right2.setVisibility(View.INVISIBLE);
        }
    }
    private void setRightView(Object obj){
        if(obj !=null && !obj.equals("")){
            tv_right.setVisibility(View.VISIBLE);
            if(obj instanceof Integer){
                tv_right.setImageResource(Integer.parseInt(obj.toString()));
            }else{
                tv_right.setImageResource(R.drawable.toolbar_share_selector);
            }
        }else{
            tv_right.setVisibility(View.INVISIBLE);
        }
    }

    private void setValue(int id,Object obj){
        if (obj != null && !obj.equals("")) {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
            if (obj instanceof String) {
                ((TextView) getView(id)).setText(obj.toString());
            } else if (obj instanceof Integer) {
                getView(id).setBackgroundResource(Integer.parseInt(obj.toString()));
            }
        } else {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
        }
    }

    private void setListener(ParentWithNaviActivity.ToolBarListener listener) {
        this.listener = listener;
    }

    /**导航栏标题
     * @return
     */
    protected abstract String title();

    /**导航栏右边：可以为string或图片资源id，不是必填项
     * @return
     */
    public  Object right(){
        return null;
    }

    /**导航栏左边
     * @return
     */
    public Object left(){return null;}

    /**设置导航栏从右数第二个
     * @param
     */
    public Object right2(){return null;}

    /**设置导航条背景色
     * @param color
     */
    public void setNavBackground(int color){
        ll_navi.setBackgroundColor(color);
    }

    /**设置右边按钮的文字大小
     * @param dimenId
     */
//    public void setRightTextSize(float dimenId){
//        tv_right.setTextSize(dimenId);
//    }

    /**设置导航栏监听
     * @return
     */
    public ParentWithNaviActivity.ToolBarListener setToolBarListener(){return null;}

    protected <T extends View> T getView(int id) {
        return (T) rootView.findViewById(id);
    }

}
