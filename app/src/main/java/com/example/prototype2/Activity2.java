package com.example.prototype2;

import androidx.appcompat.app.AppCompatActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;

public class Activity2 extends AppCompatActivity {
    private Button button2, buttonSend, timeButton;
    private TextView value;
    int mHour, mMin;
    String time;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
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

                Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMin = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Activity2.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute > 9) {
                            time = (hourOfDay) + ":" + String.valueOf(minute);
                        } else {
                            time  = (hourOfDay) + ":0" + String.valueOf(minute);
                        }

                        timeButton.setText(time);

                    }
                }, mHour, mMin, true);
                timePickerDialog.show();

            }
        });
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageTask task = new SendMessageTask();
                task.execute((String.valueOf(mHour)));

            }
        });

    }

    public void increment(View v) {
        count++;
        value.setText("" + count);
    }
    private class SendMessageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                String message = params[0];
                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName("130.15.36.203"); // Replace "Arduino_IP_Address" with the actual IP address of your Arduino Uno WiFi
                int port = 8888; // Port to which Arduino is listening

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