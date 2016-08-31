package com.example.addshortcuttest;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/*
 * �ڳ����а�һ������Ŀ�ݷ�ʽ��ӵ������ϣ�ֻ��Ҫ����������
 * 1������һ����ӿ�ݷ�ʽ��Intent����Intent��Action����ֵӦ��Ϊcom.android.launcher.action.INSTALL_SHORTCUT��
 * 2��ͨ��Ϊ��Intent��Extra���������ÿ�ݷ�ʽ�ı��⡢ͼ���Լ���ݷ�ʽ��Ӧ�����ĳ���
 * 3������sendBroadcast()�������͹㲥������ӿ�ݷ�ʽ��
 */
public class AddShortcutTest extends Activity {
	
	private ImageView flower;
	Animation animation, reverse; //�������ݶ�����Դ
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			if (msg.what == 0x123)
			{
				flower.startAnimation(reverse);
			}
		}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        flower = (ImageView)findViewById(R.id.flower);
        
        animation = AnimationUtils.loadAnimation(this, R.anim.animation); //���ص�һ�ݶ�����Դ
        animation.setFillAfter(true); //���ö���������������״̬
        
        reverse = AnimationUtils.loadAnimation(this, R.anim.reverse); //���صڶ��ݶ�����Դ
        reverse.setFillAfter(true);  //���ö���������������״̬
        
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");//������ӿ�ݷ�ʽ��Intent
				String title = getResources().getString(R.string.title);
				
				Parcelable icon = Intent.ShortcutIconResource.fromContext(AddShortcutTest.this, R.drawable.ic_launcher);//���ؿ�ݷ�ʽ��ͼ��
				Intent myIntent = new Intent(AddShortcutTest.this, AddShortcutTest.class);//���������ݷ�ʽ�����Intent���ô�����������Ŀ�ݷ�ʽ���ٴ������ó���
				
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);//���ÿ�ݷ�ʽ�ı���
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);//���ÿ�ݷ�ʽ��ͼ��
				
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent); //���ÿ�ݷ�ʽ��Ӧ��Intent
				sendBroadcast(addIntent); //���͹㲥��ӿ�ݷ�ʽ
			}
		});
    }
    
    @Override
    public void onResume() 
    {
    	super.onResume();
    	
    	flower.startAnimation(animation); //��ʼִ�ж���
    	
    	//����3.5��������ڶ�������
    	new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				handler.sendEmptyMessage(0x123);
			}
		}, 3500);
    }
}