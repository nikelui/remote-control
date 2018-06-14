package lb.remotecontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Id of buttons
        Button mEditButton1 = findViewById(R.id.edit_button1);
        Button mEditButton2 = findViewById(R.id.edit_button2);
        Button mEditButton3 = findViewById(R.id.edit_button3);

        //Id of EditText
        final EditText IpEdit = findViewById(R.id.editTextIP);
        final EditText PortEdit = findViewById(R.id.editTextPort);

        // Retrieve IP and Port from preferences
        String IpAddress = appUtilities.readAddress(this);
        int Port = appUtilities.readPort(this);

        // write on EditTexts
        IpEdit.setText(IpAddress);
        PortEdit.setText(String.valueOf(Port));

        // Define the listener for the EditText (on focus change)
        View.OnFocusChangeListener focusChange = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if EditText loses focus, save IP and Port
                if (!hasFocus) {
                    String newIP = IpEdit.getText().toString();
                    int newPort = (Integer.valueOf(PortEdit.getText().toString()));
                    appUtilities.saveAddress(AdminActivity.super.getBaseContext(),newIP,newPort);
                }
            }
        };

        // Define the listener for the Edit Buttons
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int click_id = v.getId();

                // the switch checks the button ID and passes the appropriate
                // commands list in the Intent
                switch (click_id) {
                    case R.id.edit_button1: {
                        // stuff on button 1
                        Intent intent = new Intent(AdminActivity.this,
                                EditActivity.class);

                        // Pass the filename as a String in the intent
                        String filename = "commands1";
                        intent.putExtra("filename",filename);

                        startActivity(intent);

                        break; }

                    case R.id.edit_button2: {
                        // stuff on button 2
                        Intent intent = new Intent(AdminActivity.this,
                                EditActivity.class);

                        // Pass the filename as a String in the intent
                        String filename = "commands2";
                        intent.putExtra("filename",filename);

                        startActivity(intent);

                        break; }

                    case R.id.edit_button3:  {
                        // stuff on button 3
                        Intent intent = new Intent(AdminActivity.this,
                                EditActivity.class);

                        // Pass the filename as a String in the intent
                        String filename = "commands3";
                        intent.putExtra("filename",filename);

                        startActivity(intent);

                        break; }
                }
            }
        };

        // the listener is the same, the action depends on the button ID and is
        // defined in the onClick method.
        mEditButton1.setOnClickListener(listener);
        mEditButton2.setOnClickListener(listener);
        mEditButton3.setOnClickListener(listener);
    }

    // Override the behaviour of back button: 1. Save IP and Port;
    // 2. Call an intent to go back to MainActivity (instead of AdminLogin)
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Id of EditText
        EditText IpEdit = findViewById(R.id.editTextIP);
        EditText PortEdit = findViewById(R.id.editTextPort);

        // Save Ip and Port
        String newIP = IpEdit.getText().toString();
        int newPort = (Integer.valueOf(PortEdit.getText().toString()));
        appUtilities.saveAddress(AdminActivity.super.getBaseContext(),newIP,newPort);

        // Go back to MainActivity
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
