package info.ginpei.realmdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import info.ginpei.realmdemo.util.InputDialogBuilder;
import io.realm.Realm;
import io.realm.RealmQuery;
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
        // show a dialog and receive new user's name
        InputDialogBuilder.show(this, "Create", "Input new user's name", result -> {
            if (result != null && !result.isEmpty()) {
                // OK let's make it!
                createUser(result);
            }
        });
    }

    private void createUser(String name) {
        final Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        int id = findLastId() + 1;
        User user = realm.createObject(User.class, id);
        user.setName(name);

        realm.commitTransaction();

        realm.close();

        Toast.makeText(this, "Inserted a row!", Toast.LENGTH_SHORT).show();
    }

    private int findLastId() {
        final Realm realm = Realm.getDefaultInstance();
        RealmQuery<User> query = realm.where(User.class);
        if (query.count() > 0) {
            return query
                    .max("id")
                    .intValue();
        } else {
            return 0;
        }
    }

    public void readAllButton_click(View view) {
        final Realm realm = Realm.getDefaultInstance();

        RealmResults<User> users = realm.where(User.class).findAll();
        Log.d(TAG, String.format("readAllButton_click: %d user(s).", users.size()));
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            Log.d(TAG, String.format("readAllButton_click: #%d %s", user.getId(), user.getName()));
        }

        realm.close();

        Toast.makeText(MainActivity.this, "Read " + users.size() + " user(s)!", Toast.LENGTH_SHORT).show();
    }
}
