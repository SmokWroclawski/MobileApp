package ovh.olo.smok.smokwroclawski.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ovh.olo.smok.smokwroclawski.Adapter.DrawerListAdapter;
import ovh.olo.smok.smokwroclawski.Adapter.SettingsListAdapter;
import ovh.olo.smok.smokwroclawski.Object.NavItem;
import ovh.olo.smok.smokwroclawski.Object.SettingsItem;
import ovh.olo.smok.smokwroclawski.R;

public class SettingsActivity extends Activity {
    private ListView mSettingsList;
    private SettingsListAdapter adapter;
    public ArrayList<SettingsItem> mSettingsItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSettingsList = (ListView) findViewById(R.id.settingsList);

        adapter = new SettingsListAdapter(this, mSettingsItems);
        mSettingsList.setAdapter(adapter);

        mSettingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromSettings(position);
            }
        });

        addSettingsItems();
    }

    private void addSettingsItems() {
        mSettingsItems.add(new SettingsItem("AAA", "aaaaaa"));
        mSettingsItems.add(new SettingsItem("BBB", "bbbbbb"));
        mSettingsItems.add(new SettingsItem("CCC", "cccccc"));
        mSettingsItems.add(new SettingsItem("DDD", "dddddd"));
        mSettingsItems.add(new SettingsItem("EEE", "eeeeee"));
        mSettingsItems.add(new SettingsItem("FFF", "ffffff"));
    }

    private void setSelectedItemId(int position) {
        adapter.setmSelectedItem(position);
        adapter.notifyDataSetChanged();
    }

    private void selectItemFromSettings(int position) {
        setSelectedItemId(position);
        //todo: switch case zalezny od kliknietego itemu w menu

        if (position != -1) {
            mSettingsList.setItemChecked(position, true);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
