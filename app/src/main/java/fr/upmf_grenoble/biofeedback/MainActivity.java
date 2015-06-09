package fr.upmf_grenoble.biofeedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    public static String macAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openBiofeedbackActivity(View view) {
        Intent intent = new Intent(MainActivity.this, BiofeedbackActivity.class);
        startActivity(intent);
    }

    public void openBeltActivity(View view) {
        Intent intent = new Intent(MainActivity.this, BeltActivity.class);
        startActivity(intent);
    }
}
