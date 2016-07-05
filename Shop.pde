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

  void teken() {
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

  void drawOnce() {
    setTransparentImage((width/2)-(2*(height/4)), height/2, craftingTable[3]);
    setTransparentImage((width/2)+(height/4), height/2, craftingTable[2]);
  }

  void outlinedText(String text, int x, int y) {
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

  boolean purchase(boolean actually) {
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

  boolean purchaseMulti(boolean actually) {
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

