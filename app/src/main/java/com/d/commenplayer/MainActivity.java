package com.d.commenplayer;

import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.d.commenplayer.activity.ListActivity;
import com.d.commenplayer.activity.SimpleActivity;
import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ToastUtils;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.permissioncompat.Permission;
import com.d.lib.permissioncompat.PermissionCompat;
import com.d.lib.permissioncompat.PermissionSchedulers;
import com.d.lib.permissioncompat.callback.PermissionCallback;

public class MainActivity extends BaseActivity<MvpBasePresenter<MvpView>>
        implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_simple:
                requestPermission(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mActivity, SimpleActivity.class));
                    }
                });
                break;

            case R.id.btn_list:
                requestPermission(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mActivity, ListActivity.class));
                    }
                });
                break;
        }
    }

    private void requestPermission(final Runnable runnable) {
        PermissionCompat.with(mActivity)
                .requestEachCombined(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribeOn(PermissionSchedulers.io())
                .observeOn(PermissionSchedulers.mainThread())
                .requestPermissions(new PermissionCallback<Permission>() {
                    @Override
                    public void onNext(Permission permission) {
                        if (!permission.granted) {
                            ToastUtils.toast(getApplicationContext(), "Denied permission!");
                            return;
                        }
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public MvpBasePresenter<MvpView> getPresenter() {
        return null;
    }

    @Override
    protected MvpView getMvpView() {
        return null;
    }

    @Override
    protected void bindView() {
        ViewHelper.setOnClickListener(this, this,
                R.id.btn_simple,
                R.id.btn_list);
    }

    @Override
    protected void init() {

    }
}
