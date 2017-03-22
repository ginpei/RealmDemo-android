package info.ginpei.realmdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "#G#MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        final Realm realm = Realm.getDefaultInstance();
        Log.d(TAG, "onCreate: " + realm);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }

            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Hello!", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}
