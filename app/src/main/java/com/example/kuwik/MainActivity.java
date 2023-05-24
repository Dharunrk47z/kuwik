package com.example.kuwik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    EditText ipAddress, e2;

    static MyClient myClient = null;

    Button connectButton, disconnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ipAddress = findViewById(R.id.ipAddress);

        connectButton = (Button) findViewById(R.id.connectButton);
        disconnectButton = (Button) findViewById(R.id.disconnectButton);

    }

    public void connect(View v) {

        myClient = new MyClient(this);
        boolean conStatus;
        connectButton.setClickable(false);
        if (!ipAddress.getText().toString().equals("")) {
            myClient.connectToServer(ipAddress.getText().toString(), 7800);
            try {
                Thread.sleep(1000);
                if (myClient.socket != null) {

                    System.out.println("server connected");
                    //sender = new Sender(myClient);    uncomment
                    connectButton.setText("Connected");
                    Intent sendIntent = new Intent(MainActivity.this,CommunicationActivity.class);

                    startActivity(sendIntent);

                } else {
                    Toast.makeText(getApplicationContext(), "No Receiver Available", Toast.LENGTH_SHORT).show();
                    connectButton.setText("Connect");
                    connectButton.setClickable(true);
                }

            } catch (NullPointerException ne) {

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else
            Toast.makeText(getApplicationContext(), "Please enter a valid ip!", Toast.LENGTH_SHORT).show();

    }


    public void disconnect(View view) {
        if (myClient != null){
            myClient.disconnect();
            myClient = null;
            connectButton.setText("Connect");
            connectButton.setClickable(true);
            return;
        }
        else{
            Toast.makeText(getApplicationContext(), "Not Connected to Any receiver", Toast.LENGTH_SHORT).show();
        }
    }


}