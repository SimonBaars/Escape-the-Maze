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

  /*int getInsertionSpot(int y){
   for(int i = 0; i<woodBlock.size(); i++){
   if(woodBlock.get(i).y<y){
   return i;
   }
   }
   return 0;
   }*/

  void teken() {
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

  boolean turnIntoCraftingTable() {
    if (veryCloseToPlayer()) {
      player.getSelectedItem().removeFromInventory();
      isCraftingTable=true;
      return true;
    }
    return false;
  }

  void lightOnFire() {
    if (veryCloseToPlayer()) {
      isBurning=true;
      audioPlayer.playSound(fire);
    }
  }

  boolean cookPork() {
    if (isBurning && veryCloseToPlayer()) {
      player.getSelectedItem().removeFromInventory();
      player.inventory.add(new Item(6, true));
      return true;
    }
    return false;
  }

  boolean harvest() {
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

  boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+camx, tileSize/2, player.x+(player.size/2)) && isCloseTo(y+camy, tileSize/2, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}
