package com.example.pengzongge.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.time.Duration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fClick)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.fClick})
    public void onViewClicked(View view){
        switch (view.getId()) {
            case R.id.fClick:
//                Toast toast = new Toast("text",true, Duration.ofSeconds(10));
//                Toast.makeText("text",true, Duration.ofSeconds(10));
                Toast toast=Toast.makeText(MainActivity.this,"Toast提示消息",Toast.LENGTH_SHORT    );
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                System.out.println("Fclick");
                break;
            default:
                System.out.println("什么玩意啊");

        }
    }
}
