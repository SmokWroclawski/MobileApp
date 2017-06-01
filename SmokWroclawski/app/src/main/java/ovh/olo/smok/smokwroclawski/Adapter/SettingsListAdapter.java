package ovh.olo.smok.smokwroclawski.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ovh.olo.smok.smokwroclawski.Activity.MainActivity;
import ovh.olo.smok.smokwroclawski.Activity.SettingsActivity;
import ovh.olo.smok.smokwroclawski.Object.SettingsItem;
import ovh.olo.smok.smokwroclawski.R;

/**
 * Created by Michal on 2017-05-09.
 */

public class SettingsListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SettingsItem> mSettingsItems;
    private int mSelectedItem;

    public SettingsListAdapter(Context mContext, ArrayList<SettingsItem> mSettingsItems) {
        this.mContext = mContext;
        this.mSettingsItems = mSettingsItems;
    }

    public void setmSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }

    public int getmSelectedItem() {
        return mSelectedItem;
    }

    @Override
    public int getCount() {
        return mSettingsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mSettingsItems.get(position);
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
            view = inflater.inflate(R.layout.settings_list_item, null);
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

        titleView.setText( mSettingsItems.get(position).mTitle );
        subtitleView.setText( mSettingsItems.get(position).mSubtitle );

        return view;
    }


}
