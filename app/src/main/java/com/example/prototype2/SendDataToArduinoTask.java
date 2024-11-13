package com.example.prototype2;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;


import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;

public class SendDataToArduinoTask extends AppCompatActivity {
    private Button button2, timeButton;
    private TextView value;
    int mHour, mMin;
    String time;
    int count = 0;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        value = (TextView) findViewById(R.id.value);
            button2 = (Button) findViewById(R.id.button2);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMain();
                }
            });

        timeButton = findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                TimePickerDialog timePickerDialog = new TimePickerDialog(SendDataToArduinoTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute > 9) {
                            time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                        } else {
                            time = String.valueOf(hourOfDay) + ":0" + String.valueOf(minute);
                        }
                        timeButton.setText(time);
                        mHour = hourOfDay;
                        mMin = minute;
                    }
                }, mHour, mMin, true);
                timePickerDialog.show();
            }
        });

        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                if(mMin<10){
                    if(mHour<10){
                        message = (1+",0"+mHour+",0"+mMin+","+"01,"+"1,"+"7,"+count);
                    }
                    else{
                        message = (1+","+mHour+",0"+mMin+","+"01,"+"1,"+"7,"+count);
                    }

                }
                else{
                    if(mHour<10){
                        message = (1+",0"+mHour+","+mMin+","+"01,"+"1,"+"7,"+count);
                    }
                    else{
                        message = (1+","+mHour+","+mMin+","+"01,"+"1,"+"7,"+count);
                    }
                }
                SendMessageTask task = new SendMessageTask();
                task.execute(message);

                System.out.println(message);
            }
        });
    }




    private class SendMessageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                String message = params[0];
                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName("172.20.10.5"); // Replace "Arduino_IP_Address" with the actual IP address of your Arduino Uno WiFi
                int port = 8080; // Port to which Arduino is listening

                byte[] buf = message.getBytes();

                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                Log.d("UDP", "Sent: " + message);

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void increment(View v) {
        count++;
        value.setText("" + count);
    }
    public void decrement(View v) {
        if (count <= 0) count = 0;

        else count--;
        value.setText("" + count);

    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
