package com.dalingge.gankio.module.test;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseToolbarActivity;
import com.dalingge.gankio.common.base.factory.RequiresPresenter;
import com.dalingge.gankio.common.bean.ResultBean;
import com.dalingge.gankio.network.HttpRetrofit;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

@RequiresPresenter(TestPresenter.class)
public class TestActivity extends BaseToolbarActivity<TestPresenter> implements View.OnClickListener{

    Button btnStart;
    Button btnStop;
    TextView tvLog;

    @Override
    protected int getLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        btnStart= (Button) findViewById(R.id.btn_start);
        btnStop= (Button) findViewById(R.id.btn_stop);
        tvLog= (TextView) findViewById(R.id.tv_log);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    private final CompositeSubscription subscriptions = new CompositeSubscription();
    Subscription checkJobSubscription;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                checkJobSubscription = HttpRetrofit.getInstance().apiService.getRandomImage(1).compose(HttpRetrofit.toSubscribe())
                        .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                            @Override
                            public Observable<?> call(Observable<? extends Void> observable) {
                                Log.v("tag", "repeatWhen, call");
                                /**
                                 * 这个方法只会被调用一次。
                                 * 5 表示每次重复的调用（repeated call）会被延迟5s。
                                 */
                                return observable.delay(5, TimeUnit.SECONDS);
                            }
                        })
                        .takeUntil(new Func1<ResultBean, Boolean>() {
                            @Override
                            public Boolean call(ResultBean response) {
                                /** 在这里，我们可以检查服务器返回的数据是否正确，和决定我们是否应该
                                 *  停止轮询。
                                 *  当服务器的任务完成时，我们停止轮询。
                                 *  换句话说，“当任务（job）完成时，我们不拿（take）了”
                                 */
                                Log.v("tag", "takeUntil, call response " + response);
                                return response.isError();
                            }
                        })
                        .filter(new Func1<ResultBean, Boolean>() {
                            @Override
                            public Boolean call(ResultBean response) {
                                /**
                                 * 如果我们在这里返回“false”的话，那这个结果会被过滤掉（filter）
                                 * 过滤（Filtering） 表示 onNext() 不会被调用.
                                 * 但是 onComplete() 仍然会被传递.
                                 */
                                Log.v("tag", "filter, call response " + response);
                                return response.isError();
                            }
                        })
                        .subscribe(
                                new Subscriber<ResultBean>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.v("tag", "onCompleted ");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.v("tag", "onError ");
                                    }

                                    @Override
                                    public void onNext(ResultBean response) {
                                        Log.v("tag", "onNext response " + response.toString());
                                        //服务器轮询停止了，你可以做些其他事情。
                                    }
                                }
                        );
                subscriptions.add(checkJobSubscription);
                // getPresenter().start(TestPresenter.REQUEST_ITEMS);
                break;
            case R.id.btn_stop:
                subscriptions.remove(checkJobSubscription);
              //  getPresenter().stop(TestPresenter.REQUEST_ITEMS);
                break;
        }
    }
}
