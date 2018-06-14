package lb.remotecontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public final class appUtilities {

    // Various Utilities and dummy data

    // Dummy credentials. password: admin
    public static String PASSWORD = "admin";

    public static boolean isFilePresent(Context context, String fileName) {
        // To check if files are already in internal storage or need to be created
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }


    public static ArrayList<String> readRawCommands(Context context, String filename) {
        // Reads data from raw text file. Used for default values at installation

        ArrayList<String> commandList = new ArrayList<>();
        int rawID = context.getResources().getIdentifier(filename, "raw",
                context.getPackageName());
        InputStream rawStream = context.getResources().openRawResource(rawID);
        InputStreamReader rawReader = new InputStreamReader(rawStream);
        BufferedReader rawBufReader = new BufferedReader(rawReader);
        String currentLine;

        try {
            while ((currentLine = rawBufReader.readLine()) != null) {
                commandList.add(currentLine); }
        } catch (IOException ioex) {Log.e("Raw reader",ioex.toString());}
        return commandList;
    }

    public static ArrayList<String> readCommands(Context context, String filename) {
        ArrayList<String> commandsList = new ArrayList<>(); // initialize empty ListArray
        File file = new File(context.getFilesDir(),filename); // File Object
        try {
            FileReader reader = new FileReader(file); // File object
            BufferedReader bufReader = new BufferedReader(reader); // Reader
            String currentLine; // String to store read line

            while((currentLine = bufReader.readLine()) != null) {
                commandsList.add(currentLine+"\n");
            }
        } catch(Exception ex) {Log.e("File open",ex.toString());}

        return commandsList;
    }

    public static void saveCommands(Context context, String filename, ArrayList<String> commandList) {
        // saves commands in internal storage
        File file = new File(context.getFilesDir(),filename); // File Object
        try {
            FileWriter writer = new FileWriter(file);
            for (String command: commandList) {
                writer.write(command + "\n");
            }
            writer.close();
        } catch(IOException ioex) {Log.e("Write file",ioex.toString());}
    }

    public static void saveAddress(Context context, String Ip,int Port) {
        // Saves IP address and port on shared preferences
        SharedPreferences IpPref = context.getSharedPreferences("address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = IpPref.edit();
        editor.putString("IP", Ip);
        editor.putInt("Port", Port);
        editor.apply(); }

    public static String readAddress(Context context) {
        SharedPreferences IpPref = context.getSharedPreferences("address",Context.MODE_PRIVATE);
        String defaultValue = "10.0.2.2";
        // Return default value if "IP" key does not exist
        String IpAddress = IpPref.getString("IP",defaultValue);
        return IpAddress;
    }

    public static int readPort(Context context) {
        SharedPreferences IpPref = context.getSharedPreferences("address",Context.MODE_PRIVATE);
        int defaultValue = 10002;
        // Return default value if "Port" key does not exist
        int Port = IpPref.getInt("Port",defaultValue);
        return Port;
    }

    public static DatagramSocket createUdpSocket()
            throws UnknownHostException,SocketException {

        DatagramSocket socket = new DatagramSocket();
        return socket;
    }

    public static DatagramPacket createUdpPacket(String command,
                     String address, int port) throws UnknownHostException {

        byte[] buf = command.getBytes();
        InetAddress IP = InetAddress.getByName(address);

        DatagramPacket packet = new DatagramPacket(buf,buf.length,IP,port);

        return packet;
    }

}
