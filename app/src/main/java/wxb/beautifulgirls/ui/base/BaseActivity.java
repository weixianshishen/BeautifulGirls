package wxb.beautifulgirls.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import butterknife.ButterKnife;
import wxb.beautifulgirls.R;
import wxb.beautifulgirls.ui.WebActivity;

/**
 * Created by 黑月 on 2017/3/10.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;
    protected boolean mIsHidden = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 如果存在Toolbar在子类中调用setToolbar方法
     *
     * @param isBack
     */
    protected void setToolbar(boolean isBack) {
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mAppBar == null || mToolbar == null) {
            throw new IllegalStateException("The subClass must contain a AppBarLayout and Toolbar");
        }
        mToolbar.setOnClickListener(view -> onToolbarClick());
        setSupportActionBar(mToolbar);
        if (isBack) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setAppBarAlpha(float alpha) {
        mAppBar.setAlpha(alpha);
    }
    protected void hideOrShowToolbar() {
        mAppBar.animate()
                .translationY(mIsHidden ? 0 : -mAppBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }

    
    protected abstract int getLayoutId();

    /**
     * 点击Toolbar回调
     */
    protected void onToolbarClick() {

    }

    /**
     * 初始化视图
     */
    protected void initView() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 跳转Activity带Bundle
     *
     * @param clzz
     * @param bundle
     */
    protected void startActivity(Class<?> clzz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clzz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        startActivity(intent);
    }


    /**
     * 跳转Activity不带Bundle
     *
     * @param clzz
     */
    protected void startActivity(Class<?> clzz) {
        startActivity(clzz, null);
    }

    /**
     * ActivityForResult带有Bundle
     *
     * @param clzz
     * @param bundle
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> clzz, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, clzz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * ActivityForResult不带Bundle
     *
     * @param clzz
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> clzz, int requestCode) {
        startActivityForResult(clzz, null, requestCode);
    }

    /**
     * 在BaseActivity中重写了一些显示Toast，Dialog的操作，
     * 这样我们在Activity中显示这些UI的时候可以统一调用方法与UI风格。
     *
     * @param toast 内容
     */
    protected void showToast(String toast) {
        if (toast != null && !toast.trim().equals("")) {
            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Toolbar点击返回
     *
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_login:
                loginGitHub();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loginGitHub() {
        String url = getString(R.string.url_login_github);
        Intent intent = WebActivity.newIntent(this, url,
                getString(R.string.action_github_login));
        startActivity(intent);
    }

}
