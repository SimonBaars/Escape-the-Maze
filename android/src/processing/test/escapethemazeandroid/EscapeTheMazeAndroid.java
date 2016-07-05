package processing.test.escapethemazeandroid;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import apwidgets.*; 
import ai.pathfinder.*; 
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 
import java.util.concurrent.Future; 
import java.util.concurrent.TimeUnit; 
import java.util.concurrent.TimeoutException; 
import java.util.concurrent.ExecutionException; 
import java.lang.InterruptedException; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class EscapeTheMazeAndroid extends PApplet {












int dayLength=30*200;//30*10 is default
int amountOfBots=5;
final String path = "//sdcard//Games//Escape the Maze//";
final boolean androidMode=true;
int numOfColumns = 50;
int tileSize;//normaal = 180
int gladeSize= 4;
int camx=-300, camy=-300; //int camx=-numOfColumns*tileSize/2, camy=-numOfColumns*tileSize/2;
byte[] floatSpeed = {
  PApplet.parseByte(random(4)+1), PApplet.parseByte(random(3)+1)
  };
  byte[] gameSetup = new byte[6];
int borderSize=3;
boolean night;
int targetNode=0;
int daytime;
int vision;//normaal 4
int scherm =10;
int day = 1;
boolean canMove=true;
boolean notDrawn;
byte[] wallTypes;
int[] playerNodes = new int [2];
int chestSpawnRate=80;
float grieverDamage=1;
int maxFood=dayLength*2;
int buttonSize;
int[] buttonSpots;
boolean canPress=true;
boolean sprintingMode=false;
String playerName = "Unknown Name";
int isLoading=0;

ArrayList<Node> gladeNodes = new ArrayList<Node>();
ArrayList<Tree> trees = new ArrayList<Tree>();
ArrayList<Wood> woodBlock = new ArrayList<Wood>();
ArrayList<Pig> pigs = new ArrayList<Pig>();
ArrayList<Trap> traps = new ArrayList<Trap>();

Poort[][] poorten = new Poort[numOfColumns][numOfColumns];
Node[][] nodeRegister;
lijn[] lijnen = new lijn[0];
Node[] bots = new Node[amountOfBots];
Player player;
Farmland farmland;
Pathfinder bfs;
BotSpawner botSpawner = new BotSpawner();
Audio audioPlayer;
Shop craftingGui = new Shop();
Mine mine;
SaveLoad saveManager = new SaveLoad();
Tutorial tutorial = null;
LoadThread loadThread = new LoadThread();

PImage[][] pigImage = new PImage[4][4];
PImage[][] playerImages = new PImage[4][4];
PImage[] treeImages = new PImage[3];
PImage[] fireImages = new PImage[5];
PImage[] wallImages = new PImage[3];
PImage[] craftingTable = new PImage[4];
PImage[] buttonImage = new PImage[13];
PImage[] textImage = new PImage[3];
PImage[] pressButtons = new PImage[11];
PImage grassImage;
PImage chestImage;
PImage mapBg;
PImage trapImage;
PImage mineImage;

APMediaPlayer grass;
APMediaPlayer nightMusic;
APMediaPlayer maze;
APMediaPlayer intro;
APMediaPlayer click;
APMediaPlayer fire;

public void setup() {
  frameRate(30);
  grass= new APMediaPlayer(this);
  nightMusic= new APMediaPlayer(this);
  maze= new APMediaPlayer(this);
  intro= new APMediaPlayer(this);
  click= new APMediaPlayer(this);
  fire= new APMediaPlayer(this);
  audioPlayer = new Audio();
}

public void cleanupGame() {
  playerNodes = new int [2];
  if (tutorial!=null) {
    tutorial.shown= fillWithNumber(tutorial.shown, false);
  }
  gladeNodes.clear();
  trees.clear();
  woodBlock.clear();
  pigs.clear();
  traps.clear();

  poorten = new Poort[numOfColumns][numOfColumns];
  lijnen = new lijn[0];
}

public void setupGame() {
  day=1;
  if (tutorial!=null) {
    tutorial.currentStage=0;
  }
  canMove=true;
  camx=-numOfColumns*tileSize/2; 
  camy=-numOfColumns*tileSize/2;
  player = new Player(width/2, height/2, tileSize/6);
  Item testItem = new Item(player.amountOfItems, false);
  for (int i = 0; i<player.inventoryStrings.length; i++) {
    player.inventoryStrings[i]="0x "+testItem.getItemName(i);
  }
  player.inventory.add(new Item(12, true));  
  initMap();
  wallTypes = new byte[bfs.nodes.size()];
  for (int i = 0; i<wallTypes.length; i++) {
    wallTypes[i] = PApplet.parseByte(random(wallImages.length));
  }
  initCollisions();
  for (int i = 0; i<amountOfBots; i++) {
    bots[i]=null;
  }
  botSpawner.calculatedSpots=fillWithNumber(botSpawner.calculatedSpots, 2);
  setNight(false);
  rectMode(CENTER);

  pigs.add(new Pig(PApplet.parseInt(gladeNodes.get(300).x), PApplet.parseInt(gladeNodes.get(290).y)));
  pigs.add(new Pig(PApplet.parseInt(gladeNodes.get(301).x), PApplet.parseInt(gladeNodes.get(291).y)));
  mine = new Mine();
  farmland=new Farmland();
  textSize(width/30);
}

public void initImages() {
  for (int i = 0; i<wallImages.length; i++) {
    wallImages[i]=loadImage("wall"+i+".png");
    wallImages[i].resize(tileSize/3, tileSize/3);
    if (androidMode) {
      wallImages[i].loadPixels();
    }
  }
  for (int i = 0; i<botSpawner.botImage.length; i++) {
    botSpawner.botImage[i]=loadImage("bug"+(i+1)+".png");
    botSpawner.botImage[i].resize(tileSize/3, tileSize/3);
    if (androidMode) {
      botSpawner.botImage[i].loadPixels();
    }
  }
  for (int i = 0; i<treeImages.length; i++) {
    treeImages[i] = loadImage("tree"+i+".png");
    treeImages[i].resize(tileSize/6, tileSize/6);
    if (androidMode) {
      treeImages[i].loadPixels();
    }
  }
  for (int i = 0; i<fireImages.length; i++) {
    fireImages[i] = loadImage("fire"+(i+1)+".png");
    fireImages[i].resize(tileSize/6, tileSize/6);
    if (androidMode) {
      fireImages[i].loadPixels();
    }
  }
  for (int i = 0; i<buttonImage.length; i++) {
    buttonImage[i] = loadImage("button"+(i+1)+".png");
    buttonImage[i].resize(height/2, height/8);
    if (androidMode) {
      buttonImage[i].loadPixels();
    }
  }
  for (int i = 0; i<pressButtons.length; i++) {
    pressButtons[i] = loadImage("pressbutton"+(i+1)+".png");
    pressButtons[i].resize(buttonSize, buttonSize);
    if (androidMode) {
      pressButtons[i].loadPixels();
    }
  }
  for (int i = 0; i<textImage.length; i++) {
    textImage[i] = loadImage("text"+(i+1)+".png");
    textImage[i].resize(width, height/8);
    if (androidMode) {
      textImage[i].loadPixels();
    }
  }
  for (int i = 0; i<pigImage.length; i++) {
    for (int j = 0; j<pigImage[0].length; j++) {
      pigImage[i][j] = loadImage("pig"+(i+1)+(j+1)+".png");
      if (i==0 || i==1) {
        pigImage[i][j].resize(PApplet.parseInt(tileSize/3), tileSize/6);
      } else {
        pigImage[i][j].resize(tileSize/6, PApplet.parseInt(tileSize/3));
      }
      if (androidMode) {
        pigImage[i][j].loadPixels();
      }
    }
  }
  for (int i = 0; i<playerImages.length; i++) {
    for (int j = 0; j<playerImages[0].length; j++) {
      playerImages[i][j] = loadImage("player"+(i+1)+(j+1)+".png");
      playerImages[i][j].resize(tileSize/6, tileSize/6);
      if (androidMode) {
        playerImages[i][j].loadPixels();
      }
    }
  }
  grassImage=loadImage("grass.png");
  grassImage.resize(tileSize/3, tileSize/3);
  if (androidMode) {
    grassImage.loadPixels();
  }
  chestImage=loadImage("crate.png");
  chestImage.resize(tileSize/6, tileSize/6);
  if (androidMode) {
    chestImage.loadPixels();
  }
  mapBg=loadImage("map.png");
  mapBg.resize(width, height);
  if (androidMode) {
    mapBg.loadPixels();
  }
  trapImage=loadImage("spike.png");
  trapImage.resize(tileSize/6, tileSize/6);
  if (androidMode) {
    trapImage.loadPixels();
  }
  craftingTable[0]=loadImage("craftingtable.png");
  craftingTable[1]=loadImage("craftingtable.png");
  craftingTable[2]=loadImage("arrow.png");
  craftingTable[3]=loadImage("arrow2.png");
  craftingTable[0].resize(tileSize/6, tileSize/6);
  craftingTable[1].resize(height/2, height/2);
  craftingTable[2].resize(height/4, height/2);
  craftingTable[3].resize(height/4, height/2);
  if (androidMode) {
    craftingTable[0].loadPixels();
    craftingTable[1].loadPixels();
    craftingTable[2].loadPixels();
    craftingTable[3].loadPixels();
  }
  mineImage = loadImage("mine.png");
  mineImage.resize(tileSize, PApplet.parseInt(tileSize/1.5f));
  if (androidMode) {
    mineImage.loadPixels();
  }
}

public boolean isDivisible(int a, int b) {
  return a%b == 0;
}

public int[] fillWithNumber(int[] array, int getal) {
  for (int i = 0; i<array.length; i++) {
    array[i]=getal;
  }
  return array;
}

public byte[] fillWithNumber(byte[] array, byte getal) {
  for (int i = 0; i<array.length; i++) {
    array[i]=getal;
  }
  return array;
}

public boolean[] fillWithNumber(boolean[] array, boolean getal) {    
  for (int i = 0; i<array.length; i++) {    
    array[i]=getal;
  }    
  return array;
}  

public void setTransparentImage(int x, int y, PImage image) {
  if (androidMode) {
    set(x, y, image);
  } else {    
    image(image, x, y);
  }
}

public void initMap() { //part of setup
  bfs = new Pathfinder();
  bfs.offsetX = tileSize/2;
  bfs.offsetY = tileSize/2;
  bfs.setCuboidNodes(numOfColumns*3-3, numOfColumns*3-3, tileSize/3);
  nodeRegister = new Node[numOfColumns*3-3][numOfColumns*3-3];
  fillNodeRegister();
  for (int a=1; a<numOfColumns; a++) {
    lijnen = (lijn[]) append(lijnen, new lijn(a*tileSize, tileSize, tileSize, 0, true, false, true));
    lijnen = (lijn[]) append(lijnen, new lijn(a*tileSize, numOfColumns*tileSize, tileSize, 0, true, false, true));
    lijnen = (lijn[]) append(lijnen, new lijn(tileSize, a*tileSize, 0, tileSize, false, false, true));
    lijnen = (lijn[]) append(lijnen, new lijn(numOfColumns*tileSize, a*tileSize, 0, tileSize, false, false, true));
    for (int b=1; b<numOfColumns; b++) {
      if (isCloseTo(a, gladeSize, numOfColumns/2) && isCloseTo(b, gladeSize, numOfColumns/2)) {
        if (b==(numOfColumns/2)-gladeSize) {
          lijnen = (lijn[]) append(lijnen, new lijn(a*tileSize, b*tileSize, tileSize, 0, true, a==numOfColumns/2, false));
          lijnen = (lijn[]) append(lijnen, new lijn(a*tileSize, tileSize+(b+(2*gladeSize))*tileSize, tileSize, 0, true, a==numOfColumns/2, false));
          lijnen = (lijn[]) append(lijnen, new lijn(b*tileSize, a*tileSize, 0, tileSize, false, a==numOfColumns/2, false));
          lijnen = (lijn[]) append(lijnen, new lijn(tileSize+(b+(2*gladeSize))*tileSize, a*tileSize, 0, tileSize, false, a==numOfColumns/2, false));
        }
      } else {
        //poorten = (Poort[]) append(poorten, new Poort(a*tileSize+(tileSize/2), b*tileSize+(tileSize/2)));
        poorten[a][b] = new Poort(a*tileSize+(tileSize/2), b*tileSize+(tileSize/2));
      }
    }
  }
  println("DONE!!");
  //bot = (Node)bfs.nodes.get(0);
  bfs.radialDisconnectUnwalkables();
}

public void fillNodeRegister() { //part of setup
  int k = 0;
  for (int i = 0; i<nodeRegister.length; i++) {
    for (int j = 0; j<nodeRegister[0].length; j++) {
      nodeRegister[i][j]=(Node)bfs.nodes.get(k);
      k++;
    }
  }
}

public void initCollisions() { //part of setup
  gladeNodes.clear();
  for (int i = 0; i < bfs.nodes.size (); i++) {
    ((Node)bfs.nodes.get(i)).walkable=true;
  }
  int nodeCounter=0;
  int lineCounter=0;
  for (int a=1; a<numOfColumns; a++) {
    for (int b=1; b<numOfColumns; b++) {
      Node[][] temp = getNodeMap(nodeCounter);
      if (isCloseTo(a, gladeSize, numOfColumns/2) && isCloseTo(b, gladeSize, numOfColumns/2)) {
        for (int i = 0; i<temp.length; i++) {
          for (int j = 0; j<temp[0].length; j++) {
            gladeNodes.add(temp[i][j]);
          }
        }
        if (b==(numOfColumns/2)-gladeSize) {
          for (int i = 0; i<3; i++) {
            temp[0][i].walkable=false;
          }
        }
        if (b==(numOfColumns/2)+gladeSize) {
          for (int i = 0; i<3; i++) {
            temp[2][i].walkable=false;
          }
        }
        if (a==(numOfColumns/2)-gladeSize) {
          for (int i = 0; i<3; i++) {
            temp[i][0].walkable=false;
          }
        }
        if (a==(numOfColumns/2)+gladeSize) {
          for (int i = 0; i<3; i++) {
            temp[i][2].walkable=false;
          }
        }
      } else {
        temp[1][1].walkable=false;
        checkContainingKleppen(temp, getPoort(lineCounter), a, b);
        lineCounter++;
      }
      nodeCounter+=(numOfColumns*3)*3-9;
    }
    nodeCounter=a*3;
  }
}

public Poort getPoort(int x) {
  int counter=0;
  for (int a=0; a<poorten.length; a++) {
    for (int b=0; b<poorten[0].length; b++) {
      if (poorten[a][b]!=null) {
        if (x==counter) {
          return poorten[a][b];
        }
        counter++;
      }
    }
  }
  return null;
}

public void checkContainingKleppen(Node[][] nodeMap, Poort poort, int a, int b) {
  for (int i = 0; i<poort.kleppen.length; i++) {

    if (getDegrees(poort.kleppen[i].an+poort.toBeAn)==270) {
      nodeMap[0][1].walkable=false;
    } else if (getDegrees(poort.kleppen[i].an+poort.toBeAn)==0) {
      nodeMap[1][2].walkable=false;
    } else if (getDegrees(poort.kleppen[i].an+poort.toBeAn)==90) {
      nodeMap[2][1].walkable=false;
    } else if (getDegrees(poort.kleppen[i].an+poort.toBeAn)==180) {
      nodeMap[1][0].walkable=false;
    } 
    //println("Klep gevonden op "+getDegrees(poort.kleppen[i].an+poort.an)+" graden in loop nr. "+a+", "+b);
  }
}

public int getDegrees(int degrees) {
  while (degrees>=360) {
    degrees-=360;
  }
  return degrees;
}

public Node[][] getNodeMap(int node) {
  Node[][] nodeMap = {
    {
      (Node)bfs.nodes.get(node), (Node)bfs.nodes.get(node+1), (Node)bfs.nodes.get(node+2)
      }
      , 
    {
      (Node)bfs.nodes.get((numOfColumns*3-3)+node), (Node)bfs.nodes.get((numOfColumns*3-3)+node+1), (Node)bfs.nodes.get((numOfColumns*3-3)+node+2)
      }
      , 
    {
      (Node)bfs.nodes.get((2*(numOfColumns*3-3))+node), (Node)bfs.nodes.get((2*(numOfColumns*3-3))+node+1), (Node)bfs.nodes.get((2*(numOfColumns*3-3))+node+2)
      }
    };
    //println(node+", "+ (node+1)+", "+ (node+2)    +", "+          (numOfColumns*3+node)+", "+ (numOfColumns*3+node+1)+", "+ (numOfColumns*3+node+2)    +", "+          ((2*(numOfColumns*3))+node)+", "+ ((2*(numOfColumns*3))+node+1)+", "+ ((2*(numOfColumns*3))+node+2));
    return nodeMap;
}

public void drawNodes(int trackX, int trackY) {
  //stroke(10);
  //fill(255);
  //strokeWeight(1);
  for (int i = (trackY*3)-(vision*2); i < (trackY*3)+(vision*2); i++) {
    for (int j = (trackX*3)-(vision*3); j<(trackX*3)+(vision*3); j++) {
      if (i>=0 && j>=0 && i<nodeRegister.length && j<nodeRegister[0].length) {
        Node temp = nodeRegister[i][j];
        //println("check node "+i+", "+j);
        if (/*gladeNodes.contains(nodeRegister[i][j])*/isCloseTo((i+3)/3, gladeSize, numOfColumns/2) && isCloseTo((j+3)/3, gladeSize, numOfColumns/2)) {
          //if(temp.walkable){
          set(PApplet.parseInt(temp.x+camx+(1.5f*(tileSize/3))), PApplet.parseInt(temp.y+camy+(1.5f*(tileSize/3))), grassImage);
          //}
        } else {
          set(PApplet.parseInt(temp.x+camx+(1.5f*(tileSize/3))), PApplet.parseInt(temp.y+camy+(1.5f*(tileSize/3))), wallImages[wallTypes[(i*nodeRegister.length)+j]]);
        }
      }
    }
  }
}

public void setNight(boolean newValue) {
  daytime=dayLength;
  night=newValue;
  for (int i = 0; i<lijnen.length; i++) {
    if (lijnen[i].isDoor) {
      openOrCloseDoor(newValue, lijnen[i]);
    }
  }
}

public boolean isCloseTo(int number, int howCloseTo, int anotherNumber) {
  if (anotherNumber<=number+howCloseTo && anotherNumber>=number-howCloseTo) {
    return true;
  }
  return false;
}

public void draw() {
  if (isLoading!=0) {
    while (loadThread.isAlive ()) {
    }
    isLoading=0;
    loadThread = new LoadThread();
  }
  if (scherm==1) {
    background(122, 122, 122);
    audioPlayer.update();
    playerNodes[0] = PApplet.parseInt((((player.x-camx)-(PApplet.parseFloat(tileSize)/2))/PApplet.parseFloat(tileSize))/*-(float(tileSize)/2)*/);
    playerNodes[1] = PApplet.parseInt((((player.y-camy)-(PApplet.parseFloat(tileSize)/2))/PApplet.parseFloat(tileSize))/*-(float(tileSize)/2)*/);
    player.checkOutOfScreen();
    drawNodes(playerNodes[0], playerNodes[1]);
    strokeWeight(tileSize/6);
    stroke(player.wallColor); 
    for (int i = 0; i<traps.size (); i++) {
      traps.get(i).teken();
    }
    for (int a=0; a<poorten.length; a++) {
      for (int b=0; b<poorten[0].length; b++) {
        if (poorten[a][b]!=null) {
          if ( isCloseTo(a, vision, playerNodes[0]) && isCloseTo(b, vision, playerNodes[1])) {
            poorten[a][b].teken(/*millis()*//*0*/);
          }
          poorten[a][b].checkUpdate();
        }
      }
    }
    for (int a=0; a<lijnen.length; a++) {
      lijnen[a].checkUpdate();
      lijnen[a].teken();
    }
    farmland.createFarmland();
    for (int i = 0; i<pigs.size (); i++) {
      pigs.get(i).teken();
      pigs.get(i).wander();
    }
    player.teken();
    player.update();
    for (int i = 0; i<trees.size (); i++) {
      trees.get(i).teken();
      trees.get(i).update();
    }
    for (int i = 0; i<woodBlock.size (); i++) {
      woodBlock.get(i).teken();
    }

    if (player.stuckInWall()) {
      player.health--;
    }
    if (player.currentFood<=0) {
      player.health-=0.1f;
    }
    if (player.health<=0) {
      scherm=2;
    }
    //println((float(player.currentFood)/float(player.maxFood)*100));
    if (PApplet.parseFloat(player.currentFood)/PApplet.parseFloat(maxFood)*100>90 && player.health<=99.95f) {
      player.health+=0.05f;
    }
    mine.teken();
    botSpawner.drawBot();
    botSpawner.checkCloseToPlayer();
    botSpawner.checkCloseToTrap();
    //println("8. "+millis());
    fill(0);
    textSize(height/30);
    if (player.inventory.size()>0) {
      player.outlinedText("Currenly selected item: "+player.inventoryStrings[player.selectedItem], width/30, (height/15)-20);
    } else {
      player.outlinedText("Your inventory is empty!", width/30, (height/15)-20);
    }
    if (night) {
      drawCounter(0, "Time till day", daytime, dayLength, 0xff00368B);
    } else {
      drawCounter(0, "Time till night", daytime, dayLength, 0xffFCF503);
    }
    drawCounter(1, "Food", player.currentFood, maxFood, 0xff54FF00);
    drawCounter(2, "Health", player.health, 100, 0xff54FF00);
    fill(0, 0, 0);
    text("Day "+day, width/2-(width/15), height/30);
    daytime--;
    if (player.currentFood>0) {
      player.currentFood--;
    }
    farmland.growFarm();
    if (daytime<=0) {
      setNight(!night);
      if (night) {
        moveAllWalls();
      } else {
        botSpawner.botsBackToSpawn();
        day++;
      }
    }
    canMove=true;
    if (androidMode) {
      for (int i = 0; i<buttonSpots.length; i+=2) {
        if (!(i==18 && !player.canRun)) {
          setTransparentImage(buttonSpots[i], buttonSpots[i+1], pressButtons[i/2+(booleanToInt(i==18 && sprintingMode))]);
        }
      }
    }

    if (tutorial!=null) {
      tutorial.update();
    }
    //text(int(frameRate), 40, 40);
    if (keyPressed) {
      if (key=='a') {
        player.moveTo(-player.movingSpeed, 0);
      } else if (key=='s') {
        player.moveTo(0, player.movingSpeed);
      } else if (key=='d') {
        player.moveTo(player.movingSpeed, 0);
      } else if (key=='w') {
        player.moveTo(0, -player.movingSpeed);
      } else if (player.canRun) {
        if (key=='A') {
          player.moveTo(-player.runningSpeed, 0);
        } else if (key=='S') {
          player.moveTo(0, player.runningSpeed);
        } else if (key=='D') {
          player.moveTo(player.runningSpeed, 0);
        } else if (key=='W') {
          player.moveTo(0, -player.runningSpeed);
        }
      }
    }
    if (androidMode) {
      mousePressedAndroid();
    }
  } else if (scherm==2) {
    textSize(width/10);
    textAlign(CENTER);
    text("Game Over", width/2, height/2);
  } else if (scherm==3) {
    if (notDrawn) {
      String[] winners = new String[0];
      if (saveManager.fileExists(path+"Winners.txt")) {
        winners = loadStrings(path+"Winners.txt");
      }
      winners = append(winners, playerName+" has finished the game in "+day+" day(s) at "+day()+"-"+month()+"-"+year()+" with difficulty "+(gameSetup[0]+gameSetup[1]+gameSetup[2]+gameSetup[3]+gameSetup[4]+gameSetup[5])+" (higher number means harder game)!");
      saveStrings(path+"Winners.txt", winners);
      notDrawn = false;
    }
    textSize(width/10);
    textAlign(CENTER);
    text("You won the game!", width/2, height/2);
  } else if (scherm==4) {
    if (notDrawn) {
      set(0, 0, mapBg);
      float tempTileSize= height/(1.0714f*numOfColumns);
      strokeWeight(PApplet.parseInt(height/400));
      for (int a=0; a<poorten.length; a++) {
        for (int b=0; b<poorten[0].length; b++) {
          if (poorten[a][b]!=null) {
            for (int i = 0; i<poorten[a][b].kleppen.length; i++) {
              float x = (width/6)+a*tempTileSize;
              float y = (height/50)+b*tempTileSize;
              float nx = x+cos(radians(PApplet.parseInt(poorten[a][b].kleppen[i].an+poorten[a][b].an)))*(tempTileSize/2);
              float ny = y+sin(radians(PApplet.parseInt(poorten[a][b].kleppen[i].an+poorten[a][b].an)))*(tempTileSize/2);
              line(x, y, nx, ny);
            }
          }
        }
      }
      for (int i = 0; i<lijnen.length; i++) {
        if (!lijnen[i].isDoor) {
          float x = (width/6)+((lijnen[i].x/tileSize)*tempTileSize)-(tempTileSize/2);
          float y = (height/50)+((lijnen[i].y/tileSize)*tempTileSize)-(tempTileSize/2);
          line(x, y, x+(booleanToInt(lijnen[i].type)*tempTileSize), y+(booleanToInt(!lijnen[i].type)*tempTileSize));
        }
      }
      if (isCloseTo((player.x-camx)/tileSize, gladeSize, numOfColumns/2) && isCloseTo((player.y-camy)/tileSize, gladeSize, numOfColumns/2)) {
        ellipseMode(CENTER);
        noStroke();
        fill(0, 255, 0);
        for (int i = 0; i<bots.length; i++) {
          if (bots[i]!=null) {
            float x = (width/6)+(((bots[i].x+(tileSize/3))/PApplet.parseFloat(tileSize))*tempTileSize)-(tempTileSize/2);
            float y = (height/50)+(((bots[i].y+(tileSize/3))/PApplet.parseFloat(tileSize))*tempTileSize)-(tempTileSize/2);
            ellipse(x, y, height/150, height/150);
          }
        }
        fill(0, 0, 255);
        float x = (width/6)+((PApplet.parseFloat(player.x-camx)/PApplet.parseFloat(tileSize))*tempTileSize)-(tempTileSize/2);
        float y = (height/50)+((PApplet.parseFloat(player.y-camy)/PApplet.parseFloat(tileSize))*tempTileSize)-(tempTileSize/2);
        ellipse(x, y, height/150, height/150);
      }
      stroke(0);
      ellipseMode(CORNER);
      notDrawn=false;
      setTransparentImage(0, 0, buttonImage[2]);
    }
    if (mousePressed) {
      ellipseMode(CENTER);
      noStroke();
      fill(255, 0, 0);
      ellipse(mouseX, mouseY, height/250, height/250);
      stroke(0);
      ellipseMode(CORNER);
    }
  } else if (scherm==5) {
    if (notDrawn) {
      fill(0, 0, 0, 100);
      rectMode(CORNER);
      noStroke();
      textSize(width/30);
      rect(0, 0, width, height);
      craftingGui.drawOnce();
    }
    textAlign(CENTER);
    craftingGui.teken();
    notDrawn=false;
    stroke(0);
    rectMode(CENTER);
    textAlign(CORNER);
  } else if (scherm==6) {
    backgroundAnimation();
    setTransparentImage(0, 0, textImage[0]);
    for (int i = 0; i<4; i++) {
      int position = i+3;
      if (i==3) {
        position=5;
      } else if (i==2) {
        position=12;
      }
      setTransparentImage((width/2)-(height/4), (i+1)*(height/5)-(height/16), buttonImage[position]);
    }
  } else if (scherm==7) {
    String[] options = {
      "Map Size", "Bug Speed", "Hunger bar size", "Daylength", "Number of crates", "Bug Damage"
    };
    backgroundAnimation();
    setTransparentImage(0, 0, textImage[1]);
    for (int i = 0; i<6; i++) {
      int x = ((booleanToInt(i<3)*2+1)*(width/4))-(height/4);
      int y = (i+1-(3*booleanToInt(i>2)))*(height/4)-(height/16);
      player.outlinedText(options[i], x, y-(height/40));
      setTransparentImage(x, y, buttonImage[gameSetup[i]+6]);
    }
    setTransparentImage((width/2)-(height/4), PApplet.parseInt(height/1.2f), buttonImage[0]);
  } else if (scherm==8) {
    if (notDrawn) {
      fill(0, 0, 0, 100);
      rectMode(CORNER);
      noStroke();
      rect(0, 0, width, height);
      stroke(0);
      rectMode(CENTER);
      setTransparentImage(0, 0, textImage[2]);
      setTransparentImage((width/2)-(height/4), 1*(height/4)-(height/16), buttonImage[2]);
      if (tutorial==null) {
        setTransparentImage((width/2)-(height/4), 2*(height/4)-(height/16), buttonImage[10]);
      }
      setTransparentImage((width/2)-(height/4), 3*(height/4)-(height/16), buttonImage[11]);
      notDrawn=false;
    }
  } else if (scherm==9) {
    textSize(width/20);
    outlinedText("I'm sorry, but due to memory problems I haven't been able to load! Please restart the game and try again (a lot of devices are able to run extreme map sizes after restarting). If you just restarted I'm sorry to you, but you should choose a smaller map size. Press anywhere on the screen to return to the main menu...", width/2, height/2, width, height);
  } else if (scherm==10) {
    isLoading=1;
    showLoadingScreen();
  }
}

public void showLoadingScreen() {
  textSize(width/20);
  fill(0, 0, 0, 100);
  rectMode(CORNER);
  noStroke();
  rect(0, 0, width, height);
  stroke(0);
  rectMode(CENTER);
  textAlign(CENTER);
  fill(255);
  text("Loading... Please wait a sec!", width/2, height/2);
  text("(harder map sizes will take longer to load)", width/2, height/1.5f);
  textAlign(CORNER);
  loadThread.start();
}

public void outlinedText(String text, int x, int y, int sizex, int sizey) {
  fill(0, 0, 0);
  text(text, x-1, y, sizex, sizey); 
  text(text, x, y-1, sizex, sizey); 
  text(text, x+1, y, sizex, sizey); 
  text(text, x, y+1, sizex, sizey); 
  fill(255, 255, 255);
  text(text, x, y, sizex, sizey);
}

public void backgroundAnimation() {
  drawNodes(PApplet.parseInt(-(PApplet.parseFloat(camx)/PApplet.parseFloat(tileSize))), PApplet.parseInt(-(PApplet.parseFloat(camy-(height/2))/PApplet.parseFloat(tileSize))));
  strokeWeight(tileSize/6);
  stroke(player.wallColor); 
  for (int i = 0; i<traps.size (); i++) {
    traps.get(i).teken();
  }
  for (int a=0; a<poorten.length; a++) {
    for (int b=0; b<poorten[0].length; b++) {
      //println(int((((player.x-camx)-(float(tileSize)/2))/float(tileSize))/*-(float(tileSize)/2)*/));
      if (poorten[a][b]!=null) {
        if ( isCloseTo(a, vision, PApplet.parseInt(-(PApplet.parseFloat(camx)/PApplet.parseFloat(tileSize)))) && isCloseTo(b, vision, PApplet.parseInt(-(PApplet.parseFloat(camy-(height/2))/PApplet.parseFloat(tileSize))))) {
          poorten[a][b].teken(/*millis()*//*0*/);
        }
        poorten[a][b].checkUpdate();
      }
    }
  }
  for (int a=0; a<lijnen.length; a++) {
    lijnen[a].checkUpdate();
    lijnen[a].teken();
  }
  if (PApplet.parseInt(random(1000))==1) {
    moveAllWalls();
  }
  if (numOfColumns<PApplet.parseInt(-(PApplet.parseFloat(camx)/PApplet.parseFloat(tileSize)))+10 || numOfColumns<PApplet.parseInt(-(PApplet.parseFloat(camy)/PApplet.parseFloat(tileSize)))+10) {
    camx=-300; 
    camy=-300;
    floatSpeed[0]=PApplet.parseByte(random(4)+1);
    floatSpeed[1]=PApplet.parseByte(random(3)+1);
  }
  camx-=floatSpeed[0];
  camy-=floatSpeed[1];
}

public void mousePressed() {
  if (scherm==-1) {
    scherm=1;
  }
  if (scherm==5) {
    if (checkMousePosition((width/2)+(height/4), height/2, height/4, height/2)) {
      if (craftingGui.selectedSale<(craftingGui.shop.length/4)+(craftingGui.shopMulti.length/6)-1) {
        craftingGui.selectedSale++;
        audioPlayer.playSound(click);
      }
    } else if (checkMousePosition((width/2)-(2*(height/4)), height/2, height/4, height/2)) {
      if (craftingGui.selectedSale>0) {
        craftingGui.selectedSale--;
        audioPlayer.playSound(click);
      }
    } else if (checkMousePosition((width/2)-(height/4), height/4, height/2, height/8)) {
      if (craftingGui.selectedSale<craftingGui.shop.length/4) {
        if (craftingGui.purchase(true)) {
          scherm=1;
          audioPlayer.playSound(click);
        }
      } else {
        if (craftingGui.purchaseMulti(true)) {
          scherm=1;
          audioPlayer.playSound(click);
        }
      }
    }
  } else if (scherm==6) {
    for (int i = 0; i<4; i++) {
      if (checkMousePosition((width/2)-(height/4), (i+1)*(height/5)-(height/16), height/2, height/8)) {
        if (i==0) {
          audioPlayer.playSound(click);
          scherm=7;
        } else if (i==1) {
          audioPlayer.playSound(click);
          if (saveManager.fileExists(path+"poorten.txt")) {
            isLoading=3;
            showLoadingScreen();
          }
        } else if (i==3) {
          audioPlayer.stopAllMusic();
          System.exit(0);
        } else if (i==2) {           
          tutorial = new Tutorial();
          gameSetup[0]=0;
          gameSetup[1]=1;
          gameSetup[2]=1;    
          gameSetup[3]=1;    
          gameSetup[4]=0;    
          gameSetup[5]=1; 
          isLoading = 2;
          showLoadingScreen();
        }
      }
    }
  } else if (scherm==7) {
    for (int i = 0; i<6; i++) {
      int x = ((booleanToInt(i<3)*2+1)*(width/4))-(height/4);
      int y = (i+1-(3*booleanToInt(i>2)))*(height/4)-(height/16);
      if (checkMousePosition(x, y, height/2, height/8)) {
        audioPlayer.playSound(click);
        gameSetup[i]++;
        if (gameSetup[i]==4) {
          gameSetup[i]=0;
        }
      }
    }
    if (checkMousePosition((width/2)-(height/4), PApplet.parseInt(height/1.2f), height/2, height/8)) {
      audioPlayer.playSound(click);
      isLoading = 2;
      showLoadingScreen();
    }
  } else if (scherm==8) {
    for (int i = 0; i<3; i++) {
      if (checkMousePosition((width/2)-(height/4), (i+1)*(height/4)-(height/16), height/2, height/8)) {
        if (i==0) {
          scherm=1;
        } else if (i==1 && tutorial==null) {
          isLoading=4;
          showLoadingScreen();
          audioPlayer.stopAllMusic();
          audioPlayer.playMusic(intro);
          tutorial=null;
        } else if (i==2) {
          scherm=6;
          audioPlayer.stopAllMusic();
          audioPlayer.playMusic(intro);
          tutorial=null;
        }
      }
    }
  } else if (scherm==2 || scherm==3 || scherm==9) {
    poorten=null;
    showLoadingScreen();
    isLoading=5;
  }
  if (scherm==4 || scherm==5) {
    if (checkMousePosition(0, 0, height/2, height/8)) {
      audioPlayer.playSound(click);
      scherm=1;
    }
  }
}

public void mousePressedAndroid() {
  if (mousePressed) {
    if (canPress) {
      for (int buttonCounter = 0; buttonCounter<buttonSpots.length; buttonCounter+=2) {
        int j = buttonCounter/2;
        if (checkMousePosition(buttonSpots[buttonCounter], buttonSpots[buttonCounter+1], buttonSize, buttonSize)) {
          if (j==1) {
            player.moveTo(-player.movingSpeed*(booleanToInt(sprintingMode)+1), 0);
          } else if (j==3) {
            player.moveTo(0, player.movingSpeed*(booleanToInt(sprintingMode)+1));
          } else if (j==0) {
            player.moveTo(player.movingSpeed*(booleanToInt(sprintingMode)+1), 0);
          } else if (j==2) {
            player.moveTo(0, -player.movingSpeed*(booleanToInt(sprintingMode)+1));
          } else if (j>3) {
            canPress=false;
            if (j==7) {
              if (player.inventory.size()>0) {
                player.getSelectedItem().use();
              }
            } else if (j==8) {
whileloop:
              while (true) {
                if (farmland.gatherWheatAtClosestFarmland()) {
                  break whileloop;
                }
                if (poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize]!=null) {
                  for (int i = 0; i<poorten[ (player.x-camx)/tileSize][(player.y-camy)/tileSize].kleppen.length; i++) {
                    if (poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize].kleppen[i].hasChest && player.closeTo(poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize].x+camx+(tileSize/3), poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize].y+camy+(tileSize/3), tileSize/3)) {
                      poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize].kleppen[i].hasChest=false;
                      player.inventory.add(new Item(getRandomLootFromChest(), true));
                      break whileloop;
                    }
                  }
                }
                for (int i = 0; i<traps.size (); i++) {
                  if (traps.get(i).harvest()) {
                    break whileloop;
                  }
                }
                for (int i = 0; i<woodBlock.size (); i++) {
                  if (woodBlock.get(i).isCraftingTable && woodBlock.get(i).veryCloseToPlayer()) {
                    scherm=5;
                    notDrawn=true;
                    break whileloop;
                  }
                }
                if (player.inventory.size()>0) {
                  if (player.getSelectedItem().id==12) {
                    for (int i = 0; i<woodBlock.size (); i++) {
                      if (woodBlock.get(i).turnIntoCraftingTable()) {
                        break whileloop;
                      }
                    }
                  }
                }
                break whileloop;
              }
            } 
            if (j==4) {
              key=0; 
              scherm=8;
              notDrawn=true;
            }  
            if (j==6) {
              player.selectedItem=getSelectedItemIncrease();
            } else if (j==5) {
              player.selectedItem=getSelectedItemDecrease();
            }
            if (j==9 && player.canRun) {
              sprintingMode=!sprintingMode;
              canPress=false;
            }
          }
        }
      }
    }
  } else {
    canPress=true;
  }
}

