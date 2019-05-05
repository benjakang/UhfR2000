package com.hyipc.uhf_r2000.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hyipc.uhf_r2000.R;
import com.hyipc.uhf_r2000.hardware.function.UhfWrite;

import android.view.View;
import android.view.View.OnClickListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.Buffer;

public class UploadFragment extends Fragment {

    private EditText mEtServiceAddr, mEtServicePort;
    private TextView mTvDisplay;
    private Button mBtnUpload;
//    private Socket socket;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        super.onViewCreated(view, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_upload, null);

        mEtServiceAddr = (EditText) view.findViewById(R.id.etServiceAddr);
        mEtServicePort = (EditText) view.findViewById(R.id.etServicePort);
        mBtnUpload = (Button) view.findViewById(R.id.btnUpload);
        mTvDisplay = (TextView) view.findViewById(R.id.tvDisplay);

        mBtnUpload.setOnClickListener(onClickListener);

        return view;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            upload();
        }
    };

    private void upload() {
        mTvDisplay.setText("上传按钮有效哦\n");
//        geted1 = ed1.getText().toString();
        mTvDisplay.append("开始启动连接线程"+"\n");
        //启动线程 向服务器发送和接收信息
        new UploadThread().start();
    }

    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                Bundle bundle = msg.getData();
                mTvDisplay.append("from server:"+bundle.getString("msg")+"\n");
            }
        }

    };


    class UploadThread extends Thread {

        public String txt1 = null;

        public UploadThread(String str) {
            txt1 = str;
        }
        public UploadThread() {
        }

        @Override
        public void run() {
            //定义消息
            Message msg = myHandler.obtainMessage();
            msg.what = 0x11;
            Bundle bundle = new Bundle();
            bundle.clear();
            try {
                int length = 0;
                //连接服务器 并设置连接超时为5秒
                Socket socket = new Socket();
                InetSocketAddress socketAddress = new InetSocketAddress(
                        InetAddress.getByName(mEtServiceAddr.getText().toString()),
                        Integer.parseInt(mEtServicePort.getText().toString()));
                socket.connect(socketAddress, 5000);
                //获取输入输出流
                DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        .getAbsolutePath() + "/超高频数据" + "/uhf.txt");
                FileInputStream fin = new FileInputStream(file);

                BufferedReader bff = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                //读取发来服务器信息(输入)
                String line = null;
                String buffer="";
                while ((line = bff.readLine()) != null) {
                    buffer = line + buffer;
                }

                bundle.putString("msg", buffer.toString());
                msg.setData(bundle);
                //修改UI线程中的组件
                myHandler.sendMessage(msg);

                //向服务器发送信息（输出）
                byte[] sendByte = new byte[1024];
                dout.writeUTF(file.getName());
                while((length = fin.read(sendByte, 0, sendByte.length))>0){
                    dout.write(sendByte,0,length);
                    dout.flush();
                }

                //关闭各种输入输出流
                bff.close();
                dout.close();
                socket.close();
            } catch (SocketTimeoutException aa) {
                //连接超时 在UI界面显示消息
                bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
                msg.setData(bundle);
                //发送消息 修改UI线程中的组件
                myHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
