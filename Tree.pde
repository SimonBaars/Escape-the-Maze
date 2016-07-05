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

  boolean tryToCreateCollisionsAt(int x, int y, int sizex, int sizey) {
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

  int getInsertionSpot(int y) {
    for (int i = 0; i<trees.size(); i++) {
      if (trees.get(i).y>y) {
        return i;
      }
    }
    return 0;
  }

  void teken() {
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

  void update() {
    if (state!=4 && int(random(cycleSpeed))==1) {
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

  boolean harvest() {
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

  boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+camx, tileSize/2, player.x+(player.size/2)) && isCloseTo(y+camy, tileSize/2, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}
