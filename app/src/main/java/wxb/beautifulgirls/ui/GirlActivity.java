package wxb.beautifulgirls.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import wxb.beautifulgirls.R;
import wxb.beautifulgirls.ui.base.BaseActivity;

public class GirlActivity extends BaseActivity {

    @BindView(R.id.picture)
    PhotoView mGirlImg;
    private String mImgTitle;
    private String mImgUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_girl;
    }

    @Override
    protected void initView() {
        mImgTitle = getIntent().getStringExtra("imgTitle");
        mImgUrl = getIntent().getStringExtra("imgUrl");
        setToolbar(true);
        setAppBarAlpha(0.5f);
        setTitle(mImgTitle);
        Picasso.with(this).load(mImgUrl).into(mGirlImg);
        setupPhotoListener();
    }

    private void setupPhotoListener() {
        mGirlImg.setOnViewTapListener((view, v, v1) -> hideOrShowToolbar());
        mGirlImg.setOnLongClickListener(view -> {
            new AlertDialog.Builder(GirlActivity.this)
                    .setMessage("是否保存到手机?")
                    .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        saveImgToGallery();
                    })
                    .show();
            return true;
        });

    }

    private void saveImgToGallery() {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
                              @Override
                              public void call(Subscriber<? super Bitmap> subscriber) {
                                  Bitmap bitmap = null;
                                  try {
                                      bitmap = Picasso.with(GirlActivity.this).load(mImgUrl).get();
                                  } catch (Exception e) {
                                      subscriber.onError(e);
                                  }
                                  if (bitmap == null) {
                                      subscriber.onError(new Exception("无法下载到图片"));
                                  }
                                  subscriber.onNext(bitmap);
                                  subscriber.onCompleted();
                              }
                          }
        ).subscribeOn(Schedulers.io())
                .flatMap(bitmap -> {
                    File appDir = new File(Environment.getExternalStorageDirectory(), "Girls");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    String fileName = mImgTitle.replace('/', '-') + ".jpg";
                    File file = new File(appDir, fileName);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        assert bitmap != null;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri uri = Uri.fromFile(file);
                    // 通知图库更新
                    Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                    sendBroadcast(scannerIntent);
                    return Observable.just(appDir.getAbsolutePath());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> showToast(String.format("保存到%s目录下", s)), throwable -> showToast("保存失败,请重试"));
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
                saveImgToGallery();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