public void doGameSetup() {
  if (gameSetup[0]==0) {
    numOfColumns=50;
  } else if (gameSetup[0]==1) {
    numOfColumns=100;
  } else if (gameSetup[0]==2) {
    numOfColumns=150;
  } else if (gameSetup[0]==3) {
    numOfColumns=200;
  } 
  if (gameSetup[1]==0) {
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      botSpawner.movementSteps[i]=9+PApplet.parseInt(random(3));
    }
  } else if (gameSetup[1]==1) {
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      botSpawner.movementSteps[i]=6+PApplet.parseInt(random(3));
    }
  } else if (gameSetup[1]==2) {
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      botSpawner.movementSteps[i]=4+PApplet.parseInt(random(3));
    }
  } else if (gameSetup[1]==3) {
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      botSpawner.movementSteps[i]=2+PApplet.parseInt(random(3));
    }
  } 
  dayLength=30*200;
  if (gameSetup[2]==0) {
    maxFood=dayLength*5;
  } else if (gameSetup[2]==1) {
    maxFood=dayLength*2;
  } else if (gameSetup[2]==2) {
    maxFood=PApplet.parseInt(dayLength*1.5f);
  } else if (gameSetup[2]==3) {
    maxFood=dayLength;
  } 
  if (gameSetup[3]==0) {
    dayLength=30*300;
  } else if (gameSetup[3]==1) {
    dayLength=30*200;
  } else if (gameSetup[3]==2) {
    dayLength=30*100;
  } else if (gameSetup[3]==3) {
    dayLength=30*50;
  } 
  if (gameSetup[4]==0) {
    chestSpawnRate=30;
  } else if (gameSetup[4]==1) {
    chestSpawnRate=75;
  } else if (gameSetup[4]==2) {
    chestSpawnRate=100;
  } else if (gameSetup[4]==3) {
    chestSpawnRate=150;
  } 
  if (gameSetup[5]==0) {
    grieverDamage=0.25f;
  } else if (gameSetup[5]==1) {
    grieverDamage=1;
  } else if (gameSetup[5]==2) {
    grieverDamage=5;
  } else if (gameSetup[5]==3) {
    grieverDamage=100;
  }
}

