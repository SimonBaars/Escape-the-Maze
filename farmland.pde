class Farmland {
  int x, y;
  Crop[][] crops = new Crop[6][8];
  PImage[][] cropImage = new PImage[2][8];
  PImage farmLand;
  int cropSize = tileSize/6;
  final int spawnAtNode = 574;

  Farmland() {
    x=int(gladeNodes.get(spawnAtNode).x);
    y=int(gladeNodes.get(spawnAtNode).y);
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
        crops[i][j]=new Crop(int(i*tileSize/3), j*tileSize/6);
      }
    }
  }

  void createFarmland() {
    for (int i = 0; i<crops.length; i++) {
      for (int j = 0; j<crops[0].length; j++) {
        crops[i][j].teken();
      }
    }
  }

  void growFarm() {
    for (int i = 0; i<crops.length; i++) {
      for (int j = 0; j<crops[0].length; j++) {
        if (crops[i][j].state!=0 && crops[i][j].state!=8 && int(random(100))==1) {
          crops[i][j].state++;
        }
      }
    }
  }

  void plantAtClosestFarmland(int type) {
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

  boolean gatherWheatAtClosestFarmland() {
    for (int i = 0; i<crops.length; i++) {
      for (int j = 0; j<crops[0].length; j++) {
        if (veryCloseToPlayer(crops[i][j].x, crops[i][j].y) && crops[i][j].state==8) {
          crops[i][j].state=0;
          if (crops[i][j].type==0) {
            player.inventory.add(new Item(1, true));
          } else if (crops[i][j].type==1) {
            player.inventory.add(new Item(15, true));
          }
          int random=int(random(4));
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

  boolean veryCloseToPlayer(int cropx, int cropy) {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+cropx+camx, tileSize/2, player.x+(player.size/2)) && isCloseTo(y+cropy+camy, tileSize/2, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
}