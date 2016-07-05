class Mine {
  final int spawnAtNode = 200;
  int x, y;
  Mine() {
    x=int(gladeNodes.get(spawnAtNode).x);
    y=int(gladeNodes.get(spawnAtNode).y);
    player.collisions.add(x+(tileSize/24));
    player.collisions.add(y+(tileSize/24));
    player.collisions.add(int(tileSize/1.1));
    player.collisions.add(int(tileSize/1.6));
  }

  void teken() {
    setTransparentImage(x+camx, y+camy, mineImage);
  }

  void interact() {
    if (veryCloseToPlayer()) {
      player.health-=random(30);
      player.currentFood-=int(random(3000));
      int random = int(random(3));
      for (int i = 0; i<random; i++) {
        player.inventory.add(new Item(17, true));
      }
    }
  }

  boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(int(x)+camx+(tileSize/2), tileSize, player.x+(player.size/2)) && isCloseTo(int(y)+camy+int(tileSize/1.5), tileSize, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}