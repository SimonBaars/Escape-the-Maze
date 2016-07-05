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
    animationFrames[1]=int(random(4));
    //collisionBox=player.collisions.size();
    //player.collisions.add(x);player.collisions.add(y);player.collisions.add(int(tileSize/1.5));player.collisions.add(tileSize/3);
    //getNodeAt(x-(tileSize/3),y-(tileSize/3)).walkable=false;
  }

  void teken() {
    setTransparentImage(int(x)+camx, int(y)+camy, pigImage[animationFrames[0]][animationFrames[1]]);
  }

  void wander() {
    if (walkingProgress==0) {
      while (true) {
        int random = int(random(4));
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
      if (!isCollisionBox(x-((tileSize/3)/speed)+camx, y+camy) && (isCloseTo(int((x-((tileSize/3)/speed))/tileSize), gladeSize-1, numOfColumns/2) && isCloseTo(int(y/tileSize), gladeSize-1, numOfColumns/2))) {
        x-=(tileSize/3)/speed;
      } else {
        walkingProgress=0;
        doFurtherParsing=false;
      }
    } else if (animationFrames[0]==1) {
      if (!isCollisionBox(x+((tileSize/3)/speed)+camx, y+camy) && (isCloseTo(int((x+((tileSize/3)/speed))/tileSize), gladeSize-1, numOfColumns/2) && isCloseTo(int(y/tileSize), gladeSize-1, numOfColumns/2))) {
        x+=(tileSize/3)/speed;
      } else {
        walkingProgress=0;
        doFurtherParsing=false;
      }
    } else if (animationFrames[0]==2) {
      if (!isCollisionBox(x+camx, y+((tileSize/3)/speed)+camy) && (isCloseTo(int(x/tileSize), gladeSize-1, numOfColumns/2) && isCloseTo(int((y+((tileSize/3)/speed))/tileSize), gladeSize-1, numOfColumns/2))) {
        y+=(tileSize/3)/speed;
      } else {
        walkingProgress=0;
        doFurtherParsing=false;
      }
    } else if (animationFrames[0]==3) {
      if (!isCollisionBox(x+camx, y-((tileSize/3)/speed)+camy) && (isCloseTo(int(x/tileSize), gladeSize-1, numOfColumns/2) && isCloseTo(int((y-((tileSize/3)/speed))/tileSize), gladeSize-1, numOfColumns/2))) {
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

  boolean isCollisionBox(float x, float y) {
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

  void kill() {
    if (veryCloseToPlayer()) {
      int j=int(wheatEaten*random(0.5));
      for (int i = 0; i<1+j; i++) {
        player.inventory.add(new Item(5, true));
      }
      //removeCollisionBox(collisionBox);
      pigs.remove(this);
    }
  }

  boolean feed() {
    if (veryCloseToPlayer()) {
      player.getSelectedItem().removeFromInventory();
      wheatEaten++;
      return true;
    }
    return false;
  }

  boolean isCloserToPlayer(int x, int y, int x2, int y2) {//if the distance of x and y is smaller to the player than x2 and y2 it will return true
    if (player.getDistance(x, y, player.x-camx, player.y-camy)<player.getDistance(x2, y2, player.x-camx, player.y-camy)) {
      return true;
    }
    return false;
  }

  boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(int(x)+camx, tileSize, player.x+(player.size/2)) && isCloseTo(int(y)+camy, tileSize, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}