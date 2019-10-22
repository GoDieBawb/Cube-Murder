/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigbawb.cubeeater;

import android.app.Activity;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import mygame.AndroidManager;

/**
 *
 * @author Bob
 */
public class ActivityManager extends AbstractAppState {

  private SimpleApplication  app;
  private AppStateManager    stateManager;
  private AssetManager       assetManager;
  private String             filePath;
  private AndroidManager     androidManager;
  private MainActivity       activity;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    System.out.println("ActivityManager attached");
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    app.getStateManager().attach(new AndroidManager());  
    androidManager    = app.getStateManager().getState(AndroidManager.class);
    setAndroidPath();
    }
  
  public void setActivity(Activity activity){
    this.activity = (MainActivity) activity;
    }
  
  public void setAndroidPath(){
    androidManager.setFilePath(activity.getFilesDir().toString());
    }
  
  @Override
  public void update(float tpf) {
    
    }
  
  }
