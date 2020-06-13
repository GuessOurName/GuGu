package Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class AdapterGuideViewPager extends PagerAdapter {

    private Context context;
    private List<View> viewList;

    public AdapterGuideViewPager(Context context, List<View> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }
}
