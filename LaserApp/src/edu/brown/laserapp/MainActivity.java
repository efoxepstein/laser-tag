package edu.brown.laserapp;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import edu.brown.gamelogic.GameLogic;

public class MainActivity extends Activity {

	private Camera mCamera;
    private CameraPreview mPreview;

    private GameLogic engine;
    private boolean cameraReady;
    
//    // Luqi: My hack on acceleration
//    private SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//    private Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//    //don't know how to hook mSensor with the SensorEvent
//    private float[] linear_acceleration = new float[3];
//    
//    public void onSensorChanged(SensorEvent event) {
//    	
//    	linear_acceleration[0] = event.values[0];
//    	linear_acceleration[1] = event.values[1];
//    	linear_acceleration[2] = event.values[2];
//    	
//    }
//    
//    // Luqi END
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("ELI", "onCreate fired");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);

		hideUi();
		
		try {
			mCamera = Camera.open();
		} 
		catch (Exception e){}
		
		if (mCamera == null) {
			Log.e("ERR", "camera instance not created!");
			cameraReady = false;
		} else {
			Log.d("ELI", "Got camera");
	        mPreview = new CameraPreview(this, mCamera);
	        cameraReady = true;
	        
	        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	        preview.addView(mPreview);
		}
        
        engine = new GameLogic(this);
	}
	
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideUi();
    }
	
	@Override
	protected void onDestroy(){ if (mCamera != null) mCamera.release(); super.onDestroy(); }
	
	public void takePicture(){		
		Log.d("ELI", "FIRING!");
		
		if (cameraReady) {
			cameraReady = false;

			mCamera.takePicture(null, null, new PictureCallback() {
			    @Override
			    public void onPictureTaken(byte[] data, Camera camera) {
			    	camera.startPreview();
			    	cameraReady = true;
			    	Log.d("ELI", "picture taken");
			    	engine.convert(data);
			    }
			});
		}
	}
	
	public void setKillsText(String s){ getTextView(R.id.kills).setText(s); }
	public void setDeathsText(String s){ getTextView(R.id.deaths).setText(s); }
	public void vibrate(long millis) {
		Log.d("ELI", "trying to vibrate");
		Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if (vibe != null) vibe.vibrate(millis);
	}
	
	private TextView getTextView(int id) { return (TextView) findViewById(id); }
    private void hideUi(){
	    getActionBar().hide();                                 
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    					 WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);	
    }  
}
