package com.mhw666.my_port_190504;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.io.Ports.SerialPort;

public class MainActivity extends AppCompatActivity {
    EditText edtR,edtW;
    Button btnSe,btnCl;
    SerialPort com;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtR=findViewById(R.id.editText2);
        edtW=findViewById(R.id.editText);
        btnSe=findViewById(R.id.button);
        btnCl=findViewById(R.id.button2);
        btnCl.setOnClickListener(view -> {
            try {
                com.Close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        });
        btnSe.setOnClickListener(view -> {
            try {
                String str="\n"+edtW.getText().toString().trim();
                Message msg=new Message();
                msg.obj=str;
                handler.sendMessage(msg);
                byte[] by=str.getBytes("GBK");
                com.Send(by);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try {
            com=new SerialPort("/dev/ttySAC1",115200);
            boolean b=com.Open();
            Message msg2=new Message();

            msg2.obj="串口打开"+(b?"成功":"失败");
            handler.sendMessage(msg2);
            if(b){
                com.setOnDataReceiveListener((bytes, i) -> {
                    try {
                        byte[] by=new byte[i];
                        System.arraycopy(bytes,0,by,0,i);
                        String str=new String(by,"GBK");
                        Message msg3=new Message();
                        msg3.obj=str;
                        handler.sendMessage(msg3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            try {
                edtR.getText().append((String)message.obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });
}
