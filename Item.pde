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

  String getItemName(int id) {
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

  void use() {
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
        int x=int(random(poorten.length)), y=int(random(poorten[0].length));
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

  String changeNumber(String number, boolean higher) {
    int i;
    for (i = 0; number.charAt(i)>='0' && number.charAt(i)<='9'; i++) {
    }
    int num = int(number.substring(0, i));
    if (higher) {
      return str(num+1)+number.substring(i);
    } 
    return str(num-1)+number.substring(i);
  }

  int getNumber(String number) {
    int i;
    for (i = 0; number.charAt(i)>='0' && number.charAt(i)<='9'; i++) {
    }
    return int(number.substring(0, i));
  }

  void removeFromInventory() {
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
