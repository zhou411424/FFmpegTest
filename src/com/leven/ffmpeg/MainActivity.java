
package com.leven.ffmpeg;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    private EditText videopathTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        videopathTxt = (EditText) findViewById(R.id.videopath);
        Button startPlay = (Button) findViewById(R.id.startPlay);
        
        startPlay.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                String videopath = videopathTxt.getText().toString();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, VideoPlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("videopath", videopath);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
