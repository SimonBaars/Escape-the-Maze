class SaveLoad {
  String prefix;
  String timeStamp;
  SaveLoad() {
    timeStamp= day()+"-"+month()+"-"+year()+" at "+hour()+"h"+minute()+"m"+second()+"s";
  }

  void saveGame() {
    String[] array = new String[0];
    for (int i = 0; i<gameSetup.length; i++) {
      array = append(array, str(gameSetup[i]));
    }
    array = append(array, str(amountOfBots));
    array = append(array, str(tileSize));
    array = append(array, str(gladeSize));
    array = append(array, str(camx));
    array = append(array, str(camy));
    array = append(array, str(borderSize));
    array = append(array, str(night));
    array = append(array, str(targetNode));
    array = append(array, str(daytime));
    array = append(array, str(vision));
    array = append(array, str(day));
    array = append(array, str(canMove));
    array = append(array, str(notDrawn));
    array = append(array, str(playerNodes[0]));
    array = append(array, str(playerNodes[1]));
    array = append(array, playerName);
    saveStrings(path+"gameSettings.txt", array);
    /*dayLength;
     amountOfBots;
     numOfColumns;
     tileSize;
     gladeSize;
     camx;
     camy; 
     borderSize=3;
     night);
     targetNode=0;
     daytime;
     vision;
     scherm;
     day;
     canMove);
     notDrawn);
     playerNodes[0];
     playerNodes[1];
     chestSpawnRate;
     grieverDamage;
     maxFood;*/

    /*
DOES NOT WORK LIKE THIS!
     
     array = new String[0];
     for(int i = 0; i<gladeNodes.size(); i++){
     array = append(array,str(gladeNodes.get(i).x));
     array = append(array,str(gladeNodes.get(i).y));
     }
     saveStrings("gladeNodes.txt",array);
     */

    array = new String[0];
    for (int i = 0; i<trees.size (); i++) {
      array = append(array, str(trees.get(i).x));
      array = append(array, str(trees.get(i).y));
      array = append(array, str(trees.get(i).createdTrees));
      array = append(array, str(trees.get(i).state));
    }
    saveStrings(path+"trees.txt", array);

    array = new String[0];
    for (int i = 0; i<woodBlock.size (); i++) {
      array = append(array, str(woodBlock.get(i).x));
      array = append(array, str(woodBlock.get(i).y));
      array = append(array, str(woodBlock.get(i).currentCycle));
      array = append(array, str(woodBlock.get(i).currentImage));
      array = append(array, str(woodBlock.get(i).isBurning));
      array = append(array, str(woodBlock.get(i).isCraftingTable));
    }
    saveStrings(path+"woodBlock.txt", array);

    array = new String[0];
    for (int i = 0; i<pigs.size (); i++) {
      array = append(array, str(pigs.get(i).x));
      array = append(array, str(pigs.get(i).y));
      array = append(array, str(pigs.get(i).animationFrames[0]));
      array = append(array, str(pigs.get(i).animationFrames[1]));
      array = append(array, str(pigs.get(i).wheatEaten));
      array = append(array, str(pigs.get(i).walkingProgress));
      array = append(array, str(pigs.get(i).followPlayer));
    }
    saveStrings(path+"pigs.txt", array);

    array = new String[0];
    for (int i = 0; i<traps.size (); i++) {
      array = append(array, str(traps.get(i).x));
      array = append(array, str(traps.get(i).y));
    }
    saveStrings(path+"traps.txt", array);

    long milliCount=millis()+7000;
    array = new String[0];
  outerloop:
    for (int i = 0; i<poorten.length; i++) {
      for (int j = 0; j<poorten.length; j++) {
        if (poorten[i][j]!=null) {
          array = append(array, str(poorten[i][j].an));
          array = append(array, str(poorten[i][j].toBeAn));
          for (int k =0; k<poorten[i][j].kleppen.length; k++) {
            array = append(array, str(poorten[i][j].kleppen[k].an));
            array = append(array, str(poorten[i][j].kleppen[k].hasChest));
          }
          array = append(array, "-");
        }
        if (millis()>milliCount) {//give it 7 seconds to save
          array = new String[0];
          array = append(array, "null");
          break outerloop;
        }
      }
    }
    saveStrings(path+"poorten.txt", array);
    /*
array = new String[0];
     for(int i = 0; i<nodeRegister.length; i++){
     for(int j = 0; j<nodeRegister.length; j++){
     array = append(array,str(nodeRegister[i][j].x));
     array = append(array,str(nodeRegister[i][j].y));
     }
     saveStrings("nodeRegister.txt",array);
     */
    array = new String[0];
    for (int i = 0; i<lijnen.length; i++) {
      array = append(array, str(lijnen[i].sizex));
      array = append(array, str(lijnen[i].sizey));
      array = append(array, str(lijnen[i].toBeSizeX));
      array = append(array, str(lijnen[i].toBeSizeY));
      array = append(array, str(lijnen[i].type));
      array = append(array, str(lijnen[i].isDoor));
    }
    saveStrings(path+"lijnen.txt", array);

    //Node[] bots = new Node[amountOfBots];

    array = new String[0];
    array = append(array, str(player.x));
    array = append(array, str(player.y));
    array = append(array, str(player.health));
    array = append(array, str(player.currentFood));
    array = append(array, str(player.selectedItem));
    array = append(array, str(player.animationFrames[0]));
    array = append(array, str(player.animationFrames[1]));
    array = append(array, str(player.animationFrames[2]));
    array = append(array, str(player.canRun));
    saveStrings(path+"player.txt", array);

    array = new String[0];
    for (int i = 0; i<player.inventory.size (); i++) {
      array = append(array, str(player.inventory.get(i).id));
      array = append(array, str(player.inventory.get(i).usesLeft));
    }
    saveStrings(path+"playerInventory.txt", array);

    saveStrings(path+"inventoryStrings.txt", player.inventoryStrings);

    array = new String[0];
    for (int i = 0; i<farmland.crops.length; i++) {
      for (int j = 0; j<farmland.crops[0].length; j++) {
        array = append(array, str(farmland.crops[i][j].state));
        array = append(array, str(farmland.crops[i][j].type));
      }
    }
    saveStrings(path+"farmland.txt", array);

    array = new String[0];
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      array = append(array, str(botSpawner.movementSteps[i]));
    }
    array = append(array, str(botSpawner.spawnPoint[0]));
    array = append(array, str(botSpawner.spawnPoint[1]));
    for (int i = 0; i<botSpawner.animationFrame.length; i++) {
      array = append(array, str(botSpawner.animationFrame[i]));
    }
    for (int i = 0; i<botSpawner.movementProgress.length; i++) {
      array = append(array, str(botSpawner.movementProgress[i]));
    }
    for (int i = 0; i<botSpawner.calculatedSpots.length; i++) {
      array = append(array, str(botSpawner.calculatedSpots[i]));
    }
    for (int i = 0; i<botSpawner.stopCalculating.length; i++) {
      array = append(array, str(botSpawner.stopCalculating[i]));
    }
    array = append(array, str(botSpawner.terminate));
    saveStrings(path+"botSpawner.txt", array);

    array = new String[0];
    if (botSpawner.spawnNode[0]==null) {
      array = append(array, "null");
    } else {
      for (int i = 0; i<botSpawner.spawnNode.length; i++) {
        array = append(array, str(botSpawner.spawnNode[i].x+(tileSize/3)));
        array = append(array, str(botSpawner.spawnNode[i].y+(tileSize/3)));
      }
    }
    if (bots[0]==null) {
      array = append(array, "null");
    } else {
      for (int i = 0; i<bots.length; i++) {
        array = append(array, str(bots[i].x+(tileSize/3)));
        array = append(array, str(bots[i].y+(tileSize/3)));
      }
    }
    saveStrings(path+"nodes.txt", array);
  }

  void loadGame() {
    int l;
    String[] array = loadStrings(path+"gameSettings.txt");
    for (int i = 0; i<gameSetup.length; i++) {
      gameSetup[i]=byte(int(array[i]));
    }
    doGameSetup();
    cleanupGame();
    setupGame();
    amountOfBots=int(array[6]);
    tileSize=int(array[7]);
    gladeSize = int(array[8]);
    camx = int(array[9]);
    camy = int(array[10]);
    borderSize = int(array[11]);
    night = boolean(array[12]);
    targetNode = int(array[13]);
    daytime = int(array[14]);
    vision = int(array[15]);
    day = int(array[16]);
    canMove = boolean(array[17]);
    notDrawn = boolean(array[18]);
    playerNodes[0] = int(array[19]);
    playerNodes[1] = int(array[20]);
    playerName = array[21];
    l=0;
    array = loadStrings(path+"trees.txt");
    for (int i = 0; i<array.length/4; i++) {
      new Tree(int(array[l+0]), int(array[l+1]));
      trees.get(i).createdTrees = int(array[l+2]);
      trees.get(i).state = int(array[l+3]);
      l+=4;
    }
    l=0;
    array = loadStrings(path+"woodBlock.txt");
    for (int i = 0; i<array.length/6; i++) {
      new Wood(int(array[l+0]), int(array[l+1]));
      woodBlock.get(i).currentCycle = int(array[l+2]);
      woodBlock.get(i).currentImage = int(array[l+3]);
      woodBlock.get(i).isBurning = boolean(array[l+4]);
      woodBlock.get(i).isCraftingTable = boolean(array[l+5]);
      l+=6;
    }

    pigs.clear();
    array = loadStrings(path+"pigs.txt");
    l=0;
    for (int i = 0; i<array.length/7; i++) {
      pigs.add(new Pig(int(array[l]), int(array[l+1])));
      pigs.get(i).animationFrames[0] = int(array[l+2]);
      pigs.get(i).animationFrames[1] = int(array[l+3]);
      pigs.get(i).wheatEaten = int(array[l+4]);
      pigs.get(i).walkingProgress = int(array[l+5]);
      pigs.get(i).followPlayer = boolean(array[l+6]);
      l+=7;
    }

    array = loadStrings(path+"traps.txt");
    l=0;
    for (int i = 0; i<array.length/2; i++) {
      traps.add(new Trap(int(array[l]), int(array[l+1])));
      l+=2;
    }

    array = loadStrings(path+"poorten.txt");
    l = 0;
    if (!array[0].equals("null")) {
      for (int i = 0; i<poorten.length; i++) {
        for (int j = 0; j<poorten.length; j++) {
          if (poorten[i][j]!=null) {
            poorten[i][j].an=int(array[l]);
            poorten[i][j].toBeAn=int(array[l+1]);
            poorten[i][j].kleppen = new klep[0];
            l+=2;
            int k = 0;
            while (!array[l].equals ("-")) {
              poorten[i][j].kleppen = (klep[]) append(poorten[i][j].kleppen, new klep(int(array[l])));
              poorten[i][j].kleppen[k].hasChest=boolean(array[l+1]);
              k++;
              l+=2;
            }
            l+=1;
          }
        }
      }
    }
    /*
array = new String[0];
     for(int i = 0; i<nodeRegister.length; i++){
     for(int j = 0; j<nodeRegister.length; j++){
     array = append(array,str(nodeRegister[i][j].x));
     array = append(array,str(nodeRegister[i][j].y));
     }
     saveStrings("nodeRegister.txt",array);
     */

    array = loadStrings(path+"lijnen.txt");
    l=0;
    for (int i = 0; i<lijnen.length; i++) {
      lijnen[i].sizex = int(array[l+0]);
      lijnen[i].sizey = int(array[l+1]);
      lijnen[i].toBeSizeX = int(array[l+2]);
      lijnen[i].toBeSizeY = int(array[l+3]);
      lijnen[i].type = boolean(array[l+4]);
      lijnen[i].isDoor = boolean(array[l+5]);
      l+=6;
    }

    //Node[] bots = new Node[amountOfBots];

    array = loadStrings(path+"player.txt");
    player.x = int(array[0]);
    player.y = int(array[1]);
    player.health = int(array[2]);
    player.currentFood = int(array[3]);
    player.selectedItem = int(array[4]);
    player.animationFrames[0] = int(array[5]);
    player.animationFrames[1] = int(array[6]);
    player.animationFrames[2] = int(array[7]);
    player.canRun = boolean(array[8]);

    player.inventory.clear();
    l=0;
    array = loadStrings(path+"playerInventory.txt");
    for (int i = 0; i<array.length/2; i++) {
      player.inventory.add(new Item(int(array[l]), true));
      player.inventory.get(i).usesLeft=int(array[l+1]);
      l+=2;
    }

    player.inventoryStrings = loadStrings(path+"inventoryStrings.txt");

    array = loadStrings(path+"farmland.txt");
    l = 0;
    for (int i = 0; i<farmland.crops.length; i++) {
      for (int j = 0; j<farmland.crops[0].length; j++) {
        farmland.crops[i][j].state= int(array[l]);
        farmland.crops[i][j].type= int(array[l+1]);
        l+=2;
      }
    }
    array = loadStrings(path+"botSpawner.txt");
    for (int i = 0; i<botSpawner.movementSteps.length; i++) {
      botSpawner.movementSteps[i]=int(array[i]);
    }
    botSpawner.spawnPoint[0]=int(array[botSpawner.movementSteps.length]);
    botSpawner.spawnPoint[1]=int(array[botSpawner.movementSteps.length+1]);
    for (int i = 0; i<botSpawner.animationFrame.length; i++) {
      botSpawner.animationFrame[i]=int(array[botSpawner.movementSteps.length+2+i]);
    }
    for (int i = 0; i<botSpawner.movementProgress.length; i++) {
      botSpawner.movementProgress[i]=int(array[botSpawner.movementSteps.length+2+botSpawner.animationFrame.length+i]);
    }
    for (int i = 0; i<botSpawner.calculatedSpots.length; i++) {
      botSpawner.calculatedSpots[i]=int(array[botSpawner.movementSteps.length+2+botSpawner.animationFrame.length+i+botSpawner.movementProgress.length]);
    }
    for (int i = 0; i<botSpawner.stopCalculating.length; i++) {
      botSpawner.stopCalculating[i]=boolean(array[botSpawner.movementSteps.length+2+botSpawner.animationFrame.length+i+botSpawner.movementProgress.length+botSpawner.calculatedSpots.length]);
    }
    botSpawner.terminate=boolean(array[botSpawner.movementSteps.length+2+botSpawner.animationFrame.length+botSpawner.stopCalculating.length+botSpawner.movementProgress.length+botSpawner.calculatedSpots.length]);

    array = loadStrings(path+"nodes.txt");
    l=0;
    if (array[l].equals("null")) {
      l++;
    } else {
      println("in");
      for (int i = 0; i<botSpawner.spawnNode.length; i++) {
        botSpawner.spawnNode[i]=getNodeAt(int(array[l]), int(array[l+1]));
        l+=2;
      }
    }
    if (!array[l].equals("null")) {
      println("in2");
      for (int i = 0; i<bots.length; i++) {
        bots[i]=getNodeAt(int(array[l]), int(array[l+1]));
        l+=2;
      }
    }
    for (int i = 0; i<amountOfBots; i++) {
      botSpawner.calculateClosestPathToPlayer(i);
    }
  }

  boolean fileExists(String filename) {
    File file = new File(sketchPath(filename));
    if (!file.exists()) {
      return false;
    }
    return true;
  }
}
