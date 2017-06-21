package ovh.olo.smok.smokwroclawski.Adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ovh.olo.smok.smokwroclawski.Object.NavItem;
import ovh.olo.smok.smokwroclawski.R;

/**
 * Created by Michal on 2017-05-09.
 */

public class DrawerListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NavItem> mNavItems;
    private int mSelectedItem;

    public DrawerListAdapter(Context mContext, ArrayList<NavItem> mNavItems) {
        this.mContext = mContext;
        this.mNavItems = mNavItems;
    }

    public void setmSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }

    public int getmSelectedItem() {
        return mSelectedItem;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        }
        else {
            view = convertView;
        }

        //kolorowanie itemu
        if(position == mSelectedItem) {
            view.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorSelected, null));
        }
        else {
            view.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorUnselected, null));
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText( mNavItems.get(position).mTitle );
        subtitleView.setText( mNavItems.get(position).mSubtitle );
        iconView.setImageResource(mNavItems.get(position).mIcon);

        return view;
    }
}
