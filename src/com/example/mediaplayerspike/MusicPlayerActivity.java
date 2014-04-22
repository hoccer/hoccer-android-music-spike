package com.example.mediaplayerspike;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;

public class MusicPlayerActivity extends Activity {
	
	private static final String LOG_TAG=MusicPlayerActivity.class.getSimpleName();

	private int mId = 1;
	
	private AudioManager mAudioManager;
	private MediaPlayer mMediaPlayer;
	private Notification.Builder mBuilder;
	
	private OnAudioFocusChangeListener mAudioFocusChangeListener = new OnAudioFocusChangeListener() {
		
	    public void onAudioFocusChange(int focusChange) {
	    	
	    	Log.d(LOG_TAG,  "AUDIO FOCUS CHANGED: " + focusChange);
	    	
	        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
	        	Log.d("bla",  "AUDIOFOCUS_LOSS_TRANSIENT");
	        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
	        	Log.d("bla",  "AUDIOFOCUS_GAIN");
	        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
	        	Log.d("bla",  "AUDIOFOCUS_LOSS");
                
	        } else if( focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
	        	Log.d("bla",  "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
	        }
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musicplayer);
		
		mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	}
	
	private void createNotification(){

    	final Intent emptyIntent = new Intent(this, MusicPlayerActivity.class);
    	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, emptyIntent, 0);
    	
    	mBuilder = new Notification.Builder(this)
    	        .setSmallIcon(R.drawable.logo)
    	        .setContentTitle("Awesome Author")
    	        .setContentText("Awesome Track")
    		    .setContentIntent(pendingIntent)
		        .setAutoCancel(false)
		        .addAction(R.drawable.btn01, "Click", pendingIntent)
		        .addAction(R.drawable.btn02, "Me", pendingIntent)
		        .addAction(R.drawable.btn03, "Please", pendingIntent);
    	
    	mBuilder.setPriority(100);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.notify(mId, mBuilder.build());
	}
	
	public void onButtonPlay( View view){

    	int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
    	                                 // Use the music stream.
    	                                 AudioManager.STREAM_MUSIC,
    	                                 // Request permanent focus.
    	                                 AudioManager.AUDIOFOCUS_GAIN);
    	   
    	if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
    	
        	createNotification();
        	
        	mMediaPlayer = MediaPlayer.create(this, R.raw.frei_wild_weil_du_mich_nur_verarscht_hast);
        	mMediaPlayer.start();
        	mMediaPlayer.setVolume(1.0f, 1.0f);
    	}
    	else{
    		Log.i(LOG_TAG, "Audio focus request not granted");
    	}
	}
	
	public void onButtonStop(View view){
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(mId);
		
		// Abandon audio focus when playback complete    
		mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
		
		//TODO! calling these crashes the app
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
	}
}