public boolean checkMousePosition(int x, int y, int sizex, int sizey) {
  if (mouseX>x && mouseY>y && mouseX<x+sizex && mouseY<y+sizey) {
    return true;
  }
  return false;
}

public int booleanToInt(boolean x) {
  return (x) ? 1 : 0;
}

public void drawCounter(int num, String name, float value, float maxValue, int counterColor) {
  strokeWeight(1);
  stroke(0);
  fill(0, 0, 0);
  textSize(height/25);
  rect(width/1.2f, height/15+(num*(height/15)), width/4, height/40);
  text(name, width/1.4f, (height/15)-20+(num*(height/15)));
  fill(counterColor);
  rect((width/1.2f)+((value/maxValue)*(width/8))-(width/8), height/15+(num*(height/15)), ((width/4)/maxValue)*value, height/40);
}

public void keyPressed() {
  if (scherm==1) {
    if (canMove) {
      if (key=='e' || key=='E') {
        if (player.inventory.size()>0) {
          player.getSelectedItem().use();
        }
      } else if (key=='q' || key=='Q') {
whileloop:
        while (true) {
          if (farmland.gatherWheatAtClosestFarmland()) {
            break whileloop;
          }
          if (poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize]!=null) {
            for (int i = 0; i<poorten[ (player.x-camx)/tileSize][(player.y-camy)/tileSize].kleppen.length; i++) {
              if (poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize].kleppen[i].hasChest && player.closeTo(poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize].x+camx+(tileSize/3), poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize].y+camy+(tileSize/3), tileSize/3)) {
                poorten[(player.x-camx)/tileSize][(player.y-camy)/tileSize].kleppen[i].hasChest=false;
                player.inventory.add(new Item(getRandomLootFromChest(), true));
                break whileloop;
              }
            }
          }
          for (int i = 0; i<traps.size (); i++) {
            if (traps.get(i).harvest()) {
              break whileloop;
            }
          }
          for (int i = 0; i<woodBlock.size (); i++) {
            if (woodBlock.get(i).isCraftingTable && woodBlock.get(i).veryCloseToPlayer()) {
              scherm=5;
              notDrawn=true;
              break whileloop;
            }
          }
          if (player.inventory.size()>0) {
            if (player.getSelectedItem().id==12) {
              for (int i = 0; i<woodBlock.size (); i++) {
                if (woodBlock.get(i).turnIntoCraftingTable()) {
                  if (tutorial!=null) {
                    if (tutorial.shown[23]==false) {
                      tutorial.tutorialQueue.add(23);
                    }
                  }
                  break whileloop;
                }
              }
            }
          }
          break whileloop;
        }
      } 
      if (key == ESC) {
        key=0; 
        scherm=8;
        notDrawn=true;
      } 
      if (key==CODED) {
        if (keyCode==UP) {
          player.selectedItem=getSelectedItemIncrease();
        } else if (keyCode==DOWN) {
          player.selectedItem=getSelectedItemDecrease();
        }
      }
      canMove=false;
    }
  }
}

public int getRandomLootFromChest() {
  if (tutorial!=null) {
    if (tutorial.shown[8]==false) {
      tutorial.tutorialQueue.add(8);
    }
  }
  int random=PApplet.parseInt(random(100));
  if (random<25) {
    if (tutorial!=null) {
      if (tutorial.shown[9]==false) {
        tutorial.tutorialQueue.add(9);
      }
    }
    return 4;
  } else if (random<55) {
    if (tutorial!=null) {
      if (tutorial.shown[10]==false) {
        tutorial.tutorialQueue.add(10);
      }
    }
    return 7;
  } else if (random<75) {
    if (tutorial!=null) {
      if (tutorial.shown[15]==false) {
        tutorial.tutorialQueue.add(15);
      }
    }
    return 8;
  } else if (random<80) {
    return 12;
  } else if (random<85) {
    return 5;
  } else if (random<92) {
    if (tutorial!=null) {
      if (tutorial.shown[24]==false) {
        tutorial.tutorialQueue.add(24);
        tutorial.tutorialQueue.add(25);
      }
    }
    return 13;
  }
  if (tutorial!=null) {
    if (tutorial.shown[27]==false) {
      tutorial.tutorialQueue.add(27);
      tutorial.tutorialQueue.add(28);
      tutorial.tutorialQueue.add(29);
    }
  }
  return 14;
}

public void moveAllWalls() {
  for (int a=0; a<poorten.length; a++) {
    for (int b=0; b<poorten[0].length; b++) {
      if (poorten[a][b]!=null) {
        poorten[a][b].toBeAn=PApplet.parseInt(random(4))*90;
      }
    }
  }
  int randomLine=PApplet.parseInt(random(lijnen.length));
  while (!lijnen[randomLine].outsideWall) {
    randomLine=PApplet.parseInt(random(lijnen.length));
  }
  openOrCloseDoor(false, lijnen[randomLine]);
  botSpawner.spawnPoint[0]=lijnen[randomLine].x;
  botSpawner.spawnPoint[1]=lijnen[randomLine].y;
  for (int a=0; a<lijnen.length; a++) {
    if (lijnen[a].outsideWall && a!=randomLine) {
      openOrCloseDoor(true, lijnen[a]);
    }
  }
  initCollisions();
  botSpawner.spawnBots();
}

public int getNodeAtPosition(int x, int y) {
  int j=0;
  for (int i = 0; i<bfs.nodes.size (); i++) {
    if (isCloseTo(PApplet.parseInt(((Node)bfs.nodes.get(i)).x), tileSize/3, x-(tileSize-(tileSize/6))) && isCloseTo(PApplet.parseInt(((Node)bfs.nodes.get(i)).y), tileSize/3, y-(tileSize-(tileSize/6)))) {
      j=i;
    }
  }
  return j;
}

public Node getNodeAt(int y, int x) {
  //println(x+", "+y);
  int newx=PApplet.parseInt((PApplet.parseFloat(x-tileSize)/(PApplet.parseFloat(tileSize)))*3)+1;
  int newy=PApplet.parseInt((PApplet.parseFloat(y-tileSize)/(PApplet.parseFloat(tileSize)))*3)+1;
  return nodeRegister[newx][newy];
}

public int getLowestValue(int i, int j) {
  while (true) {
    if (i>j && j>0) {
      i=i-j;
    } else {
      break;
    }
  }
  return i;
}

public void openOrCloseDoor(boolean open, lijn _lijn) {
  if (_lijn.type) {
    _lijn.toBeSizeX=tileSize*((open) ? 1 : 0);
  } else {
    _lijn.toBeSizeY=tileSize*((open) ? 1 : 0);
  }
}

/*float plusofmin(float c) {
 if (random(10)<5) {
 return -c;
 } else {
 return c;
 }
 }*/

/*float[] getMoves(float an) {
 float an2 = an+ (plusofmin(90));
 float[] m = new float[12];
 float pas = 90/12.0;
 for (int a=0; a<12; a++) {
 m[a]= ((an+pas*(a+1)));
 } 
 return m;
 }*/

/*@Override
 public void onPause()
 {
 super.onPause();
 if(audioPlayer!=null){
 audioPlayer.stopAllMusic();
 }
 }*/

public int getClosestDivisibleBy(int number, int divisibleBy) {
  boolean bool = false;
  int higher=1;
  while (true) {
    if (isDivisible(number, divisibleBy)) {
      return number;
    }
    if (bool) {
      number+=higher;
    } else {
      number-=higher;
    }
    bool=!bool;
    higher++;
  }
}

public void removeCollisionBox(int collisionBox) {
  for (int i = 0; i<4; i++) {
    player.collisions.remove(collisionBox);
  }
  for (int i = 0; i<trees.size (); i++) {
    if (trees.get(i).collisionBox>collisionBox) {
      trees.get(i).collisionBox-=4;
    }
  }
  for (int i = 0; i<woodBlock.size (); i++) {
    if (woodBlock.get(i).collisionBox>collisionBox) {
      woodBlock.get(i).collisionBox-=4;
    }
  }
  for (int i = 0; i<traps.size (); i++) {
    if (traps.get(i).collisionBox>collisionBox) {
      traps.get(i).collisionBox-=4;
    }
  }
}

public boolean isCollision(int ax, int ay, int aWidth, int aHeight, int bx, int by, int bWidth, int bHeight) {
  if ((ax > bx + bWidth - 1) || // Is a on the right side of b?
  (ay > by + bHeight - 1) || // Is a under b?
  (bx > ax + aWidth - 1) || // Is b on the right side of a?
  (by > ay + aHeight - 1)) {   // Is b under a?
    return false; // No collision
  }
  return true; // Collision
}

public int getSelectedItemIncrease() {
  for (int i = player.selectedItem+1; i<player.inventoryStrings.length; i++) {
    if (player.inventoryStrings[i].charAt(0)!='0') {
      return i;
    }
  }
  return player.selectedItem;
}

public int getSelectedItemDecrease() {
  for (int i = player.selectedItem-1; i>=0; i--) {
    if (player.inventoryStrings[i].charAt(0)!='0') {
      return i;
    }
  }
  return player.selectedItem;
}

