package com.dalingge.gankio.common.rxjava.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * 6.0权限管理
 * Created by dingboyang on 2016/12/19.
 */
public class RxPermissions {

    static final String TAG = "RxPermissions";
    static final Object TRIGGER = new Object();

    RxPermissionsFragment mRxPermissionsFragment;

    public RxPermissions(@NonNull Activity activity) {
        mRxPermissionsFragment = getRxPermissionsFragment(activity);
    }

    private RxPermissionsFragment getRxPermissionsFragment(Activity activity) {
        RxPermissionsFragment rxPermissionsFragment = findRxPermissionsFragment(activity);
        boolean isNewInstance = rxPermissionsFragment == null;
        if (isNewInstance) {
            rxPermissionsFragment = new RxPermissionsFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(rxPermissionsFragment, TAG)
                    .commit();
            fragmentManager.executePendingTransactions();
        }
        return rxPermissionsFragment;
    }

    private RxPermissionsFragment findRxPermissionsFragment(Activity activity) {
        return (RxPermissionsFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    public void setLogging(boolean logging) {
        mRxPermissionsFragment.setLogging(logging);
    }

    /**
     * 如果从来没有请求的一个或多个权限,调用相关的框架方法要求用户如果他允许权限
     */
    @SuppressWarnings("WeakerAccess")
    public ObservableTransformer<Object, Boolean> ensure(final String... permissions) {
        return new ObservableTransformer<Object, Boolean>() {
            @Override
            public ObservableSource<Boolean> apply(Observable<Object> upstream) {
                return request(upstream, permissions)
                        .buffer(permissions.length)
                        .flatMap(new Function<List<Permission>, ObservableSource<Boolean>>() {
                            @Override
                            public ObservableSource<Boolean> apply(List<Permission> permissions) throws Exception {
                                if (permissions.isEmpty()) {
                                    return Observable.empty();
                                }
                                // 返回true,所有权限授予。
                                for (Permission p : permissions) {
                                    if (!p.granted) {
                                        return Observable.just(false);
                                    }
                                }
                                return Observable.just(true);
                            }
                        });
            }
        };
    }

    /**
     * 如果从来没有请求的一个或多个权限,调用相关的框架方法要求用户如果他允许权限
     */
    @SuppressWarnings("WeakerAccess")
    public ObservableTransformer<Object, Permission> ensureEach(final String... permissions) {
        return new ObservableTransformer<Object, Permission>() {
            @Override
            public ObservableSource<Permission> apply(Observable<Object> upstream) {
                return request(upstream, permissions);
            }
        };
    }

    /**
     * 立即请求权限e
     * 必须在应用程序的初始化阶段调用
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Observable<Boolean> request(final String... permissions) {
        return Observable.just(TRIGGER).compose(ensure(permissions));
    }

    /**
     * 立即请求权限,每一个请求权限
     * 必须在应用程序的初始化阶段调用
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Observable<Permission> requestEach(final String... permissions) {
        return Observable.just(TRIGGER).compose(ensureEach(permissions));
    }

    private Observable<Permission> request(final Observable<?> trigger, final String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("RxPermissions.request/requestEach requires at least one input permission");
        }
        return oneOf(trigger, pending(permissions))
                .flatMap(new Function<Object, ObservableSource<Permission>>() {
                    @Override
                    public ObservableSource<Permission> apply(Object o) throws Exception {
                        return requestImplementation(permissions);
                    }
                });
    }

    private Observable<?> pending(final String... permissions) {
        for (String p : permissions) {
            if (!mRxPermissionsFragment.containsByPermission(p)) {
                return Observable.empty();
            }
        }
        return Observable.just(TRIGGER);
    }

    private Observable<?> oneOf(Observable<?> trigger, Observable<?> pending) {
        if (trigger == null) {
            return Observable.just(TRIGGER);
        }
        return Observable.merge(trigger, pending);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private Observable<Permission> requestImplementation(final String... permissions) {
        List<Observable<Permission>> list = new ArrayList<>(permissions.length);
        List<String> unrequestedPermissions = new ArrayList<>();

        // 在多个权限的情况下,我们为每个人创造一个可观测的。
        // 最后,可见结合独特的响应。
        for (String permission : permissions) {
            mRxPermissionsFragment.log("Requesting permission " + permission);
            if (isGranted(permission)) {
                // 返回一个授予权限。
                list.add(Observable.just(new Permission(permission, true, false)));
                continue;
            }

            if (isRevoked(permission)) {
                // 返回一个拒绝权限。
                list.add(Observable.just(new Permission(permission, false, false)));
                continue;
            }

            PublishSubject<Permission> subject = mRxPermissionsFragment.getSubjectByPermission(permission);
            //  如果不存在创建一个新的 subject
            if (subject == null) {
                unrequestedPermissions.add(permission);
                subject = PublishSubject.create();
                mRxPermissionsFragment.setSubjectForPermission(permission, subject);
            }

            list.add(subject);
        }

        if (!unrequestedPermissions.isEmpty()) {
            String[] unrequestedPermissionsArray = unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]);
            requestPermissionsFromFragment(unrequestedPermissionsArray);
        }
        return Observable.concat(Observable.fromIterable(list));
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Boolean> shouldShowRequestPermissionRationale(final Activity activity, final String... permissions) {
        if (!isMarshmallow()) {
            return Observable.just(false);
        }
        return Observable.just(shouldShowRequestPermissionRationaleImplementation(activity, permissions));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean shouldShowRequestPermissionRationaleImplementation(final Activity activity, final String... permissions) {
        for (String p : permissions) {
            if (!isGranted(p) && !activity.shouldShowRequestPermissionRationale(p)) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissionsFromFragment(String[] permissions) {
        mRxPermissionsFragment.log("requestPermissionsFromFragment " + TextUtils.join(", ", permissions));
        mRxPermissionsFragment.requestPermissions(permissions);
    }

    /**
     * 返回true,已经获得许可。
     * 小于23 总是false
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isGranted(String permission) {
        return !isMarshmallow() || mRxPermissionsFragment.isGranted(permission);
    }

    /**
     * 返回true,许可被撤销.
     * 小于23 总是false
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isRevoked(String permission) {
        return isMarshmallow() && mRxPermissionsFragment.isRevoked(permission);
    }

    private boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    void onRequestPermissionsResult(String permissions[], int[] grantResults) {
        mRxPermissionsFragment.onRequestPermissionsResult(permissions, grantResults, new boolean[permissions.length]);
    }
}
