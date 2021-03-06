package com.vkc.strabo.manager;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.vkc.strabo.R;

/**
 * Created by user2 on 7/9/17.
 */
public class DialogConfirm extends Dialog implements
        android.view.View.OnClickListener {

    public Activity mActivity;


    public DialogConfirm(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.mActivity = a;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);
        init();

    }

    private void init() {

        // Button buttonSet = (Button) findViewById(R.id.buttonOk);

        Button buttonCancel = (Button) findViewById(R.id.buttonOk);
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                mActivity.finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

        dismiss();
    }

}
