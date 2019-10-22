/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.collision.CollisionResults;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.JmeSystem;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bob
 */
public class PlayerManager extends AbstractAppState {
  
  private SimpleApplication app;  
  private Node              player;
  private Node              cubeNode;
  private float             playerSize;
  public  int               cubesEaten;
  public  int               highScore;
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app  = (SimpleApplication) app;
    cubeNode  = app.getStateManager().getState(CubeManager.class).cubeNode;
    highScore = readScore(stateManager);
    initPlayer();
    }
    
  public void initPlayer(){
    Box box         = new Box(1,1,1);
    Geometry model  = new Geometry("Player", box);
    player          = new Node("Model");
    playerSize      = .3f;
    Material mat    = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color",ColorRGBA.Orange);
    model.setMaterial(mat);
    player.attachChild(model);
    app.getRootNode().attachChild(player);
    scalePlayer();
    }
  
  private void scalePlayer(){
    player.setLocalScale(playerSize);
      System.out.println("New Size: " + playerSize);
    }
  
  private void lose(){
    CubeManager cm = app.getStateManager().getState(CubeManager.class);
    playerSize     = .3f;
    scalePlayer();
    app.getStateManager().getState(InteractionManager.class).dead = true;
    app.getStateManager().getState(GuiManager.class).showTitle();
    app.getStateManager().getState(GuiManager.class).titleDisplay.setText("You Lose");
    app.getStateManager().getState(GuiManager.class).hideJoystick();
    cm.setEnabled(false);
    cm.cubeNode.detachAllChildren();
    
    if (cubesEaten > highScore) {
      saveScore(cubesEaten, app.getStateManager());
      highScore = cubesEaten;
      }
    
    }
  
  public void saveScore(int newScore, AppStateManager stateManager) {
    
    String filePath;  
      
    try {  
      filePath         = stateManager.getState(AndroidManager.class).filePath;
      }
    
    catch (NullPointerException e){
      filePath = JmeSystem.getStorageFolder().toString();
      }
    
    BinaryExporter exporter = BinaryExporter.getInstance();
    Node score              = new Node();
    score.setUserData("Name", "Hope");
    score.setUserData("Score", newScore);
    File file               = new File(filePath + "/score.j3o");
    
    System.out.println("Saving Score");
    
    try {
        
      exporter.save(score, file);  
      System.out.println("Score saved to: " + filePath);
        
      }
    
    catch (IOException e) {
        
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error: Failed to save game!", e);  
        System.out.println("Failure");
      }
    
      System.out.println("score completion");
    
    }
  
  public int readScore(AppStateManager stateManager) {
      
    String filePath;  
      
    try {  
      filePath         = stateManager.getState(AndroidManager.class).filePath;
      }
    
    catch (NullPointerException e){
      filePath = JmeSystem.getStorageFolder().toString();
      }
    
     AssetManager assetManager = stateManager.getApplication().getAssetManager();
     
     assetManager.registerLocator(filePath, FileLocator.class);
     
     Node newNode;
     int  score;
     
     try {
       newNode = (Node) assetManager.loadModel("score.j3o");
       score   = newNode.getUserData("Score");
       }
     
     catch (AssetNotFoundException ex) {
       saveScore(0, stateManager);
       score = 0;    
       }
     
     catch (IllegalArgumentException e) {
       saveScore(0, stateManager);
       score = 0;
       }
     
     
     System.out.println("You've loaded: " + score);
     return score;
     }
  
  @Override
  public void update(float tpf) {
    
    for (int i = 0; i < cubeNode.getQuantity(); i++) {
      
      CollisionResults results = new CollisionResults();  
      Cube currentCube         = (Cube) cubeNode.getChild(i);
      
      currentCube.collideWith(player.getWorldBound(), results);
      
      if (results.size() > 0){
        
        if (playerSize > currentCube.size) {
          
          currentCube.removeFromParent();
          cubesEaten++;
          
          if (playerSize < .9f) {
            playerSize = playerSize + .01f;
            scalePlayer();
            }
          
          }
        
        else {
          lose();
          }
          
        }
      
      }  
      
    }
  
  }
