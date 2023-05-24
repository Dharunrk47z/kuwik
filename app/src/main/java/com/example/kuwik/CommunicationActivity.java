package com.example.kuwik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CommunicationActivity extends AppCompatActivity {
    EditText ipAddress, e2;
    Sender sender;
    MyClient myClient = null;
    String filename;
    long fileSize;
    Uri fileUri;
    Button connectButton, disconnectButton;
    FileInputStream file;
    int fileChosenFlag = 0;
    // Request code for selecting a PDF document.
    int requestCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        sender = new Sender(MainActivity.myClient);
        e2 = (EditText) findViewById(R.id.message);
    }


    public void sendFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Opening ");
                try {
                    file = (FileInputStream) getContentResolver().openInputStream(fileUri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Opening the file: ");
                System.out.println("Filename : " + filename + "size :" + Long.toString(fileSize));
                if (sender == null) {
                    System.out.println("Sender is null");
                } else {
                    sender.sendFile(file, filename, fileSize);

                }
            }
        }).start();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            fileUri = data.getData();
            Cursor returnCursor =
                    getContentResolver().query(fileUri, null, null, null, null);
            /*
             * Get the column indexes of the data in the Cursor,
             * move to the first row in the Cursor, get the data,
             * and display it.
             */
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();

            filename = returnCursor.getString(nameIndex);
            fileSize = returnCursor.getLong(sizeIndex);

            Context context = getApplicationContext();
            //EditText fileLabel = (EditText) findViewById(R.id.fileURI);

            Toast.makeText(context, this.fileUri.getPath(), Toast.LENGTH_SHORT).show();
        }
    }
    // Request code for selecting a PDF document.

    public void openFileChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, requestCode);
        fileChosenFlag = 1;


    }

    public void sendMessage(View v) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(fileChosenFlag == 1){
                        fileChosenFlag = 0;
                        sendFile();
                    }
                    else{
                        boolean isSuccess = sender.send(e2.getText().toString());
                    }

                } catch (Exception e) {
                }
            }
        }) {
        }.start();
    }

    public void sendClipBoard(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData cd = clipboard.getPrimaryClip();
                    System.out.println("Hellloooo ");
                    String clipContent = cd.getItemAt(0).getText().toString();

                    System.out.println("Converted");
                    System.out.println(clipContent);
                    sender.sendClipBoard(clipContent);
                } catch (Exception e) {
                }
            }
        }) {
        }.start();
    }

}