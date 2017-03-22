package info.ginpei.realmdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "#G#MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void createButton_click(View view) {
        final Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        int id = findLastId() + 1;
        User user = realm.createObject(User.class, id);
        user.setName("Alice");
        user.setAge(11);

        realm.commitTransaction();

        Toast.makeText(this, "Inserted a row!", Toast.LENGTH_SHORT).show();
    }

    private int findLastId() {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(User.class)
                .max("id")
                .intValue();
    }

    public void readAllButton_click(View view) {
        final Realm realm = Realm.getDefaultInstance();

        RealmResults<User> users = realm.where(User.class).findAll();
        Log.d(TAG, "readAllButton_click: %d user(s)." + users.size());
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            Log.d(TAG, String.format("readAllButton_click: #%d %s", i, user.getName()));
        }

        Toast.makeText(MainActivity.this, "Read " + users.size() + " user(s)!", Toast.LENGTH_SHORT).show();
    }
}