public class LoadThread extends Thread {

  public void run() {
    if (isLoading==1) {
      buttonSize= height/6;
      int[] buttonSpots2= {
        2*buttonSize, height-(2*buttonSize), 0, height-(2*buttonSize), 1*buttonSize, height-(3*buttonSize), 1*buttonSize, height-(1*buttonSize), (width/2)-(buttonSize/2), height-buttonSize, 1*buttonSize, height/16, 3*buttonSize, height/16, width-(3*buttonSize), height-(1*buttonSize), width-(1*buttonSize), height-(1*buttonSize), width-(2*buttonSize), height-(2*buttonSize)
        };
        buttonSpots=buttonSpots2;
      vision=5;
      tileSize=getClosestDivisibleBy((width+height)/7, 12);
      gameSetup=fillWithNumber(gameSetup, PApplet.parseByte(1));
      stroke(255, 0, 0); 
      initImages();
      setupGame();
      strokeCap(ROUND);
      scherm=6;
    } else if (isLoading==2) {
      doGameSetup();
      try {
        cleanupGame();
        setupGame();
      }
      catch(OutOfMemoryError e) {
        poorten = null;
        scherm=9;
      }
      audioPlayer.stopAllMusic();
      //audioPlayer.playGrassMusic();
      if (scherm!=9) {
        scherm=1;
      }
    } else if (isLoading==3) {
      try {
        saveManager.loadGame();
      }
      catch(OutOfMemoryError e) {
        poorten = null;
        scherm=9;
      }
      if (scherm!=9) {
        scherm=1;
      }
    } else if (isLoading==4) {
      saveManager.saveGame();
      scherm=6;
    } else if(isLoading==5){
      gameSetup[0]=0;
        doGameSetup();
        cleanupGame();
        setupGame();
        scherm=6;
    }
  }
}
class Audio {


  int music = 3;

  Audio() {
    grass.setMediaFile("grass.mp3");
    grass.setLooping(true);
    intro.setMediaFile("intro.mp3");
    intro.setLooping(true);
    click.setMediaFile("click.mp3");
    click.setLooping(false);
    maze.setMediaFile("maze.mp3");
    maze.setLooping(true);
    nightMusic.setMediaFile("night.mp3");
    nightMusic.setLooping(true);
    fire.setMediaFile("fire.mp3");
    fire.setLooping(false);
    playMusic(intro);
  }

  public void playMusic(APMediaPlayer music) {
    music.seekTo(0);
    music.start();
  }

  public void playSound(APMediaPlayer sound) {
    sound.seekTo(0);
    sound.start();
  }

  public void stopAllMusic() {
    intro.pause();
    grass.pause();
    maze.pause();
    nightMusic.pause();
  }

  public void update() {
    if (isCloseTo((player.x-camx)/tileSize, gladeSize, numOfColumns/2) && isCloseTo((player.y-camy)/tileSize, gladeSize, numOfColumns/2)) {
      if (music!=0) {
        stopAllMusic();
        playMusic(grass);
        music=0;
      }
    } else {
      if (night) {
        if (music!=1) {
          stopAllMusic();
          playMusic(nightMusic);
          music=1;
        }
      } else {
        if (music!=2) {
          stopAllMusic();
          playMusic(maze);
          music=2;
        }
      }
    }
  }
}
class BotSpawner {
  int[] movementSteps = new int[amountOfBots];
  int[] spawnPoint = new int[2];
  int[] animationFrame = new int[amountOfBots];
  PImage[] botImage = new PImage[4];
  int[] movementProgress = new int[amountOfBots];
  ArrayList[] paths = (ArrayList[])new ArrayList[amountOfBots];
  int[] calculatedSpots=new int[amountOfBots];
  int botNumber;
  boolean[] stopCalculating = new boolean[amountOfBots];
  boolean terminate;
  Node target;

  Node[] spawnNode = new Node[amountOfBots];
  //int[] prevPos = new int[amountOfBots][2];
  BotSpawner() {
    //for(int i = 0; i<movementSteps.length; i++){
    //  movementSteps[i]=6+int(random(3));
    //}
    movementProgress=fillWithNumber(movementProgress, 1);
  }

  /*
  To get the places where they should spawn:
   
   if(spawnPoint[0]==tileSize){
   //Bots have to spawn from left to right   (x increases)
   } else if (spawnPoint[0]==tileSize*numOfColumns){
   //Bots have to spawn from right to left (x decreases)
   } else if (spawnPoint[1]==tileSize){
   //Bots have to spawn from upper to down (y increases)
   } else if (spawnPoint[1]==tileSize*numOfColumns){
   //Bots have to spawn from down to upper (y decreases)
   }
   */
  public void spawnBots() {
    stopCalculating=setFalse(stopCalculating);
    terminate=false;
    int attempt=0;
    for (int i = 0; i<amountOfBots; i++) {
      animationFrame[i]=PApplet.parseInt(random(botImage.length));
      while (true) {
        Node tryToSpawnAt = (Node)bfs.nodes.get(0);
        if (spawnPoint[0]==tileSize) {
          tryToSpawnAt=(Node)bfs.nodes.get(getNodeAtPosition(spawnPoint[0]+(tileSize/6)+((tileSize/3)*attempt), spawnPoint[1]+(tileSize/6)));
        } else if (spawnPoint[0]==tileSize*numOfColumns) {
          tryToSpawnAt=(Node)bfs.nodes.get(getNodeAtPosition(spawnPoint[0]-(tileSize/6)-((tileSize/3)*attempt), spawnPoint[1]+(tileSize/6)));
        } else if (spawnPoint[1]==tileSize) {
          tryToSpawnAt=(Node)bfs.nodes.get(getNodeAtPosition(spawnPoint[0]+(tileSize/6), spawnPoint[1]+(tileSize/6)+((tileSize/3)*attempt)));
        } else if (spawnPoint[1]==tileSize*numOfColumns) {
          tryToSpawnAt=(Node)bfs.nodes.get(getNodeAtPosition(spawnPoint[0]+(tileSize/6), spawnPoint[1]-(tileSize/6)-((tileSize/3)*attempt)));
        }
        attempt+=5;
        if (tryToSpawnAt.walkable) {
          bots[i]=tryToSpawnAt;
          spawnNode[i]=tryToSpawnAt;
          calculateClosestPathToPlayer(i);
          //println((i+1)+". "+tryToSpawnAt);
          break;
        }
      }
    }
    //println(spawnPoint[0]+", "+spawnPoint[1]);
  }

  public boolean[] setFalse(boolean[] array) {
    for (int i = 0; i<array.length; i++) {
      array[i]=false;
    }
    return array;
  }

  public void calculateClosestPathToPlayer(int botNumber) {
    this.botNumber=botNumber;
    calculatedSpots[botNumber]=2;
    boolean doNotParse=false;
    targetNode = getNodeAtPosition(player.x-camx-1, player.y-camy-1);
    //println(targetNode);
    //println(targetNode);
    //targetNode=42644-20;
    Node target = (Node)bfs.nodes.get(targetNode);
    if (target.walkable==false) {
      //println("It seems to be false");
      int triedx=getLowestValue(player.x-camx, tileSize/3), triedy=getLowestValue(player.y-camy, tileSize/3);
      if (triedx>tileSize/6 && ((Node)bfs.nodes.get(targetNode+1)).walkable) {
        target=(Node)bfs.nodes.get(targetNode+1);
      } else if (triedx<=tileSize/6 && ((Node)bfs.nodes.get(targetNode-1)).walkable) {
        target=(Node)bfs.nodes.get(targetNode-1);
      } else if (triedy<=tileSize/6 && ((Node)bfs.nodes.get(targetNode-(numOfColumns*3)+3)).walkable) {
        target=(Node)bfs.nodes.get(targetNode-(numOfColumns*3)+3);
      } else if (triedy>tileSize/6 && ((Node)bfs.nodes.get(targetNode+(numOfColumns*3)-3)).walkable) {
        target=(Node)bfs.nodes.get(targetNode+(numOfColumns*3)-3);
      } else if (triedy>tileSize/6 && triedx>tileSize/6 && ((Node)bfs.nodes.get(targetNode+(numOfColumns*3)-2)).walkable) {
        target=(Node)bfs.nodes.get(targetNode+(numOfColumns*3)-2);
      } else if (triedy>tileSize/6 && triedx<=tileSize/6 && ((Node)bfs.nodes.get(targetNode+(numOfColumns*3)-4)).walkable) {
        target=(Node)bfs.nodes.get(targetNode+(numOfColumns*3)-4);
      } else if (triedy<=tileSize/6 && triedx<=tileSize/6 && ((Node)bfs.nodes.get(targetNode-(numOfColumns*3)+2)).walkable) {
        target=(Node)bfs.nodes.get(targetNode-(numOfColumns*3)+2);
      } else if (triedy<=tileSize/6 && triedx>tileSize/6 && ((Node)bfs.nodes.get(targetNode-(numOfColumns*3)+4)).walkable) {
        target=(Node)bfs.nodes.get(targetNode-(numOfColumns*3)+4);
      } else {
        doNotParse=true;
        println("Not parsing");
      }
    }
    this.target=target;
    if (/*!gladeNodes.contains(target)*/!(isCloseTo((player.x-camx)/tileSize, gladeSize, numOfColumns/2) && isCloseTo((player.y-camy)/tileSize, gladeSize, numOfColumns/2)) && !doNotParse) {
      paths[botNumber] = new ArrayList();
      ExecutorService es = Executors.newFixedThreadPool(1); // You only asked for 1 thread
      Future<?> future = es.submit( new PathFinding() );
      try {
        future.get(200, TimeUnit.MILLISECONDS); // This waits timeout seconds; returns null
      } 
      catch(TimeoutException e) {
        future.cancel(true);
        stopCalculating[botNumber]=true;
      } 
      catch(ExecutionException e) {
        future.cancel(true);
        stopCalculating[botNumber]=true;
      }
      catch(InterruptedException e) {
        future.cancel(true);
        stopCalculating[botNumber]=true;
      }

      //paths[botNumber] = bfs.bfs(bots[botNumber], target);
      //paths[botNumber] = removeDiagonals(paths[botNumber]);
    } else {
      paths[botNumber] = new ArrayList();
    }
  }

  public void botsBackToSpawn() {
    terminate=true;
    for (int i = 0; i<amountOfBots; i++) {
      calculatedSpots[i]=2;
      paths[i] = bfs.bfs(bots[i], spawnNode[i]);
    }
  }

  public void drawBot() {
    for (int i  = 0; i<amountOfBots; i++) {
      if (bots[i]!=null) {
        animateTo(PApplet.parseInt(bots[i].x+camx+(2*(tileSize/3))-(tileSize/6)), PApplet.parseInt(bots[i].y+camy+(2*(tileSize/3))-(tileSize/6)), movementProgress[i], movementSteps[i], i, bots[i]);
        if (movementProgress[i]>=movementSteps[i]) {
          movementProgress[i]=1;
        } else {
          movementProgress[i]++;
          continue;
        }
        if (paths[i]!=null && paths[i].size()-calculatedSpots[i]>=0) {
          bots[i] = (Node)paths[i].get(paths[i].size()-calculatedSpots[i]);
          calculatedSpots[i]++;
        } else if (terminate) {
          for (int j = 0; j<amountOfBots; j++) {
            bots[j]=null;
          }
        } else if (!stopCalculating[i]) {
          calculateClosestPathToPlayer(i);
        }
        //noStroke();
        //fill(40, 40, 240);
        //println((i+1)+". "+(bots[i].x+camx+(2*(tileSize/3)))+", "+(bots[i].y+camy+(2*(tileSize/3))));
      }
    }
  }

