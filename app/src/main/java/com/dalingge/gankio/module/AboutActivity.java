package com.dalingge.gankio.module;

import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.TextView;

import com.dalingge.gankio.BuildConfig;
import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseToolbarActivity;

import butterknife.BindView;


/**
 * FileName:AboutActivity.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/8
 */

/*
   *           N777777777NO
   *         N7777777777777N
   *        M777777777777777N
   *        $N877777777D77777M
   *       N M77777777ONND777M
   *       MN777777777NN  D777
   *     N7ZN777777777NN ~M7778
   *    N777777777777MMNN88777N
   *    N777777777777MNZZZ7777O
   *    DZN7777O77777777777777
   *     N7OONND7777777D77777N
   *      8$M++++?N???$77777$
   *       M7++++N+M77777777N
   *        N77O777777777777$                              M
   *          DNNM$$$$777777N                              D
   *         N$N:=N$777N7777M                             NZ
   *        77Z::::N777777777                          ODZZZ
   *       77N::::::N77777777M                         NNZZZ$
   *     $777:::::::77777777MN                        ZM8ZZZZZ
   *     777M::::::Z7777777Z77                        N++ZZZZNN
   *    7777M:::::M7777777$777M                       $++IZZZZM
   *   M777$:::::N777777$M7777M                       +++++ZZZDN
   *     NN$::::::7777$$M777777N                      N+++ZZZZNZ
   *       N::::::N:7$O:77777777                      N++++ZZZZN
   *       M::::::::::::N77777777+                   +?+++++ZZZM
   *       8::::::::::::D77777777M                    O+++++ZZ
   *        ::::::::::::M777777777N                      O+?D
   *        M:::::::::::M77777777778                     77=
   *        D=::::::::::N7777777777N                    777
   *       INN===::::::=77777777777N                  I777N
   *      ?777N========N7777777777787M               N7777
   *      77777$D======N77777777777N777N?         N777777
   *     I77777$$$N7===M$$77777777$77777777$MMZ77777777N
   *      $$$$$$$$$$$NIZN$$$$$$$$$M$$7777777777777777ON
   *       M$$$$$$$$M    M$$$$$$$$N=N$$$$7777777$$$ND
   *      O77Z$$$$$$$     M$$$$$$$$MNI==$DNNNNM=~N
   *   7 :N MNN$$$$M$      $$$777$8      8D8I
   *     NMM.:7O           777777778
   *                       7777777MN
   *                       M NO .7:
   *                       M   :   M
   *                            8
   */
public class AboutActivity extends BaseToolbarActivity {


    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.tv_version)
    TextView tvVersion;

//    public static Intent newIntent(Context context) {
//        return new Intent(context, AboutActivity.class);
//    }

    @Override
    protected boolean isBack() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        collapsingToolbar.setTitle("关于");

        tvVersion.setText("Version " + BuildConfig.VERSION_NAME);
    }

}
