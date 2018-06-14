package lb.remotecontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // ID of the linear layout inside the ScrollView
        final LinearLayout mLinearLayout = findViewById(R.id.linearLayout);

        // Extract the filename from the Intent
        String filename = getIntent().getExtras().getString("filename");

        // Read commandList directly from File "filename" instead of passing it inside a bundle
        ArrayList<String> commandList = appUtilities.readCommands
                (EditActivity.super.getBaseContext(),filename);

        // Onclick listener for button presses
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = v.getId();

                /*// Debug -> WORKS :D
                Toast toast = Toast.makeText(getApplicationContext(),
                        "ID = "+ String.valueOf(ID), Toast.LENGTH_SHORT);
                toast.show();*/

                // Retrieve row number and button type from ID
                int rowNumber = ID/2;
                String type = "delete"; // Default value to avoid compiler error
                if((ID % 2) == 0) {type = "delete";}
                else if ((ID % 2) == 1) {type = "add";}

                // Retrieve current commandList
                ArrayList<String> currentCommands = getCommandsList(mLinearLayout);
                if(type.equals("add")) {
                    currentCommands.add(rowNumber+1,"\n"); // Add a line after
                } else if (type.equals("delete")) {
                    currentCommands.remove(rowNumber); // Delete current line
                }

                // Update the ViewGroup: call createCommandList
                ArrayList<View> newCommandsList = createCommandsList(mLinearLayout,
                        currentCommands,this);
                // Update the ViewGroup: call updateCommandList
                updateCommandsList(mLinearLayout,newCommandsList);
            }
        };

        // Call the functions to read the commands, create an array of views
        // and attach them to the Linear Layout
        ArrayList viewsList = createCommandsList(mLinearLayout, commandList,listener);
        updateCommandsList(mLinearLayout, viewsList);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Read current commands and save them on exit
        String filename = getIntent().getExtras().getString("filename");
        LinearLayout mLinearLayout = findViewById(R.id.linearLayout);

        ArrayList<String> commandsList = getCommandsList(mLinearLayout);
        appUtilities.saveCommands(this,filename,commandsList);
        this.finish();
    }

    // Inflate the save menu
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Clicking the menu item calls the getCommandsList method and saves
        // the commands on file
        String filename = getIntent().getExtras().getString("filename");
        LinearLayout mLinearLayout = findViewById(R.id.linearLayout);

        ArrayList<String> commandsList = getCommandsList(mLinearLayout);
        appUtilities.saveCommands(this,filename,commandsList);

        return super.onOptionsItemSelected(item);
    }


    private void updateCommandsList(ViewGroup rootView, ArrayList viewsList) {
        // The Views inside the viewsList are appended to the root Layout
        // use removeAllViews() to clear the layout first

        rootView.removeAllViews(); // Clear the layout

        for(int counter = 0; counter < viewsList.size();counter++) {
            View toAdd = (View) viewsList.get(counter);
            rootView.addView(toAdd);
        }
    }


    private ArrayList<View> createCommandsList(ViewGroup rootView, ArrayList<String> commands,
                                               View.OnClickListener listener){

        // Inflates the line_edit_layout for each command and save it in an ArrayList

        ArrayList <View> viewsList = new ArrayList<>(); // To store the views

        for (int counter = 0; counter < commands.size(); counter++) {
            // Inflate layout
            LayoutInflater inflater = LayoutInflater.from(EditActivity.this);
            View inflatedLine = inflater.inflate(R.layout.line_edit_layout,
                    rootView,false); // set attachToRoot false and add View

            // Get View elements by ID
            TextView mCommandNumber = inflatedLine.findViewById(R.id.command_index);
            EditText mCommandLine = inflatedLine.findViewById(R.id.editText_command);
            ImageButton mButtonDelete = inflatedLine.findViewById(R.id.editButton_delete);
            ImageButton mButtonAdd = inflatedLine.findViewById(R.id.editButton_add);

            // Assign View properties
            String index = String.format(Locale.getDefault(),"%02d",counter+1);
            mCommandNumber.setText(index);
            mButtonDelete.setId(counter*2); // Even numbers are "delete" buttons
            mButtonAdd.setId(counter*2 + 1); // Odd numbers are "add" buttons

            mButtonDelete.setOnClickListener(listener);
            mButtonAdd.setOnClickListener(listener);

            mCommandLine.setText(commands.get(counter));
            mCommandLine.setId(counter + 1000); // Setting unique ID for EditTexts.
            // This assures that EditText retain their content when screen is rotated.
            // Adds 1000 to counter to avoid conflicts with the buttons ID (this way up
            // to 500 commands can be saved without errors). If more commands are needed,
            // consider to redo the logic of ID assignment (3 per row instead of 2 per row).

            viewsList.add(counter,inflatedLine); // Add inflated view to ArrayList
        }
        return viewsList;
    }

    private ArrayList<String> getCommandsList(ViewGroup rootView) {
        // Reads the commands in each EditText and returns them inside an ArrayList
        // Used together with appUtilities.saveCommands to save current commands on file

        // Save all "rows" Views inside an ArrayList
        ArrayList<ViewGroup> rowViews = new ArrayList<>();
        for (int i=0; i<rootView.getChildCount();i++) {
            rowViews.add(i,((ViewGroup)rootView.getChildAt(i))); }

        // Child indexes -> 0:command_index,        1:EditText_command,
        //                  2:ImageButton:delete    3:ImageButton:add
        ArrayList<String> commandsList = new ArrayList<>();
        for (int i=0; i<rowViews.size();i++) {
            String command = ((EditText)rowViews.get(i).getChildAt(1)).getText().toString();
            command = command.replace("\n",""); //Strip newline
            commandsList.add(i,command);
            // Debug
            Log.i("ChildCommand",command);
        }

        return commandsList;
    }
}
