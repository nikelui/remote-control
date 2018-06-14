package lb.remotecontrol;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Id of buttons
        Button mButton1 = findViewById(R.id.button1);
        Button mButton2 = findViewById(R.id.button2);
        Button mButton3 = findViewById(R.id.button3);

        // Debug: internal storage path
        Log.v("FilePath",getFilesDir().getAbsolutePath());


        // Check if the commands files exist. If not, create them from raw file at installation
        if(!appUtilities.isFilePresent(this,"commands1")) {
            ArrayList<String> commandList1 = appUtilities
                    .readRawCommands(this,"commands1");
            appUtilities.saveCommands(this,"commands1",commandList1); }

        if(!appUtilities.isFilePresent(this,"commands2")) {
            ArrayList<String> commandList1 = appUtilities
                    .readRawCommands(this,"commands2");
            appUtilities.saveCommands(this,"commands2",commandList1); }

        if(!appUtilities.isFilePresent(this,"commands3")) {
            ArrayList<String> commandList1 = appUtilities
                    .readRawCommands(this,"commands3");
            appUtilities.saveCommands(this,"commands3",commandList1); }


        // Reads IP and Port in app preferences. If they don't exist, returns default values
        String IpAddress = appUtilities.readAddress(this);
        int Port = appUtilities.readPort(this);


        // Save Address and Port in preferences
        appUtilities.saveAddress(this,IpAddress,Port);


        // Define the listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int click_id = v.getId();

                // Read IP Address and port from preferences
                String IpAddress = appUtilities.readAddress(MainActivity.super.getBaseContext());
                int Port = appUtilities.readPort(MainActivity.super.getBaseContext());

                switch (click_id) {
                    case R.id.button1:{
                        // stuff on button 1
                        // Read commands1 from file
                        ArrayList<String> commandList = appUtilities.readCommands(MainActivity
                                        .super.getBaseContext(),"commands1");

                        // Create parameters to pass to AsyncTask
                        AsyncParam asyncParam = new AsyncParam(IpAddress, Port, commandList);

                        UdpSend udpSend = new UdpSend();
                        udpSend.execute(asyncParam);

                        break; }

                    case R.id.button2:{
                        // stuff on button 2
                        // Read commands2 from file
                        ArrayList<String> commandList = appUtilities.readCommands(MainActivity
                                .super.getBaseContext(),"commands2");

                        // Create parameters to pass to AsyncTask
                        AsyncParam asyncParam = new AsyncParam(IpAddress,Port,commandList);

                        UdpSend udpSend = new UdpSend();
                        udpSend.execute(asyncParam);

                        break; }

                    case R.id.button3:{
                        // stuff on button 3
                        // Read commands3 from file
                        ArrayList<String> commandList = appUtilities.readCommands(MainActivity
                                .super.getBaseContext(),"commands3");

                        // Create parameters to pass to AsyncTask
                        final AsyncParam asyncParam = new AsyncParam(IpAddress,Port,commandList);

                        UdpSend udpSend = new UdpSend();
                        udpSend.execute(asyncParam);

                        break; }
                }
            }
        };

        // the listener is the same, the action depends on the button ID and is
        // defined in the onClick method.
        mButton1.setOnClickListener(listener);
        mButton2.setOnClickListener(listener);
        mButton3.setOnClickListener(listener);
    }

    // Inflate the admin menu
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Clicking the menu item generates an intent that calls the Login Activity
        Intent intent = new Intent(this, AdminLogin.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
    }


    // Class container to pass parameters to AsyncTask
    private class AsyncParam {
        String address;
        int port;
        ArrayList<String> commands;

        // Constructor
        AsyncParam(String address, int port, ArrayList<String> commands) {
            this.address = address;
            this.port = port;
            this.commands = commands;
        }
    }


    // use asyncTask to do network operations
    private static class UdpSend extends AsyncTask <AsyncParam, Void, Void> {
        @Override
        public Void doInBackground(AsyncParam... params) {
            // TODO: Does this work?
            if (isCancelled()) {
                Log.d("async", "ASYNC CANCELED!!!");
                return null;
            }

            ArrayList<String> commandsList = params[0].commands;

            DatagramSocket mSocket = null;
            try {mSocket = appUtilities.createUdpSocket();

                InetAddress remoteIP = InetAddress.getByName(params[0].address);
                int remotePort = params[0].port;

                mSocket.connect(remoteIP,remotePort); }
            catch (UnknownHostException hex) {Log.e("HostException",hex.toString());}
            catch(SocketException sex) {Log.e("SocketException",sex.toString());}

            // Creates and sends UDP packets one at time
            for (int i = 0;i < commandsList.size(); i++) {

                if(isCancelled()){
                    break; // stop execution on cancel
                }

                if (commandsList.get(i).split(" ")[0].equals("pause")) {
                    //TODO: implement pause command between instructions
                    // if the instruction begins with "pause", pause the thread for
                    // pause_time in ms. Instruction syntax: "pause N"
                    int pause_time = Integer.parseInt(commandsList.get(i)
                            .replaceAll("\n","").split(" ")[1]);
                    try {Thread.sleep(pause_time);}
                    catch (InterruptedException intex) {Log.e("Pause",intex.toString());}
                }else {
                    // Creates an UDP packet and sends it
                    try {DatagramPacket packet = appUtilities.createUdpPacket(commandsList.get(i),
                            params[0].address,params[0].port);
                        mSocket.send(packet); }
                    catch (UnknownHostException uex) {Log.e("",uex.toString());}
                    catch (IOException ioex) {Log.e("",ioex.toString());}
                }
            }
            mSocket.disconnect();
            mSocket.close();
            return null;
        }
    }
}
