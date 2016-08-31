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
 * 在程序中把一个软件的快捷方式添加到桌面上，只需要如下三步：
 * 1、创建一个添加快捷方式的Intent，该Intent的Action属性值应该为com.android.launcher.action.INSTALL_SHORTCUT。
 * 2、通过为该Intent的Extra属性来设置快捷方式的标题、图标以及快捷方式对应启动的程序。
 * 3、调用sendBroadcast()方法发送广播即可添加快捷方式。
 */
public class AddShortcutTest extends Activity {
	
	private ImageView flower;
	Animation animation, reverse; //定义两份动画资源
	
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
        
        animation = AnimationUtils.loadAnimation(this, R.anim.animation); //加载第一份动画资源
        animation.setFillAfter(true); //设置动画结束后保留结束状态
        
        reverse = AnimationUtils.loadAnimation(this, R.anim.reverse); //加载第二份动画资源
        reverse.setFillAfter(true);  //设置动画结束后保留结束状态
        
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");//创建添加快捷方式的Intent
				String title = getResources().getString(R.string.title);
				
				Parcelable icon = Intent.ShortcutIconResource.fromContext(AddShortcutTest.this, R.drawable.ic_launcher);//加载快捷方式的图标
				Intent myIntent = new Intent(AddShortcutTest.this, AddShortcutTest.class);//创建点击快捷方式后操作Intent，该处当点击创建的快捷方式后，再次启动该程序
				
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);//设置快捷方式的标题
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);//设置快捷方式的图标
				
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent); //设置快捷方式对应的Intent
				sendBroadcast(addIntent); //发送广播添加快捷方式
			}
		});
    }
    
    @Override
    public void onResume() 
    {
    	super.onResume();
    	
    	flower.startAnimation(animation); //开始执行动画
    	
    	//设置3.5秒后启动第二个动画
    	new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				handler.sendEmptyMessage(0x123);
			}
		}, 3500);
    }
}