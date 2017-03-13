package wxb.beautifulgirls.ui;

import android.view.Menu;
import android.view.MenuItem;

import wxb.beautifulgirls.R;
import wxb.beautifulgirls.ui.base.BaseActivity;

public class GirlActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_girl;
    }

    @Override
    protected void initView() {
        String imgTitle = getIntent().getStringExtra("imgTitle");
        setToolbar(true, imgTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                return true;
            case R.id.action_save:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
