package vktest.test.com.prototype;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class HomeActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        Firebase.setAndroidContext(this);
        Button studentUserButton = (Button) findViewById(R.id.student_button);
        Button sellerUserButton = (Button) findViewById(R.id.seller_button);

        studentUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        sellerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(getApplicationContext());
                final View textEntryView = factory.inflate(R.layout.dialog_view, null);

                final EditText loginField = (EditText) textEntryView.findViewById(R.id.login);
                final EditText passwordField = (EditText) textEntryView.findViewById(R.id.password);
                final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this, R.style.AppCompatAlertDialogStyle);

                alert.setTitle("Log In")
                        .setView(textEntryView)
                        .setPositiveButton("Log In",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/sellers");
                                        myFirebaseRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                            if(snapshot.child("login").getValue().toString().equals(loginField.getText().toString()) &&
                                                    snapshot.child("password").getValue().toString().equals(passwordField.getText().toString()))
                                            {
                                                  Intent i = new Intent(HomeActivity.this, SellerActivity.class);
                                                  startActivity(i);
                                            }
                                              else
                                              {
                                                  Toast.makeText(getApplicationContext(), "Log In Failed", Toast.LENGTH_SHORT).show();
                                              }

                                            }
                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {
                                                System.out.println("The read failed: " + firebaseError.getMessage());
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                });
                alert.show();
            }
        });



    }
}
