/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Bob
 */
public class SceneManager extends AbstractAppState {
  
  private SimpleApplication app;  
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app = (SimpleApplication) app;
    initFloor();
    }
    
  public void initFloor() {
    Box box        = new Box(25,.1f,25);
    Geometry floor = new Geometry("The Floor", box);
    Material mat   = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    floor.setMaterial(mat);
    app.getRootNode().attachChild(floor);
    floor.setLocalTranslation(0,-.5f,0);
    }

  }
