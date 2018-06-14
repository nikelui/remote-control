package lb.remotecontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        final EditText mEditTextPassword = findViewById(R.id.editTextPassword);
        Button mLoginButton = findViewById(R.id.loginButton);
        //TextView mLoginErrorMessage = findViewById(R.id.loginErrorMessage);

        hideErrorMessage(); // Initially hide the error message

        // Define the listener for Login button
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordCheck(mEditTextPassword.getText().toString());
                mEditTextPassword.setText(""); // clears password fields on "login" press
            }
        };
        mLoginButton.setOnClickListener(listener);

    }

    @Override
    public void onBackPressed() {
        // Clears password field on back press
        super.onBackPressed();
        EditText mEditTextPassword = findViewById(R.id.editTextPassword);
        mEditTextPassword.setText("");
        this.finish();
    }


    public void hideErrorMessage () {
        // Hide Error message and show login form
        findViewById(R.id.loginErrorMessage).setVisibility(View.INVISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
        findViewById(R.id.editTextPassword).setVisibility(View.VISIBLE);
    }

    public void showErrorMessage () {
        // Show error message and hide login form
        findViewById(R.id.loginErrorMessage).setVisibility(View.VISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.editTextPassword).setVisibility(View.INVISIBLE);
    }

    public void passwordCheck(String pw) {
        /*// Debug ---- Start
        Log.v("Password Log","Password: " + PASSWORD
                +" // User password: " + pw);
        // Debug ---- End */

        if (pw.equals(appUtilities.PASSWORD)) {
            //Toast toast = Toast.makeText(this,"Correct!",Toast.LENGTH_SHORT);
            //toast.show();

            // If password is correct, start an Intent to call AdminActivity
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            this.finish();

        } else {
            showErrorMessage();
        }
    }
}
