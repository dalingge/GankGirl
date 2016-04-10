package com.dalingge.gankio.main.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.base.BaseActivity;

import butterknife.Bind;

public class AddGankActivity extends BaseActivity {


    @Bind(R.id.et_url)
    AppCompatEditText etUrl;
    @Bind(R.id.et_desc)
    AppCompatEditText etDesc;
    @Bind(R.id.et_who)
    AppCompatEditText etWho;
    @Bind(R.id.et_type)
    AppCompatEditText etType;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddGankActivity.class);
        return intent;
    }

    @Override
    protected boolean isBack() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_gank;
    }

    @Override
    protected void initView() {
        setTitle("提交干货");

        etType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final String[] types = {"福利", "iOS", "Android", "前端", "App", "拓展资源", "瞎推荐", "休息视频"};
                builder.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etType.setText(types[which]);
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_gank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_gank) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final String[] types = {"福利", "iOS", "Android", "前端", "App", "拓展资源", "瞎推荐", "休息视频"};
            builder.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
