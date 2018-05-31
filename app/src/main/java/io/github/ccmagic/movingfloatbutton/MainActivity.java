package io.github.ccmagic.movingfloatbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
//        NavAndStatusutil.setTransparentBar(this);
        //
//        FloatMovingView floatMovingView = new FloatMovingView(this);
//        floatMovingView.setText("全屏滑动");
//        floatMovingView.setGravity(Gravity.CENTER);
//        floatMovingView.setBackgroundResource(R.mipmap.float_moving_btn);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        floatMovingView.setLayoutParams(layoutParams);
//        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
//        rootView.addView(floatMovingView);
        //
        setContentView(R.layout.activity_main);

        FloatMovingView f = findViewById(R.id.float_moving_view);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "FloatMovingView", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
