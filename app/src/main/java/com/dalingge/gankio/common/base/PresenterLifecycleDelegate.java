package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.dalingge.gankio.common.base.factory.PresenterFactory;
import com.dalingge.gankio.common.base.factory.PresenterStorage;


/**
 * This class adopts a View lifecycle to the Presenter`s lifecycle.
 *
 * @param <P> a type of the presenter.
 */
public final class PresenterLifecycleDelegate<P extends BasePresenter> {

    private static final String PRESENTER_KEY = "presenter";
    private static final String PRESENTER_ID_KEY = "presenter_id";

    @Nullable private PresenterFactory<P> presenterFactory;
    @Nullable private P presenter;
    @Nullable private Bundle bundle;

    private boolean presenterHasView;

    public PresenterLifecycleDelegate(@Nullable PresenterFactory<P> presenterFactory) {
        this.presenterFactory = presenterFactory;
    }

    /**
     * {@link BaseView#getPresenterFactory()}
     */
    @Nullable
    public PresenterFactory<P> getPresenterFactory() {
        return presenterFactory;
    }

    /**
     * {@link BaseView#setPresenterFactory(PresenterFactory)}
     */
    public void setPresenterFactory(@Nullable PresenterFactory<P> presenterFactory) {
        if (presenter != null)
            throw new IllegalArgumentException("setPresenterFactory() should be called before onResume()");
        this.presenterFactory = presenterFactory;
    }

    /**
     * {@link BaseView#getPresenter()}
     */
    public P getPresenter() {
        if (presenterFactory != null && presenter == null) {
            if (bundle != null)
                presenter = PresenterStorage.INSTANCE.getPresenter(bundle.getString(PRESENTER_ID_KEY));

            if (presenter == null) {
                presenter = presenterFactory.createPresenter();
                // 添加到PresenterStorage中,已添加OnDestroyListener,当销毁时从Storage内移除,注意调用就是了
                PresenterStorage.INSTANCE.add(presenter);
                presenter.create(bundle == null ? null : bundle.getBundle(PRESENTER_KEY));
            } else {
                presenter.restore();
            }
            bundle = null;
        }
        return presenter;
    }

    /**
     * {@link android.app.Activity#onSaveInstanceState(Bundle)},
     * {@link android.app.Fragment#onSaveInstanceState(Bundle)},
     * {@link android.view.View#onSaveInstanceState()}.
     */
    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        getPresenter();
        // 有可能不需要presenter
        if (presenter != null) {
            // 保存presenter内需要保存的值
            Bundle presenterBundle = new Bundle();
            presenter.save(presenterBundle);
            bundle.putBundle(PRESENTER_KEY, presenterBundle);
            // 保存presenter在storage的key
            bundle.putString(PRESENTER_ID_KEY, PresenterStorage.INSTANCE.getId(presenter));
        }
        return bundle;
    }

    /**
     * {@link android.app.Activity#onCreate(Bundle)},
     * {@link android.app.Fragment#onCreate(Bundle)},
     * {@link android.view.View#onRestoreInstanceState(Parcelable)}.
     */
    public void onRestoreInstanceState(Bundle presenterState) {
        if (presenter != null)
            throw new IllegalArgumentException("onRestoreInstanceState() should be called before onResume()");
//        this.bundle = ParcelFn.unmarshall(ParcelFn.marshall(presenterState));
        this.bundle = presenterState;
    }

    /**
     * {@link android.app.Activity#onResume()},
     * {@link android.app.Fragment#onResume()},
     * {@link android.view.View#onAttachedToWindow()}
     */
    public void onResume(Object view) {
        getPresenter();
        if (presenter != null && !presenterHasView) {
            //noinspection unchecked
            presenter.takeView(view);
            presenterHasView = true;
        }
    }

    /**
     * {@link android.app.Activity#onDestroy()},
     * {@link android.app.Fragment#onDestroyView()},
     * {@link android.view.View#onDetachedFromWindow()}
     */
    public void onDropView() {
        if (presenter != null && presenterHasView) {
            presenter.dropView();
            presenterHasView = false;
        }
    }


    /**
     * 如果订阅者要操作View,建议最好使用restartableXX和deliverXX方法,因为他们方法中handle有lastest View
     * 解绑View的时候不会受影响,destroy又能正常解除订阅
     *
     * {@link android.app.Activity#onDestroy()},
     * {@link android.app.Fragment#onDestroy()},
     * {@link android.view.View#onDetachedFromWindow()}
     */
    public void onDestroy(boolean destroy) {
        if (presenter != null) {
            // 解绑View
            presenter.dropView();
            if (destroy) {
                presenter.destroy();
                // 诱使GC回收
                presenter = null;
            }
        }
    }
}
