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

  void teken() {
    setTransparentImage(x+camx, y+camy, trapImage);
  }

  boolean harvest() {
    if (veryCloseToPlayer()) {
      player.inventory.add(new Item(11, true));
      removeTrap();
      return true;
    }
    return false;
  }

  void removeTrap() {
    removeCollisionBox(collisionBox);
    traps.remove(this);
  }

  boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+camx, tileSize, player.x+(player.size/2)) && isCloseTo(y+camy, tileSize, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}