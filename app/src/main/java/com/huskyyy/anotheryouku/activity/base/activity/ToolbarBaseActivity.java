package com.huskyyy.anotheryouku.activity.base.activity;

import android.view.Menu;
import android.view.MenuItem;

import com.huskyyy.anotheryouku.R;

/**
 * Created by Wang on 2016/8/22.
 */
public class ToolbarBaseActivity extends BaseActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
