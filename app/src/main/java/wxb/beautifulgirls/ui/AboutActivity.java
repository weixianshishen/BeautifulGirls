package wxb.beautifulgirls.ui;

import android.view.Menu;
import android.view.MenuItem;

import wxb.beautifulgirls.R;
import wxb.beautifulgirls.ui.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        setToolbar(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                //58c78695ae1bf81f540003f2
                showToast("分享");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
