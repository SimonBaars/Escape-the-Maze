import apwidgets.*;

import ai.pathfinder.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.lang.InterruptedException;

int dayLength=30*200;//30*10 is default
int amountOfBots=5;
final String path = "//sdcard//Games//Escape the Maze//";
final boolean androidMode=true;
int numOfColumns = 50;
int tileSize;//normaal = 180
int gladeSize= 4;
int camx=-300, camy=-300; //int camx=-numOfColumns*tileSize/2, camy=-numOfColumns*tileSize/2;
byte[] floatSpeed = {
  byte(random(4)+1), byte(random(3)+1)
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

void setup() {
  frameRate(30);
  grass= new APMediaPlayer(this);
  nightMusic= new APMediaPlayer(this);
  maze= new APMediaPlayer(this);
  intro= new APMediaPlayer(this);
  click= new APMediaPlayer(this);
  fire= new APMediaPlayer(this);
  audioPlayer = new Audio();
}

void cleanupGame() {
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

void setupGame() {
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
    wallTypes[i] = byte(random(wallImages.length));
  }
  initCollisions();
  for (int i = 0; i<amountOfBots; i++) {
    bots[i]=null;
  }
  botSpawner.calculatedSpots=fillWithNumber(botSpawner.calculatedSpots, 2);
  setNight(false);
  rectMode(CENTER);

  pigs.add(new Pig(int(gladeNodes.get(300).x), int(gladeNodes.get(290).y)));
  pigs.add(new Pig(int(gladeNodes.get(301).x), int(gladeNodes.get(291).y)));
  mine = new Mine();
  farmland=new Farmland();
  textSize(width/30);
}

void initImages() {
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
        pigImage[i][j].resize(int(tileSize/3), tileSize/6);
      } else {
        pigImage[i][j].resize(tileSize/6, int(tileSize/3));
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
  mineImage.resize(tileSize, int(tileSize/1.5));
  if (androidMode) {
    mineImage.loadPixels();
  }
}

boolean isDivisible(int a, int b) {
  return a%b == 0;
}

int[] fillWithNumber(int[] array, int getal) {
  for (int i = 0; i<array.length; i++) {
    array[i]=getal;
  }
  return array;
}

byte[] fillWithNumber(byte[] array, byte getal) {
  for (int i = 0; i<array.length; i++) {
    array[i]=getal;
  }
  return array;
}

boolean[] fillWithNumber(boolean[] array, boolean getal) {    
  for (int i = 0; i<array.length; i++) {    
    array[i]=getal;
  }    
  return array;
}  

void setTransparentImage(int x, int y, PImage image) {
  if (androidMode) {
    set(x, y, image);
  } else {    
    image(image, x, y);
  }
}

void initMap() { //part of setup
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

void fillNodeRegister() { //part of setup
  int k = 0;
  for (int i = 0; i<nodeRegister.length; i++) {
    for (int j = 0; j<nodeRegister[0].length; j++) {
      nodeRegister[i][j]=(Node)bfs.nodes.get(k);
      k++;
    }
  }
}

void initCollisions() { //part of setup
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

Poort getPoort(int x) {
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

void checkContainingKleppen(Node[][] nodeMap, Poort poort, int a, int b) {
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

int getDegrees(int degrees) {
  while (degrees>=360) {
    degrees-=360;
  }
  return degrees;
}

Node[][] getNodeMap(int node) {
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

void drawNodes(int trackX, int trackY) {
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
          set(int(temp.x+camx+(1.5*(tileSize/3))), int(temp.y+camy+(1.5*(tileSize/3))), grassImage);
          //}
        } else {
          set(int(temp.x+camx+(1.5*(tileSize/3))), int(temp.y+camy+(1.5*(tileSize/3))), wallImages[wallTypes[(i*nodeRegister.length)+j]]);
        }
      }
    }
  }
}

void setNight(boolean newValue) {
  daytime=dayLength;
  night=newValue;
  for (int i = 0; i<lijnen.length; i++) {
    if (lijnen[i].isDoor) {
      openOrCloseDoor(newValue, lijnen[i]);
    }
  }
}

boolean isCloseTo(int number, int howCloseTo, int anotherNumber) {
  if (anotherNumber<=number+howCloseTo && anotherNumber>=number-howCloseTo) {
    return true;
  }
  return false;
}

void draw() {
  if (isLoading!=0) {
    while (loadThread.isAlive ()) {
    }
    isLoading=0;
    loadThread = new LoadThread();
  }
  if (scherm==1) {
    background(122, 122, 122);
    audioPlayer.update();
    playerNodes[0] = int((((player.x-camx)-(float(tileSize)/2))/float(tileSize))/*-(float(tileSize)/2)*/);
    playerNodes[1] = int((((player.y-camy)-(float(tileSize)/2))/float(tileSize))/*-(float(tileSize)/2)*/);
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
      player.health-=0.1;
    }
    if (player.health<=0) {
      scherm=2;
    }
    //println((float(player.currentFood)/float(player.maxFood)*100));
    if (float(player.currentFood)/float(maxFood)*100>90 && player.health<=99.95) {
      player.health+=0.05;
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
      drawCounter(0, "Time till day", daytime, dayLength, #00368B);
    } else {
      drawCounter(0, "Time till night", daytime, dayLength, #FCF503);
    }
    drawCounter(1, "Food", player.currentFood, maxFood, #54FF00);
    drawCounter(2, "Health", player.health, 100, #54FF00);
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
      float tempTileSize= height/(1.0714*numOfColumns);
      strokeWeight(int(height/400));
      for (int a=0; a<poorten.length; a++) {
        for (int b=0; b<poorten[0].length; b++) {
          if (poorten[a][b]!=null) {
            for (int i = 0; i<poorten[a][b].kleppen.length; i++) {
              float x = (width/6)+a*tempTileSize;
              float y = (height/50)+b*tempTileSize;
              float nx = x+cos(radians(int(poorten[a][b].kleppen[i].an+poorten[a][b].an)))*(tempTileSize/2);
              float ny = y+sin(radians(int(poorten[a][b].kleppen[i].an+poorten[a][b].an)))*(tempTileSize/2);
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
            float x = (width/6)+(((bots[i].x+(tileSize/3))/float(tileSize))*tempTileSize)-(tempTileSize/2);
            float y = (height/50)+(((bots[i].y+(tileSize/3))/float(tileSize))*tempTileSize)-(tempTileSize/2);
            ellipse(x, y, height/150, height/150);
          }
        }
        fill(0, 0, 255);
        float x = (width/6)+((float(player.x-camx)/float(tileSize))*tempTileSize)-(tempTileSize/2);
        float y = (height/50)+((float(player.y-camy)/float(tileSize))*tempTileSize)-(tempTileSize/2);
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
    setTransparentImage((width/2)-(height/4), int(height/1.2), buttonImage[0]);
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

void showLoadingScreen() {
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
  text("(harder map sizes will take longer to load)", width/2, height/1.5);
  textAlign(CORNER);
  loadThread.start();
}

void outlinedText(String text, int x, int y, int sizex, int sizey) {
  fill(0, 0, 0);
  text(text, x-1, y, sizex, sizey); 
  text(text, x, y-1, sizex, sizey); 
  text(text, x+1, y, sizex, sizey); 
  text(text, x, y+1, sizex, sizey); 
  fill(255, 255, 255);
  text(text, x, y, sizex, sizey);
}

void backgroundAnimation() {
  drawNodes(int(-(float(camx)/float(tileSize))), int(-(float(camy-(height/2))/float(tileSize))));
  strokeWeight(tileSize/6);
  stroke(player.wallColor); 
  for (int i = 0; i<traps.size (); i++) {
    traps.get(i).teken();
  }
  for (int a=0; a<poorten.length; a++) {
    for (int b=0; b<poorten[0].length; b++) {
      //println(int((((player.x-camx)-(float(tileSize)/2))/float(tileSize))/*-(float(tileSize)/2)*/));
      if (poorten[a][b]!=null) {
        if ( isCloseTo(a, vision, int(-(float(camx)/float(tileSize)))) && isCloseTo(b, vision, int(-(float(camy-(height/2))/float(tileSize))))) {
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
  if (int(random(1000))==1) {
    moveAllWalls();
  }
  if (numOfColumns<int(-(float(camx)/float(tileSize)))+10 || numOfColumns<int(-(float(camy)/float(tileSize)))+10) {
    camx=-300; 
    camy=-300;
    floatSpeed[0]=byte(random(4)+1);
    floatSpeed[1]=byte(random(3)+1);
  }
  camx-=floatSpeed[0];
  camy-=floatSpeed[1];
}

void mousePressed() {
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
    if (checkMousePosition((width/2)-(height/4), int(height/1.2), height/2, height/8)) {
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

void mousePressedAndroid() {
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

void doGameSetup() {
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
      botSpawner.movementSteps[i]=9+int(random(3));
    }
  } else if (gameSetup[1]==1) {
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      botSpawner.movementSteps[i]=6+int(random(3));
    }
  } else if (gameSetup[1]==2) {
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      botSpawner.movementSteps[i]=4+int(random(3));
    }
  } else if (gameSetup[1]==3) {
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      botSpawner.movementSteps[i]=2+int(random(3));
    }
  } 
  dayLength=30*200;
  if (gameSetup[2]==0) {
    maxFood=dayLength*5;
  } else if (gameSetup[2]==1) {
    maxFood=dayLength*2;
  } else if (gameSetup[2]==2) {
    maxFood=int(dayLength*1.5);
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
    grieverDamage=0.25;
  } else if (gameSetup[5]==1) {
    grieverDamage=1;
  } else if (gameSetup[5]==2) {
    grieverDamage=5;
  } else if (gameSetup[5]==3) {
    grieverDamage=100;
  }
}

boolean checkMousePosition(int x, int y, int sizex, int sizey) {
  if (mouseX>x && mouseY>y && mouseX<x+sizex && mouseY<y+sizey) {
    return true;
  }
  return false;
}

int booleanToInt(boolean x) {
  return (x) ? 1 : 0;
}

void drawCounter(int num, String name, float value, float maxValue, int counterColor) {
  strokeWeight(1);
  stroke(0);
  fill(0, 0, 0);
  textSize(height/25);
  rect(width/1.2, height/15+(num*(height/15)), width/4, height/40);
  text(name, width/1.4, (height/15)-20+(num*(height/15)));
  fill(counterColor);
  rect((width/1.2)+((value/maxValue)*(width/8))-(width/8), height/15+(num*(height/15)), ((width/4)/maxValue)*value, height/40);
}

void keyPressed() {
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

int getRandomLootFromChest() {
  if (tutorial!=null) {
    if (tutorial.shown[8]==false) {
      tutorial.tutorialQueue.add(8);
    }
  }
  int random=int(random(100));
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

void moveAllWalls() {
  for (int a=0; a<poorten.length; a++) {
    for (int b=0; b<poorten[0].length; b++) {
      if (poorten[a][b]!=null) {
        poorten[a][b].toBeAn=int(random(4))*90;
      }
    }
  }
  int randomLine=int(random(lijnen.length));
  while (!lijnen[randomLine].outsideWall) {
    randomLine=int(random(lijnen.length));
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

int getNodeAtPosition(int x, int y) {
  int j=0;
  for (int i = 0; i<bfs.nodes.size (); i++) {
    if (isCloseTo(int(((Node)bfs.nodes.get(i)).x), tileSize/3, x-(tileSize-(tileSize/6))) && isCloseTo(int(((Node)bfs.nodes.get(i)).y), tileSize/3, y-(tileSize-(tileSize/6)))) {
      j=i;
    }
  }
  return j;
}

Node getNodeAt(int y, int x) {
  //println(x+", "+y);
  int newx=int((float(x-tileSize)/(float(tileSize)))*3)+1;
  int newy=int((float(y-tileSize)/(float(tileSize)))*3)+1;
  return nodeRegister[newx][newy];
}

int getLowestValue(int i, int j) {
  while (true) {
    if (i>j && j>0) {
      i=i-j;
    } else {
      break;
    }
  }
  return i;
}

void openOrCloseDoor(boolean open, lijn _lijn) {
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

int getClosestDivisibleBy(int number, int divisibleBy) {
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

void removeCollisionBox(int collisionBox) {
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

boolean isCollision(int ax, int ay, int aWidth, int aHeight, int bx, int by, int bWidth, int bHeight) {
  if ((ax > bx + bWidth - 1) || // Is a on the right side of b?
  (ay > by + bHeight - 1) || // Is a under b?
  (bx > ax + aWidth - 1) || // Is b on the right side of a?
  (by > ay + aHeight - 1)) {   // Is b under a?
    return false; // No collision
  }
  return true; // Collision
}

int getSelectedItemIncrease() {
  for (int i = player.selectedItem+1; i<player.inventoryStrings.length; i++) {
    if (player.inventoryStrings[i].charAt(0)!='0') {
      return i;
    }
  }
  return player.selectedItem;
}

int getSelectedItemDecrease() {
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
      gameSetup=fillWithNumber(gameSetup, byte(1));
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
