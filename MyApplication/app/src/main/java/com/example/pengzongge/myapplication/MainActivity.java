package com.example.pengzongge.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengzongge.myapplication.activity.OrderActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fClick)
    Button button;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.jump_order)
    Button JOBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.fClick,R.id.jump_order})
    public void onViewClicked(View view){
        switch (view.getId()) {
            case R.id.fClick:
//                Toast toast = new Toast("text",true, Duration.ofSeconds(10));
//                Toast.makeText("text",true, Duration.ofSeconds(10));
                Toast toast=Toast.makeText(MainActivity.this,"Toast提示消息",Toast.LENGTH_SHORT    );
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                tvDesc.setText("这是测试 改变文字颜色");
                tvDesc.setTextColor(Color.RED);
//                tvDesc.setVisibility(View.INVISIBLE);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                lp.setMargins(10, 20, 30, 40);
//                tvDesc.setLayoutParams(lp);

                System.out.println("Fclick");
                break;
            case R.id.jump_order:
//
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);

                System.out.println("Fclick");
                break;
            default:
                System.out.println("什么玩意啊");

        }
    }
}
