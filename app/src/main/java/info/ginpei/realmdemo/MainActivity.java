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
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
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
        realm.beginTransaction();

        int id = findLastId() + 1;
        User user = realm.createObject(User.class, id);
        user.setName(name);

        realm.commitTransaction();

        Toast.makeText(this, "Inserted a row!", Toast.LENGTH_SHORT).show();
    }

    private int findLastId() {
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
        // OK look over!
        readAllUsers();
    }

    private void readAllUsers() {
        RealmResults<User> users = realm.where(User.class).findAll();
        Log.d(TAG, String.format("readAllUsers: %d user(s).", users.size()));
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            Log.d(TAG, String.format("readAllUsers: #%d %s", user.getId(), user.getName()));
        }

        Toast.makeText(MainActivity.this, "Read " + users.size() + " user(s)!", Toast.LENGTH_SHORT).show();
    }

    public void readButton_click(View view) {
        InputDialogBuilder.show(this, "Read", "Input user's ID", result -> {
            if (result != null && !result.isEmpty()) {
                int id = Integer.parseInt(result);

                // OK let's find the guy!
                readUser(id);
            }
        });
    }

    private void readUser(final int id) {
        User user = realm.where(User.class).equalTo("id", id).findFirst();
        if (user != null) {
            Log.d(TAG, String.format("readAllButton_click: #%d %s", user.getId(), user.getName()));
            Toast.makeText(MainActivity.this, "Read an user!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateAllButton_click(View view) {
        InputDialogBuilder.show(MainActivity.this, "Update", "Input new name for all users", result -> {
            if (result != null && !result.isEmpty()) {
                // OK beat it!
                updateAllUsers(result);
            }
        });
    }

    private void updateAllUsers(String name) {
        RealmResults<User> users = realm.where(User.class).findAll();

        realm.beginTransaction();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            user.setName(name);
        }

        realm.commitTransaction();

        Log.d(TAG, String.format("readAllButton_click: %d user(s).", users.size()));
        Toast.makeText(MainActivity.this, "Updated " + users.size() + " user(s)!", Toast.LENGTH_SHORT).show();
    }

    public void updateButton_click(View view) {
        InputDialogBuilder.show(this, "Update (1/2)", "Input user's ID", resultId -> {
            if (resultId != null && !resultId.isEmpty()) {
                final int id = Integer.parseInt(resultId);

                InputDialogBuilder.show(MainActivity.this, "Update (2/2)", "Input new name for the user #" + id, resultName -> {
                    if (resultName != null && !resultName.isEmpty()) {
                        // OK beat it!
                        updateUser(id, resultName);
                    }
                });
            }
        });
    }

    private void updateUser(final int id, final String name) {
        User user = realm.where(User.class).equalTo("id", id).findFirst();
        if (user != null) {
            realm.beginTransaction();
            user.setName(name);
            realm.commitTransaction();

            Log.d(TAG, String.format("updateUser: #%d %s", user.getId(), user.getName()));
            Toast.makeText(MainActivity.this, "Updated an user!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllButton_click(View view) {
        // OK kill'em all!
        deleteAllUsers();
    }

    private void deleteAllUsers() {
        RealmResults<User> users = realm.where(User.class).findAll();
        int count = users.size();
        realm.beginTransaction();
        for (User user : users) {
            user.deleteFromRealm();
        }
        realm.commitTransaction();

        Log.d(TAG, String.format("deleteAllUsers: %d user(s).", count));  // users.size() returns 0 now
        Toast.makeText(MainActivity.this, "Deleted " + count + " user(s)!", Toast.LENGTH_SHORT).show();
    }

    public void deleteButton_click(View view) {
        InputDialogBuilder.show(this, "Delete", "Input user's ID", result -> {
            if (result != null && !result.isEmpty()) {
                int id = Integer.parseInt(result);

                // OK search and destroy!
                deleteUser(id);
            }
        });
    }

    private void deleteUser(final int id) {
        User user = realm.where(User.class).equalTo("id", id).findFirst();
        if (user != null) {
            realm.beginTransaction();
            user.deleteFromRealm();
            realm.commitTransaction();

            Log.d(TAG, String.format("deleteUser: #%d", id));  // user.getId() and user.getName() throw IllegalStateException after deleting
            Toast.makeText(MainActivity.this, "Deleted an user!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
        }
    }
}
