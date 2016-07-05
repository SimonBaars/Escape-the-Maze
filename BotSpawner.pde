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
  void spawnBots() {
    stopCalculating=setFalse(stopCalculating);
    terminate=false;
    int attempt=0;
    for (int i = 0; i<amountOfBots; i++) {
      animationFrame[i]=int(random(botImage.length));
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

  boolean[] setFalse(boolean[] array) {
    for (int i = 0; i<array.length; i++) {
      array[i]=false;
    }
    return array;
  }

  void calculateClosestPathToPlayer(int botNumber) {
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

  void botsBackToSpawn() {
    terminate=true;
    for (int i = 0; i<amountOfBots; i++) {
      calculatedSpots[i]=2;
      paths[i] = bfs.bfs(bots[i], spawnNode[i]);
    }
  }

  void drawBot() {
    for (int i  = 0; i<amountOfBots; i++) {
      if (bots[i]!=null) {
        animateTo(int(bots[i].x+camx+(2*(tileSize/3))-(tileSize/6)), int(bots[i].y+camy+(2*(tileSize/3))-(tileSize/6)), movementProgress[i], movementSteps[i], i, bots[i]);
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

  void animateTo(int x, int y, int frame, int numOfFrames, int botNumber, Node bot) {
    if (paths[botNumber].size()-calculatedSpots[botNumber]>=0) {
      int newx = int(((Node)paths[botNumber].get(paths[botNumber].size()-calculatedSpots[botNumber])).x+camx+(2*(tileSize/3)));
      int newy= int(((Node)paths[botNumber].get(paths[botNumber].size()-calculatedSpots[botNumber])).y+camy+(2*(tileSize/3)));
      //println("frame="+frame);
      //println((float(frame)/float(numOfFrames)));
      if (x>newx &&y>newy-(tileSize/6)) {
        setTransparentImage(int(x-((float(frame)/float(numOfFrames))*(tileSize/3))), int(y-((float(frame)/float(numOfFrames))*float(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (x>newx && y<newy-(tileSize/6)) {
        setTransparentImage(int(x-((float(frame)/float(numOfFrames))*(tileSize/3))), int(y+((float(frame)/float(numOfFrames))*float(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (x<newx-(tileSize/6) && y>newy-(tileSize/6)) {
        setTransparentImage(int(x+((float(frame)/float(numOfFrames))*float(tileSize/3))), int(y-((float(frame)/float(numOfFrames))*float(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (x<newx-(tileSize/6) &&  y<newy-(tileSize/6)) {
        setTransparentImage(int(x+((float(frame)/float(numOfFrames))*float(tileSize/3))), int(y+((float(frame)/float(numOfFrames))*float(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (x>newx) {
        setTransparentImage(int(x-((float(frame)/float(numOfFrames))*(tileSize/3))), y, botImage[animationFrame[botNumber]]);
      } else if (x<newx-tileSize/6) {
        setTransparentImage(int(x+((float(frame)/float(numOfFrames))*float(tileSize/3))), y, botImage[animationFrame[botNumber]]);
      } else if (y>newy-(tileSize/6)) {
        setTransparentImage(x, int(y-((float(frame)/float(numOfFrames))*float(tileSize/3))), botImage[animationFrame[botNumber]]);
      } else if (y<newy-(tileSize/6)) {
        setTransparentImage(x, int(y+((float(frame)/float(numOfFrames))*float(tileSize/3))), botImage[animationFrame[botNumber]]);
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

  void checkCloseToPlayer() {
    for (int i  = 0; i<amountOfBots; i++) {
      if (bots[i]!=null) {
        //println((int(bots[i].x)+camx+(3*(tileSize/3))-(tileSize/6))+", "+(player.x+(player.size/2)));
        if (isCloseTo(int(bots[i].x)+camx+(3*(tileSize/3))-(tileSize/6), tileSize/2, player.x+(player.size/2)) && isCloseTo(int(bots[i].y)+camy+(3*(tileSize/3))-(tileSize/6), tileSize/2, player.y+(player.size/2)) && player.inMaze()) {
          player.health-=grieverDamage;
        }
      }
    }
  }

  void checkCloseToTrap() {
    for (int i  = 0; i<amountOfBots; i++) {
      if (bots[i]!=null) {
        for (int j = 0; j<traps.size(); j++) {
          //println((int(bots[i].x)+camx+(2*(tileSize/3))-(tileSize/6))+", "+(traps.get(j).x+(tileSize/12)+camx));
          if (isCloseTo(int(bots[i].x)+camx+(3*(tileSize/3))-(tileSize/6), tileSize/2, traps.get(j).x+(tileSize/12)+camx) && isCloseTo(int(bots[i].y)+camy+(3*(tileSize/3))-(tileSize/6), tileSize/2, traps.get(j).y+(tileSize/12)+camy)) {
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