  public void animateTo(int x, int y, int frame, int numOfFrames, int botNumber, Node bot) {
    if (paths[botNumber].size()-calculatedSpots[botNumber]>=0) {
      int newx = PApplet.parseInt(((Node)paths[botNumber].get(paths[botNumber].size()-calculatedSpots[botNumber])).x+camx+(2*(tileSize/3)));
      int newy= PApplet.parseInt(((Node)paths[botNumber].get(paths[botNumber].size()-calculatedSpots[botNumber])).y+camy+(2*(tileSize/3)));
      //println("frame="+frame);
      //println((float(frame)/float(numOfFrames)));
      if (x>newx &&y>newy-(tileSize/6)) {
        setTransparentImage(PApplet.parseInt(x-((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*(tileSize/3))), PApplet.parseInt(y-((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*PApplet.parseFloat(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (x>newx && y<newy-(tileSize/6)) {
        setTransparentImage(PApplet.parseInt(x-((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*(tileSize/3))), PApplet.parseInt(y+((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*PApplet.parseFloat(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (x<newx-(tileSize/6) && y>newy-(tileSize/6)) {
        setTransparentImage(PApplet.parseInt(x+((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*PApplet.parseFloat(tileSize/3))), PApplet.parseInt(y-((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*PApplet.parseFloat(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (x<newx-(tileSize/6) &&  y<newy-(tileSize/6)) {
        setTransparentImage(PApplet.parseInt(x+((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*PApplet.parseFloat(tileSize/3))), PApplet.parseInt(y+((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*PApplet.parseFloat(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (x>newx) {
        setTransparentImage(PApplet.parseInt(x-((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*(tileSize/3))), y, botImage[animationFrame[botNumber]]);
      } else if (x<newx-tileSize/6) {
        setTransparentImage(PApplet.parseInt(x+((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*PApplet.parseFloat(tileSize/3))), y, botImage[animationFrame[botNumber]]);
      } else if (y>newy-(tileSize/6)) {
        setTransparentImage(x, PApplet.parseInt(y-((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*PApplet.parseFloat(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (y<newy-(tileSize/6)) {
        setTransparentImage(x, PApplet.parseInt(y+((PApplet.parseFloat(frame)/PApplet.parseFloat(numOfFrames))*PApplet.parseFloat(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else {
        println("Critical Error!!");
      }
    } else {
      setTransparentImage(x, y, botImage[animationFrame[botNumber]]);
      //println(millis()+", "+x+", "+y+", "+frame);
    }
    if (frame==1) {
      animationFrame[botNumber]++;
    }
    if (animationFrame[botNumber]>=botImage.length) {
      animationFrame[botNumber]=0;
    }
  }

  public void checkCloseToPlayer() {
    for (int i  = 0; i<amountOfBots; i++) {
      if (bots[i]!=null) {
        //println((int(bots[i].x)+camx+(3*(tileSize/3))-(tileSize/6))+", "+(player.x+(player.size/2)));
        if (isCloseTo(PApplet.parseInt(bots[i].x)+camx+(3*(tileSize/3))-(tileSize/6), tileSize/2, player.x+(player.size/2)) && isCloseTo(PApplet.parseInt(bots[i].y)+camy+(3*(tileSize/3))-(tileSize/6), tileSize/2, player.y+(player.size/2)) && player.inMaze()) {
          player.health-=grieverDamage;
        }
      }
    }
  }

  public void checkCloseToTrap() {
    for (int i  = 0; i<amountOfBots; i++) {
      if (bots[i]!=null) {
        for (int j = 0; j<traps.size(); j++) {
          //println((int(bots[i].x)+camx+(2*(tileSize/3))-(tileSize/6))+", "+(traps.get(j).x+(tileSize/12)+camx));
          if (isCloseTo(PApplet.parseInt(bots[i].x)+camx+(3*(tileSize/3))-(tileSize/6), tileSize/2, traps.get(j).x+(tileSize/12)+camx) && isCloseTo(PApplet.parseInt(bots[i].y)+camy+(3*(tileSize/3))-(tileSize/6), tileSize/2, traps.get(j).y+(tileSize/12)+camy)) {
            movementSteps[i]*=2;
            traps.get(j).removeTrap();
          }
        }
      }
    }
  }

  /*ArrayList removeDiagonals(ArrayList path) {
   println("Diagonal remove initialized"+path.size());
   for (int i=0; i<path.size()-1; i++) {
   int x = getNodeX((Node)path.get(i));
   int y = getNodeY((Node)path.get(i));
   int x2 = getNodeX((Node)path.get(i+1));
   int y2 = getNodeY((Node)path.get(i+1)); 
   if (x>x2 &&y>y2-(tileSize/6)) {
   if (getNodeAt(x-(tileSize/6), y).walkable) {
   path.add(i+1, getNodeAt(x-(tileSize/6), y));
   println(((Node)path.get(i))+"=="+getNodeAt(x, y));
   println(((Node)path.get(i)).x+", "+getNodeAt(x, y).y);
   } else if (getNodeAt(x, y-(tileSize/6)).walkable) {
   path.add(i+1, getNodeAt(x, y-(tileSize/6)));
   }
   i++;
   } else if (x>x2 && y<y2-(tileSize/6)) {
   if (getNodeAt(x-(tileSize/6), y).walkable) {
   path.add(i+1, getNodeAt(x-(tileSize/6), y));
   } else if (getNodeAt(x, y+(tileSize/6)).walkable) {
   path.add(i+1,getNodeAt(x, y+(tileSize/6)));
   }
   i++;
   } else if (x<x2-(tileSize/6) && y>y2-(tileSize/6)) {
   if (getNodeAt(x+(tileSize/6), y).walkable) {
   path.add(i+1, getNodeAt(x+(tileSize/6), y));
   } else if (getNodeAt(x, y-(tileSize/6)).walkable) {
   path.add(i+1, getNodeAt(x, y-(tileSize/6)));
   }
   i++;
   } else if (x<x2-(tileSize/6) &&  y<y2-(tileSize/6)) {
   if (getNodeAt(x+(tileSize/6), y).walkable) {
   path.add(i+1, getNodeAt(x+(tileSize/6), y));
   } else if (getNodeAt(x, y+(tileSize/6)).walkable) {
   path.add(i+1, getNodeAt(x, y+(tileSize/6)));
   }
   i++;
   }
   }
   return path;
   }
   
   
   
   int getNodeX(Node node) {
   return int(node.x-camx+(2*(tileSize/3)));
   }
   
   int getNodeY(Node node) {
   return int(node.y-camy+(2*(tileSize/3)));
   }*/
}

class PathFinding extends Thread
{
  public void run()
  {
    botSpawner.paths[botSpawner.botNumber] = bfs.bfs(bots[botSpawner.botNumber], botSpawner.target);
  }
}
class Item {
  int id, usesLeft=1;
  String name;
  Item(int id, boolean actually) {
    this.id=id;
    if (player.inventory.size()==0 && actually) {
      player.selectedItem=id;
    }
    if (actually) {
      player.inventoryStrings[id]=changeNumber(player.inventoryStrings[id], true);
      player.selectedItem=id;
    }
    name=getItemName(id);
  }

  public String getItemName(int id) {
    switch(id) {
    case 0:
      return "Wood";
    case 1:
      return "Wheat";
    case 2:
      return "Bread";
    case 3:
      usesLeft=8;
      return "Teleportation Rod";
    case 4:
      return "Wheat Seeds";
    case 5:
      return "Porkchop";
    case 6:
      return "Cooked Porkchop";
    case 7:
      return "Tree Seeds";
    case 8:
      return "Cotton Seeds";
    case 9:
      return "Shoes";
    case 10:
      return "Magic Map";
    case 11:
      return "Spike Trap";
    case 12:
      usesLeft=50;
      return "Axe";
    case 13:
      usesLeft=10;
      return "Lighter";
    case 14:
      return "Magical Dust";
    case 15:
      return "Cotton";
    case 16:
      return "Paper";
    case 17:
      return "Iron";
    case 18:
      usesLeft=10;
      return "Pickaxe";
    case 19:
      return "Stick";
    case 20:
      return "Steel Plate";
    }
    return "Unknown ItemID";
  }

  public void use() {
    switch(id) {
    case 0:
      if (isCloseTo((player.x-camx)/tileSize, gladeSize-1, numOfColumns/2) && isCloseTo((player.y-camy)/tileSize, gladeSize-1, numOfColumns/2)) {
        new Wood(player.x-camx-(tileSize/12), player.y-camy-(tileSize/3));
        removeFromInventory();
      }
      break;
    case 1:
      for (int i = 0; i<pigs.size(); i++) {
        if (pigs.get(i).feed()) {
          break;
        }
      }
      break;
    case 2:
      player.feed(5000);
      break;
    case 3:
      Poort randomPoort;
      while (true) {
        int x=PApplet.parseInt(random(poorten.length)), y=PApplet.parseInt(random(poorten[0].length));
        if (isCloseTo((x+3)/3, gladeSize+2, numOfColumns/2) && isCloseTo((y+3)/3, gladeSize+2, numOfColumns/2)) {
          randomPoort=poorten[x][y];
          break;
        }
      }
      player.x=camx+randomPoort.x+(tileSize/3);
      player.y=camy+randomPoort.y+(tileSize/3);
      usesLeft--;
      break;
    case 4:
      farmland.plantAtClosestFarmland(0);
      break;
    case 5:
      boolean doEat=true;
      for (int i = 0; i<woodBlock.size(); i++) {
        if (woodBlock.get(i).cookPork()) {
          doEat=false;
        }
      }
      if (doEat) {
        player.feed(2500);
      }
      break;
    case 6:
      player.feed(10000);
      break;
    case 7:
      if (isCloseTo((player.x-camx)/tileSize, gladeSize-1, numOfColumns/2) && isCloseTo((player.y-camy)/tileSize, gladeSize-1, numOfColumns/2)) {
        new Tree(player.x-camx-(tileSize/12), player.y-camy-(tileSize/3));
        removeFromInventory();
      }
      break;
    case 8:
      farmland.plantAtClosestFarmland(1);
      break;
    case 9:
      player.canRun=true;
      removeFromInventory();
      break;
    case 10:
      scherm=4;
      notDrawn=true;
      break;
    case 11:
      if (player.inMaze()) {
        traps.add(new Trap(player.x-camx-(tileSize/12), player.y-camy-(tileSize/3)));
        removeFromInventory();
      }
      break;
    case 12:
      for (int i = 0; i<trees.size(); i++) {
        trees.get(i).harvest();
      }
      for (int i = 0; i<pigs.size(); i++) {
        pigs.get(i).kill();
      }
      for (int i = 0; i<woodBlock.size(); i++) {
        woodBlock.get(i).harvest();
      }
      usesLeft--;
      break;
    case 13:
      for (int i = 0; i<woodBlock.size(); i++) {
        woodBlock.get(i).lightOnFire();
      }
      usesLeft--;
      break;
    case 14:
      break;
    case 15:
      break;
    case 16:
      break;
    case 17:
      break;
    case 18:
      mine.interact();
      usesLeft--;
      break;
    case 19:
      break;
    case 20:
      break;
    }
    if (usesLeft==0) {
      removeFromInventory();
    }
  }

  public String changeNumber(String number, boolean higher) {
    int i;
    for (i = 0; number.charAt(i)>='0' && number.charAt(i)<='9'; i++) {
    }
    int num = PApplet.parseInt(number.substring(0, i));
    if (higher) {
      return str(num+1)+number.substring(i);
    } 
    return str(num-1)+number.substring(i);
  }

  public int getNumber(String number) {
    int i;
    for (i = 0; number.charAt(i)>='0' && number.charAt(i)<='9'; i++) {
    }
    return PApplet.parseInt(number.substring(0, i));
  }

  public void removeFromInventory() {
    player.inventoryStrings[id]=changeNumber(player.inventoryStrings[id], false);
    player.inventory.remove(player.getSelectedItem());
    if (player.selectedItem>0 && getNumber(player.inventoryStrings[id])==0) {
      if (getSelectedItemDecrease()!=player.selectedItem) {
        player.selectedItem=getSelectedItemDecrease();
      } else if (getSelectedItemIncrease()!=player.selectedItem) {
        player.selectedItem=getSelectedItemIncrease();
      }
    }
  }
}
class Mine {
  final int spawnAtNode = 200;
  int x, y;
  Mine() {
    x=PApplet.parseInt(gladeNodes.get(spawnAtNode).x);
    y=PApplet.parseInt(gladeNodes.get(spawnAtNode).y);
    player.collisions.add(x+(tileSize/24));
    player.collisions.add(y+(tileSize/24));
    player.collisions.add(PApplet.parseInt(tileSize/1.1f));
    player.collisions.add(PApplet.parseInt(tileSize/1.6f));
  }

  public void teken() {
    setTransparentImage(x+camx, y+camy, mineImage);
  }

  public void interact() {
    if (veryCloseToPlayer()) {
      player.health-=random(30);
      player.currentFood-=PApplet.parseInt(random(3000));
      int random = PApplet.parseInt(random(3));
      for (int i = 0; i<random; i++) {
        player.inventory.add(new Item(17, true));
      }
    }
  }

  public boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(PApplet.parseInt(x)+camx+(tileSize/2), tileSize, player.x+(player.size/2)) && isCloseTo(PApplet.parseInt(y)+camy+PApplet.parseInt(tileSize/1.5f), tileSize, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}
class Pig {
  float x, y;
  int walkingProgress=0;
  int speed=30; //higher speed means slower pig --> yupp, mindfuck!
  int[] animationFrames = new int[2];
  boolean followPlayer = false;
  int wheatEaten=0;
  //int collisionBox;

  Pig(int x, int y) {
    this.x=x;
    this.y=y;
    animationFrames[0]=0;
    animationFrames[1]=PApplet.parseInt(random(4));
    //collisionBox=player.collisions.size();
    //player.collisions.add(x);player.collisions.add(y);player.collisions.add(int(tileSize/1.5));player.collisions.add(tileSize/3);
    //getNodeAt(x-(tileSize/3),y-(tileSize/3)).walkable=false;
  }

  public void teken() {
    setTransparentImage(PApplet.parseInt(x)+camx, PApplet.parseInt(y)+camy, pigImage[animationFrames[0]][animationFrames[1]]);
  }

  public void wander() {
    if (walkingProgress==0) {
      while (true) {
        int random = PApplet.parseInt(random(4));
        if (random==0) {//Node links
          animationFrames[0]=0;
          break;
        } else if (random==1) {//Node rechts
          animationFrames[0]=1;
          break;
        } else if (random==2) {//Node onder
          animationFrames[0]=2;
          break;
        } else if (random==3) {//Node boven
          animationFrames[0]=3;
          break;
        }
      }
    }
    boolean doFurtherParsing = true;
    if (animationFrames[0]==0) {
      if (!isCollisionBox(x-((tileSize/3)/speed)+camx, y+camy) && (isCloseTo(PApplet.parseInt((x-((tileSize/3)/speed))/tileSize), gladeSize-1, numOfColumns/2) && isCloseTo(PApplet.parseInt(y/tileSize), gladeSize-1, numOfColumns/2))) {
        x-=(tileSize/3)/speed;
      } else {
        walkingProgress=0;
        doFurtherParsing=false;
      }
    } else if (animationFrames[0]==1) {
      if (!isCollisionBox(x+((tileSize/3)/speed)+camx, y+camy) && (isCloseTo(PApplet.parseInt((x+((tileSize/3)/speed))/tileSize), gladeSize-1, numOfColumns/2) && isCloseTo(PApplet.parseInt(y/tileSize), gladeSize-1, numOfColumns/2))) {
        x+=(tileSize/3)/speed;
      } else {
        walkingProgress=0;
        doFurtherParsing=false;
      }
    } else if (animationFrames[0]==2) {
      if (!isCollisionBox(x+camx, y+((tileSize/3)/speed)+camy) && (isCloseTo(PApplet.parseInt(x/tileSize), gladeSize-1, numOfColumns/2) && isCloseTo(PApplet.parseInt((y+((tileSize/3)/speed))/tileSize), gladeSize-1, numOfColumns/2))) {
        y+=(tileSize/3)/speed;
      } else {
        walkingProgress=0;
        doFurtherParsing=false;
      }
    } else if (animationFrames[0]==3) {
      if (!isCollisionBox(x+camx, y-((tileSize/3)/speed)+camy) && (isCloseTo(PApplet.parseInt(x/tileSize), gladeSize-1, numOfColumns/2) && isCloseTo(PApplet.parseInt((y-((tileSize/3)/speed))/tileSize), gladeSize-1, numOfColumns/2))) {
        y-=(tileSize/3)/speed;
      } else {
        walkingProgress=0;
        doFurtherParsing=false;
      }
    }
    if (doFurtherParsing) {
      walkingProgress++;
      if (isDivisible(walkingProgress, 7)) {
        animationFrames[1]++;
        if (animationFrames[1]==4) {
          animationFrames[1]=0;
        }
      }
      if (walkingProgress==speed) {
        walkingProgress=0;
      }
    }
  }

  public boolean isCollisionBox(float x, float y) {
    x=x-camx;
    y=y-camy;
    for (int i = 0; i<player.collisions.size (); i+=4) {
      //println(x+", "+collisions.get(i));
      if (x>player.collisions.get(i)-(tileSize/3) && y>player.collisions.get(i+1)-(tileSize/3) && x<player.collisions.get(i)+player.collisions.get(i+2)+(tileSize/3) && y<player.collisions.get(i+1)+player.collisions.get(i+3)+(tileSize/3)) {
        return true;
      }
    }
    return false;
  }

  public void kill() {
    if (veryCloseToPlayer()) {
      int j=PApplet.parseInt(wheatEaten*random(0.5f));
      for (int i = 0; i<1+j; i++) {
        player.inventory.add(new Item(5, true));
      }
      //removeCollisionBox(collisionBox);
      pigs.remove(this);
    }
  }

  public boolean feed() {
    if (veryCloseToPlayer()) {
      player.getSelectedItem().removeFromInventory();
      wheatEaten++;
      return true;
    }
    return false;
  }

  public boolean isCloserToPlayer(int x, int y, int x2, int y2) {//if the distance of x and y is smaller to the player than x2 and y2 it will return true
    if (player.getDistance(x, y, player.x-camx, player.y-camy)<player.getDistance(x2, y2, player.x-camx, player.y-camy)) {
      return true;
    }
    return false;
  }

  public boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(PApplet.parseInt(x)+camx, tileSize, player.x+(player.size/2)) && isCloseTo(PApplet.parseInt(y)+camy, tileSize, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}
class SaveLoad {
  String prefix;
  String timeStamp;
  SaveLoad() {
    timeStamp= day()+"-"+month()+"-"+year()+" at "+hour()+"h"+minute()+"m"+second()+"s";
  }

  public void saveGame() {
    String[] array = new String[0];
    for (int i = 0; i<gameSetup.length; i++) {
      array = append(array, str(gameSetup[i]));
    }
    array = append(array, str(amountOfBots));
    array = append(array, str(tileSize));
    array = append(array, str(gladeSize));
    array = append(array, str(camx));
    array = append(array, str(camy));
    array = append(array, str(borderSize));
    array = append(array, str(night));
    array = append(array, str(targetNode));
    array = append(array, str(daytime));
    array = append(array, str(vision));
    array = append(array, str(day));
    array = append(array, str(canMove));
    array = append(array, str(notDrawn));
    array = append(array, str(playerNodes[0]));
    array = append(array, str(playerNodes[1]));
    array = append(array, playerName);
    saveStrings(path+"gameSettings.txt", array);
    /*dayLength;
     amountOfBots;
     numOfColumns;
     tileSize;
     gladeSize;
     camx;
     camy; 
     borderSize=3;
     night);
     targetNode=0;
     daytime;
     vision;
     scherm;
     day;
     canMove);
     notDrawn);
     playerNodes[0];
     playerNodes[1];
     chestSpawnRate;
     grieverDamage;
     maxFood;*/

    /*
DOES NOT WORK LIKE THIS!
     
     array = new String[0];
     for(int i = 0; i<gladeNodes.size(); i++){
     array = append(array,str(gladeNodes.get(i).x));
     array = append(array,str(gladeNodes.get(i).y));
     }
     saveStrings("gladeNodes.txt",array);
     */

    array = new String[0];
    for (int i = 0; i<trees.size (); i++) {
      array = append(array, str(trees.get(i).x));
      array = append(array, str(trees.get(i).y));
      array = append(array, str(trees.get(i).createdTrees));
      array = append(array, str(trees.get(i).state));
    }
    saveStrings(path+"trees.txt", array);

    array = new String[0];
    for (int i = 0; i<woodBlock.size (); i++) {
      array = append(array, str(woodBlock.get(i).x));
      array = append(array, str(woodBlock.get(i).y));
      array = append(array, str(woodBlock.get(i).currentCycle));
      array = append(array, str(woodBlock.get(i).currentImage));
      array = append(array, str(woodBlock.get(i).isBurning));
      array = append(array, str(woodBlock.get(i).isCraftingTable));
    }
    saveStrings(path+"woodBlock.txt", array);

    array = new String[0];
    for (int i = 0; i<pigs.size (); i++) {
      array = append(array, str(pigs.get(i).x));
      array = append(array, str(pigs.get(i).y));
      array = append(array, str(pigs.get(i).animationFrames[0]));
      array = append(array, str(pigs.get(i).animationFrames[1]));
      array = append(array, str(pigs.get(i).wheatEaten));
      array = append(array, str(pigs.get(i).walkingProgress));
      array = append(array, str(pigs.get(i).followPlayer));
    }
    saveStrings(path+"pigs.txt", array);

    array = new String[0];
    for (int i = 0; i<traps.size (); i++) {
      array = append(array, str(traps.get(i).x));
      array = append(array, str(traps.get(i).y));
    }
    saveStrings(path+"traps.txt", array);

    long milliCount=millis()+7000;
    array = new String[0];
  outerloop:
    for (int i = 0; i<poorten.length; i++) {
      for (int j = 0; j<poorten.length; j++) {
        if (poorten[i][j]!=null) {
          array = append(array, str(poorten[i][j].an));
          array = append(array, str(poorten[i][j].toBeAn));
          for (int k =0; k<poorten[i][j].kleppen.length; k++) {
            array = append(array, str(poorten[i][j].kleppen[k].an));
            array = append(array, str(poorten[i][j].kleppen[k].hasChest));
          }
          array = append(array, "-");
        }
        if (millis()>milliCount) {//give it 7 seconds to save
          array = new String[0];
          array = append(array, "null");
          break outerloop;
        }
      }
    }
    saveStrings(path+"poorten.txt", array);
    /*
array = new String[0];
     for(int i = 0; i<nodeRegister.length; i++){
     for(int j = 0; j<nodeRegister.length; j++){
     array = append(array,str(nodeRegister[i][j].x));
     array = append(array,str(nodeRegister[i][j].y));
     }
     saveStrings("nodeRegister.txt",array);
     */
    array = new String[0];
    for (int i = 0; i<lijnen.length; i++) {
      array = append(array, str(lijnen[i].sizex));
      array = append(array, str(lijnen[i].sizey));
      array = append(array, str(lijnen[i].toBeSizeX));
      array = append(array, str(lijnen[i].toBeSizeY));
      array = append(array, str(lijnen[i].type));
      array = append(array, str(lijnen[i].isDoor));
    }
    saveStrings(path+"lijnen.txt", array);

    //Node[] bots = new Node[amountOfBots];

    array = new String[0];
    array = append(array, str(player.x));
    array = append(array, str(player.y));
    array = append(array, str(player.health));
    array = append(array, str(player.currentFood));
    array = append(array, str(player.selectedItem));
    array = append(array, str(player.animationFrames[0]));
    array = append(array, str(player.animationFrames[1]));
    array = append(array, str(player.animationFrames[2]));
    array = append(array, str(player.canRun));
    saveStrings(path+"player.txt", array);

    array = new String[0];
    for (int i = 0; i<player.inventory.size (); i++) {
      array = append(array, str(player.inventory.get(i).id));
      array = append(array, str(player.inventory.get(i).usesLeft));
    }
    saveStrings(path+"playerInventory.txt", array);

    saveStrings(path+"inventoryStrings.txt", player.inventoryStrings);

    array = new String[0];
    for (int i = 0; i<farmland.crops.length; i++) {
      for (int j = 0; j<farmland.crops[0].length; j++) {
        array = append(array, str(farmland.crops[i][j].state));
        array = append(array, str(farmland.crops[i][j].type));
      }
    }
    saveStrings(path+"farmland.txt", array);

    array = new String[0];
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      array = append(array, str(botSpawner.movementSteps[i]));
    }
    array = append(array, str(botSpawner.spawnPoint[0]));
    array = append(array, str(botSpawner.spawnPoint[1]));
    for (int i = 0; i<botSpawner.animationFrame.length; i++) {
      array = append(array, str(botSpawner.animationFrame[i]));
    }
    for (int i = 0; i<botSpawner.movementProgress.length; i++) {
      array = append(array, str(botSpawner.movementProgress[i]));
    }
    for (int i = 0; i<botSpawner.calculatedSpots.length; i++) {
      array = append(array, str(botSpawner.calculatedSpots[i]));
    }
    for (int i = 0; i<botSpawner.stopCalculating.length; i++) {
      array = append(array, str(botSpawner.stopCalculating[i]));
    }
    array = append(array, str(botSpawner.terminate));
    saveStrings(path+"botSpawner.txt", array);

    array = new String[0];
    if (botSpawner.spawnNode[0]==null) {
      array = append(array, "null");
    } else {
      for (int i = 0; i<botSpawner.spawnNode.length; i++) {
        array = append(array, str(botSpawner.spawnNode[i].x+(tileSize/3)));
        array = append(array, str(botSpawner.spawnNode[i].y+(tileSize/3)));
      }
    }
    if (bots[0]==null) {
      array = append(array, "null");
    } else {
      for (int i = 0; i<bots.length; i++) {
        array = append(array, str(bots[i].x+(tileSize/3)));
        array = append(array, str(bots[i].y+(tileSize/3)));
      }
    }
    saveStrings(path+"nodes.txt", array);
  }

  public void loadGame() {
    int l;
    String[] array = loadStrings(path+"gameSettings.txt");
    for (int i = 0; i<gameSetup.length; i++) {
      gameSetup[i]=PApplet.parseByte(PApplet.parseInt(array[i]));
    }
    doGameSetup();
    cleanupGame();
    setupGame();
    amountOfBots=PApplet.parseInt(array[6]);
    tileSize=PApplet.parseInt(array[7]);
    gladeSize = PApplet.parseInt(array[8]);
    camx = PApplet.parseInt(array[9]);
    camy = PApplet.parseInt(array[10]);
    borderSize = PApplet.parseInt(array[11]);
    night = PApplet.parseBoolean(array[12]);
    targetNode = PApplet.parseInt(array[13]);
    daytime = PApplet.parseInt(array[14]);
    vision = PApplet.parseInt(array[15]);
    day = PApplet.parseInt(array[16]);
    canMove = PApplet.parseBoolean(array[17]);
    notDrawn = PApplet.parseBoolean(array[18]);
    playerNodes[0] = PApplet.parseInt(array[19]);
    playerNodes[1] = PApplet.parseInt(array[20]);
    playerName = array[21];
    l=0;
    array = loadStrings(path+"trees.txt");
    for (int i = 0; i<array.length/4; i++) {
      new Tree(PApplet.parseInt(array[l+0]), PApplet.parseInt(array[l+1]));
      trees.get(i).createdTrees = PApplet.parseInt(array[l+2]);
      trees.get(i).state = PApplet.parseInt(array[l+3]);
      l+=4;
    }
    l=0;
    array = loadStrings(path+"woodBlock.txt");
    for (int i = 0; i<array.length/6; i++) {
      new Wood(PApplet.parseInt(array[l+0]), PApplet.parseInt(array[l+1]));
      woodBlock.get(i).currentCycle = PApplet.parseInt(array[l+2]);
      woodBlock.get(i).currentImage = PApplet.parseInt(array[l+3]);
      woodBlock.get(i).isBurning = PApplet.parseBoolean(array[l+4]);
      woodBlock.get(i).isCraftingTable = PApplet.parseBoolean(array[l+5]);
      l+=6;
    }

    pigs.clear();
    array = loadStrings(path+"pigs.txt");
    l=0;
    for (int i = 0; i<array.length/7; i++) {
      pigs.add(new Pig(PApplet.parseInt(array[l]), PApplet.parseInt(array[l+1])));
      pigs.get(i).animationFrames[0] = PApplet.parseInt(array[l+2]);
      pigs.get(i).animationFrames[1] = PApplet.parseInt(array[l+3]);
      pigs.get(i).wheatEaten = PApplet.parseInt(array[l+4]);
      pigs.get(i).walkingProgress = PApplet.parseInt(array[l+5]);
      pigs.get(i).followPlayer = PApplet.parseBoolean(array[l+6]);
      l+=7;
    }

    array = loadStrings(path+"traps.txt");
    l=0;
    for (int i = 0; i<array.length/2; i++) {
      traps.add(new Trap(PApplet.parseInt(array[l]), PApplet.parseInt(array[l+1])));
      l+=2;
    }

    array = loadStrings(path+"poorten.txt");
    l = 0;
    if (!array[0].equals("null")) {
      for (int i = 0; i<poorten.length; i++) {
        for (int j = 0; j<poorten.length; j++) {
          if (poorten[i][j]!=null) {
            poorten[i][j].an=PApplet.parseInt(array[l]);
            poorten[i][j].toBeAn=PApplet.parseInt(array[l+1]);
            poorten[i][j].kleppen = new klep[0];
            l+=2;
            int k = 0;
            while (!array[l].equals ("-")) {
              poorten[i][j].kleppen = (klep[]) append(poorten[i][j].kleppen, new klep(PApplet.parseInt(array[l])));
              poorten[i][j].kleppen[k].hasChest=PApplet.parseBoolean(array[l+1]);
              k++;
              l+=2;
            }
            l+=1;
          }
        }
      }
    }
    /*
array = new String[0];
     for(int i = 0; i<nodeRegister.length; i++){
     for(int j = 0; j<nodeRegister.length; j++){
     array = append(array,str(nodeRegister[i][j].x));
     array = append(array,str(nodeRegister[i][j].y));
     }
     saveStrings("nodeRegister.txt",array);
     */

    array = loadStrings(path+"lijnen.txt");
    l=0;
    for (int i = 0; i<lijnen.length; i++) {
      lijnen[i].sizex = PApplet.parseInt(array[l+0]);
      lijnen[i].sizey = PApplet.parseInt(array[l+1]);
      lijnen[i].toBeSizeX = PApplet.parseInt(array[l+2]);
      lijnen[i].toBeSizeY = PApplet.parseInt(array[l+3]);
      lijnen[i].type = PApplet.parseBoolean(array[l+4]);
      lijnen[i].isDoor = PApplet.parseBoolean(array[l+5]);
      l+=6;
    }

    //Node[] bots = new Node[amountOfBots];

    array = loadStrings(path+"player.txt");
    player.x = PApplet.parseInt(array[0]);
    player.y = PApplet.parseInt(array[1]);
    player.health = PApplet.parseInt(array[2]);
    player.currentFood = PApplet.parseInt(array[3]);
    player.selectedItem = PApplet.parseInt(array[4]);
    player.animationFrames[0] = PApplet.parseInt(array[5]);
    player.animationFrames[1] = PApplet.parseInt(array[6]);
    player.animationFrames[2] = PApplet.parseInt(array[7]);
    player.canRun = PApplet.parseBoolean(array[8]);

    player.inventory.clear();
    l=0;
    array = loadStrings(path+"playerInventory.txt");
    for (int i = 0; i<array.length/2; i++) {
      player.inventory.add(new Item(PApplet.parseInt(array[l]), true));
      player.inventory.get(i).usesLeft=PApplet.parseInt(array[l+1]);
      l+=2;
    }

    player.inventoryStrings = loadStrings(path+"inventoryStrings.txt");

    array = loadStrings(path+"farmland.txt");
    l = 0;
    for (int i = 0; i<farmland.crops.length; i++) {
      for (int j = 0; j<farmland.crops[0].length; j++) {
        farmland.crops[i][j].state= PApplet.parseInt(array[l]);
        farmland.crops[i][j].type= PApplet.parseInt(array[l+1]);
        l+=2;
      }
    }
    array = loadStrings(path+"botSpawner.txt");
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      botSpawner.movementSteps[i]=PApplet.parseInt(array[i]);
    }
    botSpawner.spawnPoint[0]=PApplet.parseInt(array[botSpawner.movementSteps.length]);
    botSpawner.spawnPoint[1]=PApplet.parseInt(array[botSpawner.movementSteps.length+1]);
    for (int i = 0; i<botSpawner.animationFrame.length; i++) {
      botSpawner.animationFrame[i]=PApplet.parseInt(array[botSpawner.movementSteps.length+2+i]);
    }
    for (int i = 0; i<botSpawner.movementProgress.length; i++) {
      botSpawner.movementProgress[i]=PApplet.parseInt(array[botSpawner.movementSteps.length+2+botSpawner.animationFrame.length+i]);
    }
    for (int i = 0; i<botSpawner.calculatedSpots.length; i++) {
      botSpawner.calculatedSpots[i]=PApplet.parseInt(array[botSpawner.movementSteps.length+2+botSpawner.animationFrame.length+i+botSpawner.movementProgress.length]);
    }
    for (int i = 0; i<botSpawner.stopCalculating.length; i++) {
      botSpawner.stopCalculating[i]=PApplet.parseBoolean(array[botSpawner.movementSteps.length+2+botSpawner.animationFrame.length+i+botSpawner.movementProgress.length+botSpawner.calculatedSpots.length]);
    }
    botSpawner.terminate=PApplet.parseBoolean(array[botSpawner.movementSteps.length+2+botSpawner.animationFrame.length+botSpawner.stopCalculating.length+botSpawner.movementProgress.length+botSpawner.calculatedSpots.length]);

    array = loadStrings(path+"nodes.txt");
    l=0;
    if (array[l].equals("null")) {
      l++;
    } else {
      println("in");
      for (int i = 0; i<botSpawner.spawnNode.length; i++) {
        botSpawner.spawnNode[i]=getNodeAt(PApplet.parseInt(array[l]), PApplet.parseInt(array[l+1]));
        l+=2;
      }
    }
    if (!array[l].equals("null")) {
      println("in2");
      for (int i = 0; i<bots.length; i++) {
        bots[i]=getNodeAt(PApplet.parseInt(array[l]), PApplet.parseInt(array[l+1]));
        l+=2;
      }
    }
    for (int i = 0; i<amountOfBots; i++) {
      botSpawner.calculateClosestPathToPlayer(i);
    }
  }

  public boolean fileExists(String filename) {
    File file = new File(sketchPath(filename));
    if (!file.exists()) {
      return false;
    }
    return true;
  }
}
class Shop {
  int[] shop = {
    5, 1, 1, 2, 1, 0, 1, 19, 1, 0, 1, 16, 3, 17, 1, 20, 10, 15, 1, 9
  };
  int[] shopMulti = {
    1, 14, 1, 19, 1, 3, 2, 14, 3, 16, 1, 10, 1, 19, 2, 0, 1, 18, 3, 17, 1, 20, 2, 11
  };
  int selectedSale = 0;

  Shop() {
  }

  public void teken() {
    setTransparentImage((width/2)-(height/4), height/2, craftingTable[1]);
    setTransparentImage((width/2)-(height/4), height/4, buttonImage[1]);
    setTransparentImage(0, 0, buttonImage[2]);
    textSize(width/30);
    if (selectedSale<shop.length/4) {
      outlinedText(shop[selectedSale*4]+" "+(new Item(shop[(selectedSale*4)+1], false).name), width/2, (height/2)+(height/8));
      outlinedText("makes", width/2, (height/2)+(2*(height/8)));
      outlinedText(shop[(selectedSale*4)+2]+" "+(new Item(shop[(selectedSale*4)+3], false).name), width/2, (height/2)+(3*(height/8)));
    } else {
      outlinedText(shopMulti[(selectedSale-(shop.length/4))*6]+" "+(new Item(shopMulti[((selectedSale-(shop.length/4))*6)+1], false).name), width/2, (height/2)+(height/10));
      outlinedText("+ "+shopMulti[((selectedSale-(shop.length/4))*6)+2]+" "+(new Item(shopMulti[((selectedSale-(shop.length/4))*6)+3], false).name), width/2, (height/2)+(2*(height/10)));
      outlinedText("makes", width/2, (height/2)+(3*(height/10)));
      outlinedText(shopMulti[((selectedSale-(shop.length/4))*6)+4]+" "+(new Item(shopMulti[((selectedSale-(shop.length/4))*6)+5], false).name), width/2, (height/2)+(4*(height/10)));
    }
  }

  public void drawOnce() {
    setTransparentImage((width/2)-(2*(height/4)), height/2, craftingTable[3]);
    setTransparentImage((width/2)+(height/4), height/2, craftingTable[2]);
  }

  public void outlinedText(String text, int x, int y) {
    fill(0, 0, 0);
    x-=height/4;
    y-=height/12;
    rectMode(CORNER);
    text(text, x-2, y, height/2, height); 
    text(text, x, y-2, height/2, height); 
    text(text, x+2, y, height/2, height); 
    text(text, x, y+2, height/2, height); 
    if (selectedSale<shop.length/4) {
      fill(255*booleanToInt(!purchase(false)), 255, 255*booleanToInt(!purchase(false)));
    } else {
      fill(255*booleanToInt(!purchaseMulti(false)), 255, 255*booleanToInt(!purchaseMulti(false)));
    }
    text(text, x, y, height/2, height);
  }

  public boolean purchase(boolean actually) {
    int amount = 0;
    int targetId =new Item(shop[selectedSale*4+1], false).id;
    for (int i = 0; i< player.inventory.size (); i++) {
      if (player.inventory.get(i).id==targetId) {
        amount++;
      }
    }
    if (amount>=shop[selectedSale*4]) {
      if (actually) {
        amount=shop[selectedSale*4];
        for (int i = 0; i<shop[selectedSale*4+2]; i++) {
          player.inventory.add(new Item(shop[selectedSale*4+3], true));
        }
        for (int i = 0; i< player.inventory.size (); i++) {
          if (player.inventory.get(i).id==targetId) {
            player.inventoryStrings[player.inventory.get(i).id]=player.inventory.get(i).changeNumber(player.inventoryStrings[player.inventory.get(i).id], false);
            if (player.inventoryStrings[player.selectedItem].charAt(0)=='0') {
              if (getSelectedItemDecrease()!=player.selectedItem) {
                player.selectedItem=getSelectedItemDecrease();
              } else if (getSelectedItemIncrease()!=player.selectedItem) {
                player.selectedItem=getSelectedItemIncrease();
              }
            }
            player.inventory.remove(i);
            amount--;
            i--;
            if (amount==0) {
              break;
            }
          }
        }
        return true;
      } else {
        return true;
      }
    }
    return false;
  }

  public boolean purchaseMulti(boolean actually) {
    int amount = 0;
    int targetId =new Item(shopMulti[(selectedSale-(shop.length/4))*6+1], false).id;
    for (int i = 0; i< player.inventory.size (); i++) {
      if (player.inventory.get(i).id==targetId) {
        amount++;
      }
    }
    if (amount>=shopMulti[(selectedSale-(shop.length/4))*6]) {
      amount=0;
      targetId =new Item(shopMulti[(selectedSale-(shop.length/4))*6+3], false).id;
      for (int i = 0; i< player.inventory.size (); i++) {
        if (player.inventory.get(i).id==targetId) {
          amount++;
        }
      }
      if (amount>=shopMulti[(selectedSale-(shop.length/4))*6+2]) {
        if (actually) {
          for (int i = 0; i<shopMulti[ (selectedSale- (shop.length/4))*6+4]; i++) {
            player.inventory.add(new Item(shopMulti[(selectedSale-(shop.length/4))*6+5], true));
          }
          amount=shopMulti[(selectedSale-(shop.length/4))*6];
          targetId =new Item(shopMulti[(selectedSale-(shop.length/4))*6+1], false).id;
          for (int i = 0; i< player.inventory.size (); i++) {
            if (player.inventory.get(i).id==targetId) {
              player.inventoryStrings[player.inventory.get(i).id]=player.inventory.get(i).changeNumber(player.inventoryStrings[player.inventory.get(i).id], false);
              if (player.inventoryStrings[player.selectedItem].charAt(0)=='0') {
                if (getSelectedItemDecrease()!=player.selectedItem) {
                  player.selectedItem=getSelectedItemDecrease();
                } else if (getSelectedItemIncrease()!=player.selectedItem) {
                  player.selectedItem=getSelectedItemIncrease();
                }
              }
              player.inventory.remove(i);
              amount--;
              i--;
              if (amount==0) {
                break;
              }
            }
          }
          amount=shopMulti[(selectedSale-(shop.length/4))*6+2];
          targetId =new Item(shopMulti[(selectedSale-(shop.length/4))*6+3], false).id;
          for (int i = 0; i< player.inventory.size (); i++) {
            if (player.inventory.get(i).id==targetId) {
              player.inventoryStrings[player.inventory.get(i).id]=player.inventory.get(i).changeNumber(player.inventoryStrings[player.inventory.get(i).id], false);
              if (player.selectedItem>0) {
                if (player.inventoryStrings[player.selectedItem].charAt(0)=='0') {
                  if (getSelectedItemDecrease()!=player.selectedItem) {
                    player.selectedItem=getSelectedItemDecrease();
                  } else if (getSelectedItemIncrease()!=player.selectedItem) {
                    player.selectedItem=getSelectedItemIncrease();
                  }
                }
              }
              player.inventory.remove(i);
              amount--;
              i--;
              if (amount==0) {
                break;
              }
            }
          }
          return true;
        } else {
          return true;
        }
      }
    }
    return false;
  }
}

class Trap {
  int x, y, collisionBox;

  Trap(int x, int y) {
    this.x=x;
    this.y=y;
    collisionBox=player.collisions.size();
    if (tryToCreateCollisionsAt(x+(tileSize/24), y+(tileSize/24), tileSize/9, tileSize/9)) {
      harvest();
    }
  }

  public boolean tryToCreateCollisionsAt(int x, int y, int sizex, int sizey) {
    player.collisions.add(x);
    player.collisions.add(y);
    player.collisions.add(sizex);
    player.collisions.add(sizey);
    for (int i = 0; i<player.collisions.size(); i+=4) {
      if (i!=collisionBox && isCollision(x, y, sizex, sizey, player.collisions.get(i), player.collisions.get(i+1), player.collisions.get(i+2), player.collisions.get(i+3))) {
        return true;
      }
    }
    return false;
  }

  public void teken() {
    setTransparentImage(x+camx, y+camy, trapImage);
  }

  public boolean harvest() {
    if (veryCloseToPlayer()) {
      player.inventory.add(new Item(11, true));
      removeTrap();
      return true;
    }
    return false;
  }

  public void removeTrap() {
    removeCollisionBox(collisionBox);
    traps.remove(this);
  }

  public boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+camx, tileSize, player.x+(player.size/2)) && isCloseTo(y+camy, tileSize, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}
class Tree {
  int x, y, collisionBox, createdTrees, state=0, cycleSpeed=1250;

  Tree(int x, int y) {
    this.x=x;
    this.y=y;
    trees.add(getInsertionSpot(y), this);
    collisionBox=player.collisions.size();
    if (tryToCreateCollisionsAt(x+(tileSize/24), y+(tileSize/24), tileSize/9, tileSize/9)) {
      player.inventory.add(new Item(7,true));
      removeCollisionBox(collisionBox);
      trees.remove(this);
    }
  }

  public boolean tryToCreateCollisionsAt(int x, int y, int sizex, int sizey) {
    player.collisions.add(x);
    player.collisions.add(y);
    player.collisions.add(sizex);
    player.collisions.add(sizey);
    for (int i = 0; i<player.collisions.size(); i+=4) {
      if (i!=collisionBox && isCollision(x, y, sizex, sizey, player.collisions.get(i), player.collisions.get(i+1), player.collisions.get(i+2), player.collisions.get(i+3))) {
        return true;
      }
    }
    return false;
  }

  public int getInsertionSpot(int y) {
    for (int i = 0; i<trees.size(); i++) {
      if (trees.get(i).y>y) {
        return i;
      }
    }
    return 0;
  }

  public void teken() {
    if (state==0) {
      setTransparentImage(x+camx, y+camy, treeImages[0]);
    } else if (state>0) {
      setTransparentImage(x+camx, y+camy, treeImages[1]);
    }
    if (state>1) {
      setTransparentImage(x+camx, y+camy-(tileSize/6), treeImages[1]);
    }
    if (state>2) {
      setTransparentImage(x+camx, y+camy-(2*(tileSize/6)), treeImages[1]);
    }
    if (state>3) {
      for (int i = -2; i<3; i++) {
        setTransparentImage(x+camx+(i*(tileSize/6)), y+camy-(3*(tileSize/6)), treeImages[2]);
      }
      for (int i = -2; i<3; i++) {
        setTransparentImage(x+camx+(i*(tileSize/6)), y+camy-(4*(tileSize/6)), treeImages[2]);
      }
      for (int i = -1; i<2; i++) {
        setTransparentImage(x+camx+(i*(tileSize/6)), y+camy-(5*(tileSize/6)), treeImages[2]);
      }
    }
  }

  public void update() {
    if (state!=4 && PApplet.parseInt(random(cycleSpeed))==1) {
      state++;
    } /*else if (state==4 && trees.size()<30 && createdTrees<3 && int(random(cycleSpeed*3))==1) {
     createdTrees++;
     int counter=0;
     while (true) {
     int newx=x+(int(random(tileSize*2))-tileSize);
     int newy=y+(int(random(tileSize*2))-tileSize);
     counter++;
     if (counter==5) {
     createdTrees=3;
     break;
     }
     if (isCloseTo(newx/tileSize, gladeSize-1, numOfColumns/2) && isCloseTo(newy/tileSize, gladeSize-1, numOfColumns/2)) {
     boolean collides = tryToCreateCollisionsAt(newx+(tileSize/24), newy+(tileSize/24), tileSize/9, tileSize/9);
     for (int i = 0; i<4; i++) {
     player.collisions.remove(player.collisions.size()-1);
     }
     if (collides) {
     continue;
     }
     new Tree(newx, newy);
     break;
     } 
     }
     }*/
  }

  public boolean harvest() {
    if (veryCloseToPlayer() && state!=0) {
      for (int i = 0; i<state; i++) {
        player.inventory.add(new Item(0, true));
      }
      if (state==4) {
        player.inventory.add(new Item(7, true));
      }
      removeCollisionBox(collisionBox);
      trees.remove(this);
      return true;
    }
    return false;
  }

  public boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+camx, tileSize/2, player.x+(player.size/2)) && isCloseTo(y+camy, tileSize/2, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}
class Tutorial {
  boolean active = false;
  ArrayList<Integer> tutorialQueue = new ArrayList<Integer>();
  int currentStage;
  boolean[] shown = new boolean[32];

  Tutorial() {
    shown= fillWithNumber(shown, false);
  }

  public void update() {
    createQueue();
    processQueue();
  }

  public void createQueue() {
    if (currentStage==0) {
      tutorialQueue.add(1);
      tutorialQueue.add(2);
      tutorialQueue.add(3);
      tutorialQueue.add(4);
      currentStage++;
    } else if (currentStage==1 && player.inMaze()) {
      tutorialQueue.add(5);
      tutorialQueue.add(6);
      tutorialQueue.add(7);
      currentStage++;
    } else if (currentStage==2 && daytime<800 && player.inMaze() && !shown[11]) {
      tutorialQueue.add(11);
    } else if (currentStage==2 && daytime<=1 && player.inMaze() && !shown[12]) {
      tutorialQueue.add(12);
    } else if (currentStage==2 && night && !player.inMaze() && !shown[13]) {
      tutorialQueue.add(13);
      currentStage++;
    } else if (player.inventoryStrings[4].charAt(0)!='0' && !player.inMaze() && !shown[14]) {
      tutorialQueue.add(14);
      tutorialQueue.add(16);
    } else if (player.inventoryStrings[4].charAt(0)=='0' && night && !player.inMaze() && !shown[31]) {
      tutorialQueue.add(31);
    } else if (player.inventoryStrings[7].charAt(0)!='0' && !player.inMaze() && !shown[17]) {
      tutorialQueue.add(17);
      tutorialQueue.add(18);
    } else if (player.inventoryStrings[1].charAt(0)!='0' && !player.inMaze() && !shown[19]) {
      tutorialQueue.add(19);
      tutorialQueue.add(20);
    } else if (player.inventoryStrings[0].charAt(0)!='0' && !player.inMaze() && !shown[21]) {
      tutorialQueue.add(21);
      tutorialQueue.add(22);
    } else if (player.inventoryStrings[9].charAt(0)!='0' && !player.inMaze() && !shown[26]) {
      tutorialQueue.add(26);
    } else if (player.inventoryStrings[18].charAt(0)!='0' && !player.inMaze() && !shown[30]) {
      tutorialQueue.add(30);
    }
  }

  public void processQueue() {
    if (tutorialQueue.size()!=0) {
      scherm=-1;
      fill(0, 0, 0, 100);
      rectMode(CORNER);
      noStroke();
      textSize(width/30);
      rect(0, 0, width, height);
      rectMode(CENTER);
      textAlign(CENTER, CENTER);
      fill(255);
      text(getTutorialMessage(tutorialQueue.get(0)), width/2, height/2, width/2, height/2);
      shown[tutorialQueue.get(0)]=true;
      tutorialQueue.remove(0);
      for(int i = 1; i<tutorialQueue.size(); i++){
        if(tutorialQueue.get(i)==tutorialQueue.get(0)){
          tutorialQueue.remove(i);
          i--;
        }
      }
      textAlign(CORNER, CORNER);
    }
  }

  public String getTutorialMessage(int i) {
    switch(i) {
    case 1: 
      return "Welcome in the maze. I will help you get started, but won't give you too much clues...";//done
    case 2: 
      return "The main priority now is survival. To survive you will need food.";//done
    case 3: 
      return "Food and other goods are obtainable in the maze. Search for crates and press interact to gather goods from them.";//done
    case 4: 
      return "Now head left, right, up- or downwards to get into the maze";//done
    case 5: 
      return "Welcome in the maze. The maze is a huge place where you'll easily get lost";//done
    case 6: 
      return "Keep track of where you go and make sure you are back well before nighttime";//done
    case 7: 
      return "If you cannot make it back before the end of the day you'll most likely die...";//done
    case 8: 
      return "Congratulations on finding your first crate!";//done
    case 9: 
      return "Congratulations on finding wheat seeds! Wheat is the number one item for survival";//done
    case 10: 
      return "Good thing you got some tree seeds. When you get back in the grass field you'll be able to make a workbench!";//done
    case 11: 
      return "Hurry! Get back to the grass field!";//done
    case 12: 
      return "Seems like you didn't make it in time... Good luck outrunning the bugs!";//done
    case 13: 
      return "It's night, and you are back in the grass field! Nice!";//done
    case 14: 
      return "Now you can start farming. Use the wheat seeds when close to farmland, then interact with it when it's fully grown.";//done
    case 15: 
      return "With cotton you can make shoes. Plant the seeds on your farm at the top right corner of the grass field.";//done
    case 16: 
      return "The farmland can be found at the top right corner of the grass field";//done
    case 17: 
      return "Plant the tree seeds by using them near the middle of the grass field";//done
    case 18: 
      return "Wait for the tree to grow, then use the axe to gather wood!";//done
    case 19: 
      return "You got some wheat! You can craft 5 wheat into bread if you have a workbench. If not, you can feed it to the pigs.";//done
    case 20: 
      return "If you kill a pig by using your axe on them you'll get more porkchops if the pig has eaten a lot of wheat";//done
    case 21: 
      return "Now place the wood by pressing use near the middle of the grass field.";//done
    case 22: 
      return "Now press interact with the axe near the wood block to get a workbench.";//done
    case 23: 
      return "You can now craft! Press interact near the workbench to open the crafting menu!";//done
    case 24: 
      return "You found a lighter. Use it near a placed woodblock to light it on fire!";//done
    case 25: 
      return "Use a porkchop near a litten woodblock to turn it into cooked porkchop!";//done
    case 26: 
      return "You got shoes! Use the shoes to wear them and then press the sprint button to activate sprint mode!";//done
    case 27: 
      return "You found magic dust! Use it to create a map and find out about the exit of the maze!";//done
    case 28: 
      return "To win the game use the map to find out the place the bugs come from.";//done
    case 29: 
      return "But watch out! This place changes every night!";//done
    case 30: 
      return "Use the pickaxe near the mine to gather iron.";//done
    case 31: 
      return "You made your way back, but didn't find any wheat seeds. To survive I would suggest you to kill your pigs by using your axe near them.";//done
    }
    return "Don't die!";
  }
}
class Wood {
  int x, y, collisionBox, currentCycle=0, currentImage=0;
  final int cycleSpeed=10;
  boolean isBurning=false, isCraftingTable=false;

  Wood(int x, int y) {
    this.x=x;
    this.y=y;
    woodBlock.add(this);
    collisionBox=player.collisions.size();
    if (tryToCreateCollisionsAt(x+(tileSize/24), y+(tileSize/24), tileSize/9, tileSize/9)) {
      harvest();
    }
  }

  public boolean tryToCreateCollisionsAt(int x, int y, int sizex, int sizey) {
    player.collisions.add(x);
    player.collisions.add(y);
    player.collisions.add(sizex);
    player.collisions.add(sizey);
    for (int i = 0; i<player.collisions.size(); i+=4) {
      if (i!=collisionBox && isCollision(x, y, sizex, sizey, player.collisions.get(i), player.collisions.get(i+1), player.collisions.get(i+2), player.collisions.get(i+3))) {
        return true;
      }
    }
    return false;
  }

  /*int getInsertionSpot(int y){
   for(int i = 0; i<woodBlock.size(); i++){
   if(woodBlock.get(i).y<y){
   return i;
   }
   }
   return 0;
   }*/

  public void teken() {
    if (!isCraftingTable) {
      setTransparentImage(x+camx, y+camy, treeImages[1]);
    } else {
      setTransparentImage(x+camx, y+camy, craftingTable[0]);
    }
    if (isBurning) {
      setTransparentImage(x+camx, y+camy, fireImages[currentImage]);
      currentCycle++;
      if (currentCycle==cycleSpeed) {
        currentCycle=0;
        currentImage++;
        if (currentImage>=fireImages.length) {
          currentImage=0;
        }
      }
    }
  }

  public boolean turnIntoCraftingTable() {
    if (veryCloseToPlayer()) {
      player.getSelectedItem().removeFromInventory();
      isCraftingTable=true;
      return true;
    }
    return false;
  }

  public void lightOnFire() {
    if (veryCloseToPlayer()) {
      isBurning=true;
      audioPlayer.playSound(fire);
    }
  }

  public boolean cookPork() {
    if (isBurning && veryCloseToPlayer()) {
      player.getSelectedItem().removeFromInventory();
      player.inventory.add(new Item(6, true));
      return true;
    }
    return false;
  }

  public boolean harvest() {
    if (veryCloseToPlayer()) {
      player.inventory.add(new Item(0, true));
      removeCollisionBox(collisionBox);
      if (isCraftingTable) {
        player.inventory.add(new Item(12, true));
      }
      woodBlock.remove(this);
      return true;
    }
    return false;
  }

  public boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+camx, tileSize/2, player.x+(player.size/2)) && isCloseTo(y+camy, tileSize/2, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}
class Crop {
  int x, y, state=0, type;

  Crop(int x, int y) {
    this.x=x;
    this.y=y;
  }

  public void teken() {
    //println((x+farmland.x+camx)+", "+(y+farmland.y+camy));
    setTransparentImage(x+farmland.x+camx, y+farmland.y+camy, farmland.farmLand);
    if (state>0) {
      setTransparentImage(x+farmland.x+camx, y+farmland.y+camy, farmland.cropImage[type][state-1]);
    }
  }
}
class Farmland {
  int x, y;
  Crop[][] crops = new Crop[6][8];
  PImage[][] cropImage = new PImage[2][8];
  PImage farmLand;
  int cropSize = tileSize/6;
  final int spawnAtNode = 574;

  Farmland() {
    x=PApplet.parseInt(gladeNodes.get(spawnAtNode).x);
    y=PApplet.parseInt(gladeNodes.get(spawnAtNode).y);
    farmLand = loadImage("farmland.png");
    farmLand.resize(cropSize, cropSize);
    farmLand.loadPixels();
    for (int i = 0; i<cropImage.length; i++) {
      for (int j = 0; j<cropImage[0].length; j++) {
        cropImage[i][j] = loadImage("wheat"+i+j+".png");
        cropImage[i][j].resize(cropSize, cropSize);
        cropImage[i][j].loadPixels();
      }
    }
    for (int i = 0; i<crops.length; i++) {
      for (int j = 0; j<crops[0].length; j++) {
        crops[i][j]=new Crop(PApplet.parseInt(i*tileSize/3), j*tileSize/6);
      }
    }
  }

  public void createFarmland() {
    for (int i = 0; i<crops.length; i++) {
      for (int j = 0; j<crops[0].length; j++) {
        crops[i][j].teken();
      }
    }
  }

  public void growFarm() {
    for (int i = 0; i<crops.length; i++) {
      for (int j = 0; j<crops[0].length; j++) {
        if (crops[i][j].state!=0 && crops[i][j].state!=8 && PApplet.parseInt(random(100))==1) {
          crops[i][j].state++;
        }
      }
    }
  }

  public void plantAtClosestFarmland(int type) {
  outerloop:
    for (int i = 0; i<crops.length; i++) {
      for (int j = 0; j<crops[0].length; j++) {
        if (veryCloseToPlayer(crops[i][j].x, crops[i][j].y) && crops[i][j].state==0) {
          crops[i][j].type=type;
          crops[i][j].state++;
          player.getSelectedItem().removeFromInventory();
          break outerloop;
        }
      }
    }
  }

  public boolean gatherWheatAtClosestFarmland() {
    for (int i = 0; i<crops.length; i++) {
      for (int j = 0; j<crops[0].length; j++) {
        if (veryCloseToPlayer(crops[i][j].x, crops[i][j].y) && crops[i][j].state==8) {
          crops[i][j].state=0;
          if (crops[i][j].type==0) {
            player.inventory.add(new Item(1, true));
          } else if (crops[i][j].type==1) {
            player.inventory.add(new Item(15, true));
          }
          int random=PApplet.parseInt(random(4));
          for (int k=0; k<random; k++) {
            if (crops[i][j].type==0) {
              player.inventory.add(new Item(4, true));
            } else if (crops[i][j].type==1) {
              player.inventory.add(new Item(8, true));
            }
          }
          return true;
        }
      }
    }
    return false;
  }

  public boolean veryCloseToPlayer(int cropx, int cropy) {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+cropx+camx, tileSize/2, player.x+(player.size/2)) && isCloseTo(y+cropy+camy, tileSize/2, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}
class klep {
  int an;
  boolean hasChest = PApplet.parseInt(random(chestSpawnRate))==1;
  klep(int _a) {
    an=_a;
  }
  public void teken(int x, int y, float _an) { 
    float nx = x+cos(radians(PApplet.parseInt(an+_an)))*(tileSize/2);
    float ny = y+sin(radians(PApplet.parseInt(an+_an)))*(tileSize/2);
    line(x+camx, y+camy, nx+camx, ny+camy);
    if(hasChest){
      setTransparentImage(x+camx+(tileSize/3), y+camy+(tileSize/3), chestImage);
    }
  }
}

class lijn {
  int x, y, sizex, sizey;
  int toBeSizeX, toBeSizeY;
  boolean type;
  boolean isDoor;
  boolean outsideWall;

  lijn (int _x, int _y, int _sizex, int _sizey, boolean _type, boolean _isDoor, boolean _outsideWall) {
    x=_x; 
    y=_y; 
    sizex=_sizex; 
    sizey=_sizey;
    toBeSizeX=sizex;
    toBeSizeY=sizey;
    type=_type;
    isDoor=_isDoor;
    outsideWall=_outsideWall;
  }

  public void teken() {
    line(x+camx, y+camy, x+sizex+camx, y+sizey+camy);
  }

  public void checkUpdate() {
    if (toBeSizeX!=sizex) {
      if (sizex>toBeSizeX) {
        sizex--;
      } else {
        sizex++;
      }
    }
    if (toBeSizeY!=sizey) {
      if (sizey>toBeSizeY) {
        sizey--;
      } else {
        sizey++;
      }
    }
  }
}

class Player {
  int x, y, size;
  float health=100;
  int movingSpeed=tileSize/40;
  int runningSpeed=movingSpeed*2;
  final int wallColor = 0xff525252;
  final int amountOfItems=21;
  ArrayList<Item> inventory = new ArrayList<Item>();
  int currentFood=maxFood;
  int selectedItem=0;
  ArrayList<Integer> collisions = new ArrayList<Integer>();
  int[] animationFrames = new int[3];
  final int animationSpeed = 5;
  boolean canRun=false;
  String[] inventoryStrings = new String[amountOfItems];

  Player(int x, int y, int size) {
    this.x=x;
    this.y=y;
    this.size=size;

    animationFrames[0] = 2;
    animationFrames[1] = 0;
    animationFrames[2] = 0; 
  }

  public void teken() {
    noStroke();
    setTransparentImage(x-(tileSize/12), y-(tileSize/12), playerImages[animationFrames[0]][animationFrames[1]]);
    //println(getLowestValue(x-camx, tileSize/3)+", "+getLowestValue(y-camy,tileSize/3));
  }
  
  public void update(){
    if(player.x-camx>numOfColumns*tileSize || player.y-camy>numOfColumns*tileSize || player.x-camx<tileSize || player.y-camy<tileSize){
      notDrawn=true;
      scherm=3;
    }
  }
  
  public boolean inMaze(){
    if(isCloseTo((x-camx)/tileSize,gladeSize,numOfColumns/2) && isCloseTo((y-camy)/tileSize,gladeSize,numOfColumns/2)){
      return false;
    }
    return true;
  }

  public void checkOutOfScreen() {
    if (x<width/borderSize) {
      camx-=x-(width/borderSize);
      x-=x-(width/borderSize);
    } else if (x>width-(width/borderSize)) {
      camx-=x-(width-(width/borderSize));
      x-=x-(width-(width/borderSize));
    } else if (y<height/borderSize) {
      camy-=y-(height/borderSize);
      y-=y-(height/borderSize);
    } else if (y>height-(height/borderSize)) {
      camy-=y-(height-(height/borderSize));
      y-=y-(height-(height/borderSize));
    }
  }

  public void feed(int amount) {
    currentFood+=amount;
    if (currentFood>maxFood) {
      currentFood=maxFood;
    }
    getSelectedItem().removeFromInventory();
  }

  public int collidesAt(int angle, int distance, int spotSize, int coordX, int coordY) {
    int x = getCoordX(coordX, angle, distance+spotSize);
    int y = getCoordY(coordY, angle, distance+spotSize);
    if (get(x,y)==wallColor) {
      //println("ploff");
      for (int i = 0; true; i++) {
        x = getCoordX(coordX, angle, distance+spotSize-i);
        y = getCoordY(coordY, angle, distance+spotSize-i);
        if (get(x,y)!=wallColor) {
          distance=PApplet.parseInt(getDistance(x, y, coordX, coordY)-spotSize);
          return distance;
        }
      }
    } else if (isCollisionBox(x, y)) {
      return 0;
    }
    return distance;
  }

  public boolean isCollisionBox(int x, int y) {
    x=x-camx;
    y=y-camy;
    for (int i = 0; i<collisions.size (); i+=4) {
      //println(x+", "+collisions.get(i));
      if (x>collisions.get(i) && y>collisions.get(i+1) && x<collisions.get(i)+collisions.get(i+2) && y<collisions.get(i+1)+collisions.get(i+3)) {
        return true;
      }
    }
    return false;
  }
  
  public Item getSelectedItem(){
    for(int i = 0; i<inventory.size(); i++){
      if(inventory.get(i).id==selectedItem){
        return inventory.get(i);
      }
    }
    return null;
  }

  public void moveTo(int moveX, int moveY) {
    int moveToX = x+moveX;
    int moveToY = y+moveY;
    int movementAngle = PApplet.parseInt(getAngle(x, y, moveToX, moveToY));
    int movementDistance = PApplet.parseInt(getDistance(moveToX, moveToY, x, y));
    if (movementAngle==0) {
      animationFrames[0]=3;
    } else if (movementAngle==90) {
      animationFrames[0]=1;
    } else if (movementAngle==180) {
      animationFrames[0]=2;
    } else if (movementAngle==270) {
      animationFrames[0]=0;
    } 
    animationFrames[2]+=(movementDistance/movingSpeed);
    if (animationFrames[2]>=animationSpeed) {
      animationFrames[2]=0;
      animationFrames[1]++;
      if (animationFrames[1]==4) {
        animationFrames[1]=0;
      }
    }
    int[] collisions = {
      collidesAt(movementAngle, movementDistance, size/2, x, y) /* collision directly in front of character */, collidesAt(movementAngle, movementDistance, size/2, getCoordX(x, movementAngle-90, (size/2)-1), getCoordY(y, movementAngle-90, (size/2)-1)), collidesAt(movementAngle, movementDistance, size/2, getCoordX(x, movementAngle+90, (size/2)-1), getCoordY(y, movementAngle+90, (size/2)-1))
      };
      //pixels[(getCoordY(moveToY, movementAngle-90, size/2)*height)+getCoordX(moveToX, movementAngle-90, size/2)]=#FF0000;
      //pixels[(getCoordY(moveToY, movementAngle+90, size/2)*height)+getCoordX(moveToX, movementAngle+90, size/2)]=#FF0000;
    setDirectionSpeed(movementAngle, getLowest(collisions));
  }

  public int getLowest(int[] array) {
    int lowest = 9999999;
    for (int i  = 0; i<array.length; i++) {
      //println(array[i]);
      if (array[i]<lowest) {
        lowest=array[i];
      }
    }
    return lowest;
  }

  public void setDirectionSpeed(double direction, double speed) {
    x = x+PApplet.parseInt((float)(speed*sin((float)(Math.toRadians(direction)))));
    y = y+PApplet.parseInt((float)(speed*-cos((float)(Math.toRadians(direction)))));
  }

  public float getAngle(int x1, int y1, int x2, int y2) {
    int deltaX=x2-x1;
    int deltaY=y2-y1;
    if (deltaX>0 && deltaY>0) {
      return invertNumber((float)Math.toDegrees(atan2(x2-x1, y2-y1)), -45)+180+45;
    } else if (deltaX>0 && deltaY<0) {
      return invertNumber((float)Math.toDegrees(atan2(x2-x1, y2-y1))-90, 45)+45;
    }
    return invertNumber((float)Math.toDegrees(atan2(x2-x1, y2-y1)), 90)+90;
  }

  public float invertNumber(float number, float median) {
    return number+((median-number*2));
  }

  public float getDistance(int x1, int y1, int x2, int y2) {
    return sqrt(sq(x2-x1)+sq(y2-y1));
  }

  public int getCoordX(int x, double direction, double distance) {
    return x+PApplet.parseInt((float)(distance*sin((float)(Math.toRadians(direction)))));
  }

  public int getCoordY(int y, double direction, double distance) {
    return y+PApplet.parseInt((float)(distance*-cos((float)(Math.toRadians(direction)))));
  }

  public void checkCollisionAndMove(int degrees) {
    //if(getCoordX(0, degrees-180, size/2)+(getCoordY(0, degrees-180, size/2)*height)>0 && getCoordX(0, degrees-180, size/2)+(getCoordY(0, degrees-180, size/2)*height)<width*height){
    //println((getCoordX(x, degrees-180, size/2)+(getCoordY(y, degrees-180, size/2)*height)));
    if (get(getCoordX(x, degrees-180, size/2+2),getCoordY(y, degrees-180, size/2+2))==wallColor) {
      //println("it's actually the color of the wall!");
      setDirectionSpeed(degrees, movingSpeed/2);
    }//}
  }

  public boolean stuckInWall() {
    int collisions=0;
    int checkDegrees=0;
    for (int i = 0; i<4; i++) {
      if (get(getCoordX(x, checkDegrees, size/2+2),getCoordY(y, checkDegrees, size/2+2))==wallColor) {
        collisions++;
      }
      checkDegrees+=90;
    }
    return collisions>2;
  }

  public boolean closeTo(int x, int y, int howClose) {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x, howClose, player.x+(player.size/2)) && isCloseTo(y, howClose, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
  
  public void outlinedText(String text, int x, int y) {
    fill(0, 0, 0);
    text(text, x-1, y); 
    text(text, x, y-1); 
    text(text, x+1, y); 
    text(text, x, y+1); 
    fill(255, 255, 255);
    text(text, x, y);
  }
}

class Poort {
  int x, y;
  //float moving =0;
  //int lastmove = millis()+int(random(10000));
  int an;
  int toBeAn;
  //float[] moves = new float[0];
  klep[] kleppen = new klep[0];
  Poort  (int _x, int _y) {
    an =  PApplet.parseInt(random(4))*90; 
    toBeAn=an;
    x=_x; 
    y=_y; 
    int n=0;
    for (int a=0; a<4; a++) {
      if ((n<3 && random(1.8f)<1) || ( n==0 && a==3 )) {
        n++; 
        kleppen = (klep[]) append(kleppen, new klep((a*90)));
      }
    }
    //poorten = (Poort[]) append(poorten, this);
  }

  public void checkUpdate() {
    if (toBeAn!=an) {
      if (an>toBeAn) {
        an--;
        if (veryCloseToPlayer()) {
          for (int i = 0; i<kleppen.length; i++) {
            player.checkCollisionAndMove(kleppen[i].an+an);
          }
          //println(this+"is very close");
        }
      } else {
        an++;
        if (veryCloseToPlayer()) {
          for (int i = 0; i<kleppen.length; i++) {
            player.checkCollisionAndMove(kleppen[i].an+an+180);
          }
          //println(this+"is very close");
        }
      }
      /*if(veryCloseToPlayer()){
       for(int i = 0; i<kleppen.length; i++){
       player.checkCollisionAndMove(kleppen[i].an+an);
       }
       //println(this+"is very close");
       }*/
    }
  }

  public boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+camx, tileSize/2, player.x+(player.size/2)) && isCloseTo(y+camy, tileSize/2, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }

  public void teken(/*int m*/) {
    for (int a=0; a<kleppen.length; a++) { 
      kleppen[a].teken(x, y, an);
    }
    /*if (moves.length!=0) {
      an = int(moves[0]);
      moves = subsetTransparentImagemoves, 1);
    } else { 
      if (m>lastmove) {
        lastmove = m+10000;
        moves = getMoves(an);
      }
    }*/
  }
}


}
