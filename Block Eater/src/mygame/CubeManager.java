/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.Random;

/**
 *
 * @author Bob
 */
public class CubeManager extends AbstractAppState {
    
  private SimpleApplication   app;
  private Material            green;
  private Material            blue;
  private Material            red;
  private Material            yellow;
  private Random              rand;
  public  Node                cubeNode;
  private Box                 b;
  private InteractionManager  inter;
  private boolean             up,down,left,right;
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app = (SimpleApplication) app;
    inter    = app.getStateManager().getState(InteractionManager.class);
    init();
    setEnabled(false);
    }
  
  private void init(){
    b        = new Box(1,1,1);
    cubeNode = new Node();
    green    = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    blue     = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    red      = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    yellow   = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    rand     = new Random();
    app.getRootNode().attachChild(cubeNode);
    green.setColor("Color", ColorRGBA.Green);
    blue.setColor("Color", ColorRGBA.Blue);
    red.setColor("Color", ColorRGBA.Red);
    yellow.setColor("Color", ColorRGBA.Yellow);
    }
  
  private void createCube() {
    Cube cube    = new Cube();
    Geometry box = new Geometry("Model", b);
    cube.attachChild(box);
    cubeNode.attachChild(cube);
    randomizeCube(cube);
    }
  
  private void randomizeCube(Cube cube) {
      
    float x     = randInt(0, 50)-25;
    float y     = randInt(0, 50)-25;  
    
    x = x/2;
    y = y/2;
    
    if (x>y) {
        
      if (x>0) {
        x = 12.5f;
        cube.moveDir = new Vector3f(-1,0,0);
        }
      
      else {
        x = -12.5f;
        cube.moveDir = new Vector3f(1,0,0);
        }
      
      }
    
    else {
        
      if (y>0) {
        y = 12.5f;
        cube.moveDir = new Vector3f(0,0,-1);
        }
      
      else {
        y = -12.5f;
        cube.moveDir = new Vector3f(0,0,1);
        }
      
      }
    
    cube.speed   = randInt(3,7);
    cube.moveDir = cube.moveDir.mult(cube.speed);
    cube.size    = (float) randInt(1,15)/10;
    cube.setLocalTranslation(x, 0, y);
    changeColor(cube);
    changeSize(cube);
    }
  
  private void changeColor(Cube cube) {
      
    int colorChance = randInt(1,4);
    if (colorChance == 1)
    cube.setMaterial(green);        
    if (colorChance == 2)
    cube.setMaterial(blue);        
    if (colorChance == 3)
    cube.setMaterial(red);    
    if (colorChance == 4)
    cube.setMaterial(yellow);
    
    }
  
  private void changeSize(Cube cube){
    cube.setLocalScale(cube.size);
    }
  
  private int randInt(int min, int max) {
    int    randomNum = rand.nextInt((max - min) + 1) + min;
    return randomNum;
    }
  
  @Override
  public void update(float tpf) {
    
    //Creates a cube if there is less than 10 cubes  
    if (cubeNode.getQuantity() < 10) {
      createCube();
      }
    
    //Checks Each Cube
    for (int i = 0; i < cubeNode.getQuantity(); i++) {
      
      //Checks the Interaction Manager for Input
      up    = inter.up;
      down  = inter.down;
      left  = inter.left;
      right = inter.right;
      
      //Sets players movement to 0
      float xMove = 0;
      float yMove = 0;
       
      //If there is any input, set the move accordingly 
      if (down) {
        yMove = 4;
        }
      
      else if (up) {
        yMove = -4;  
        }
      
      if (left) {
        xMove = -4;  
        }
      
      else if (right) {
        xMove = 4;  
        }
      
      //Gets the Current Cube
      Cube cube = (Cube) cubeNode.getChild(i);
      
      //Actually is doing the moving of the cube
      cube.move((cube.moveDir.add(xMove,0,yMove)).mult(tpf));
      
      //Remove the Cube if it is too far away
      if (cube.getLocalTranslation().x > 13 ^ cube.getLocalTranslation().x < -13)
      cube.removeFromParent();
      
      //Remove the Cube if it is too far away
      if (cube.getLocalTranslation().z > 13 ^ cube.getLocalTranslation().z < -13)
      cube.removeFromParent();
      
      //Checks each cube for collision with Current Cube
      for (int j = 0; j < cubeNode.getQuantity(); j++) {
       
        //Gets the Current Cube
        Cube currentCube         = (Cube) cubeNode.getChild(j);
        CollisionResults results = new CollisionResults();
        
        //Checks to be sure it is not checking itself, then checks collision with current cube.
        if (cube != currentCube) 
        cube.collideWith(currentCube.getChild("Model").getWorldBound(), results);
        
        //Checks if a Cube is Hit
        if (results.size() > 0) {
          
          //If Current Cube is Bigger than the Hit Cube Make it Bigger and remove Hit Cube 
          if (currentCube.size > cube.size) {
            cube.removeFromParent();
            currentCube.size = currentCube.size + .1f;
            changeColor(currentCube);
            changeSize(currentCube);
            }
          
          //If Current Cube is Smaller than the Hit Cube Remove It and make Hit Cube Bigger
          else {
            currentCube.removeFromParent();
            cube.size = cube.size + .1f;
            changeColor(cube);
            changeSize(cube);
            }
            
          }          
          
        }
      
      } 
    
    }
    
  }
