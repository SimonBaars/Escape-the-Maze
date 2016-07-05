class Tutorial {
  boolean active = false;
  ArrayList<Integer> tutorialQueue = new ArrayList<Integer>();
  int currentStage;
  boolean[] shown = new boolean[32];

  Tutorial() {
    shown= fillWithNumber(shown, false);
  }

  void update() {
    createQueue();
    processQueue();
  }

  void createQueue() {
    if (currentStage==0) {
      tutorialQueue.add(1);
      tutorialQueue.add(2);
      tutorialQueue.add(3);
      tutorialQueue.add(4);
      currentStage++;
    } else if (currentStage==1 && player.inMaze()) {
      tutorialQueue.add(5);
      tutorialQueue.add(6);
      tutorialQueue.add(7);
      currentStage++;
    } else if (currentStage==2 && daytime<800 && player.inMaze() && !shown[11]) {
      tutorialQueue.add(11);
    } else if (currentStage==2 && daytime<=1 && player.inMaze() && !shown[12]) {
      tutorialQueue.add(12);
    } else if (currentStage==2 && night && !player.inMaze() && !shown[13]) {
      tutorialQueue.add(13);
      currentStage++;
    } else if (player.inventoryStrings[4].charAt(0)!='0' && !player.inMaze() && !shown[14]) {
      tutorialQueue.add(14);
      tutorialQueue.add(16);
    } else if (player.inventoryStrings[4].charAt(0)=='0' && night && !player.inMaze() && !shown[31]) {
      tutorialQueue.add(31);
    } else if (player.inventoryStrings[7].charAt(0)!='0' && !player.inMaze() && !shown[17]) {
      tutorialQueue.add(17);
      tutorialQueue.add(18);
    } else if (player.inventoryStrings[1].charAt(0)!='0' && !player.inMaze() && !shown[19]) {
      tutorialQueue.add(19);
      tutorialQueue.add(20);
    } else if (player.inventoryStrings[0].charAt(0)!='0' && !player.inMaze() && !shown[21]) {
      tutorialQueue.add(21);
      tutorialQueue.add(22);
    } else if (player.inventoryStrings[9].charAt(0)!='0' && !player.inMaze() && !shown[26]) {
      tutorialQueue.add(26);
    } else if (player.inventoryStrings[18].charAt(0)!='0' && !player.inMaze() && !shown[30]) {
      tutorialQueue.add(30);
    }
  }

  void processQueue() {
    if (tutorialQueue.size()!=0) {
      scherm=-1;
      fill(0, 0, 0, 100);
      rectMode(CORNER);
      noStroke();
      textSize(width/30);
      rect(0, 0, width, height);
      rectMode(CENTER);
      textAlign(CENTER, CENTER);
      fill(255);
      text(getTutorialMessage(tutorialQueue.get(0)), width/2, height/2, width/2, height/2);
      shown[tutorialQueue.get(0)]=true;
      tutorialQueue.remove(0);
      for(int i = 1; i<tutorialQueue.size(); i++){
        if(tutorialQueue.get(i)==tutorialQueue.get(0)){
          tutorialQueue.remove(i);
          i--;
        }
      }
      textAlign(CORNER, CORNER);
    }
  }

  String getTutorialMessage(int i) {
    switch(i) {
    case 1: 
      return "Welcome in the maze. I will help you get started, but won't give you too much clues...";//done
    case 2: 
      return "The main priority now is survival. To survive you will need food.";//done
    case 3: 
      return "Food and other goods are obtainable in the maze. Search for crates and press interact to gather goods from them.";//done
    case 4: 
      return "Now head left, right, up- or downwards to get into the maze";//done
    case 5: 
      return "Welcome in the maze. The maze is a huge place where you'll easily get lost";//done
    case 6: 
      return "Keep track of where you go and make sure you are back well before nighttime";//done
    case 7: 
      return "If you cannot make it back before the end of the day you'll most likely die...";//done
    case 8: 
      return "Congratulations on finding your first crate!";//done
    case 9: 
      return "Congratulations on finding wheat seeds! Wheat is the number one item for survival";//done
    case 10: 
      return "Good thing you got some tree seeds. When you get back in the grass field you'll be able to make a workbench!";//done
    case 11: 
      return "Hurry! Get back to the grass field!";//done
    case 12: 
      return "Seems like you didn't make it in time... Good luck outrunning the bugs!";//done
    case 13: 
      return "It's night, and you are back in the grass field! Nice!";//done
    case 14: 
      return "Now you can start farming. Use the wheat seeds when close to farmland, then interact with it when it's fully grown.";//done
    case 15: 
      return "With cotton you can make shoes. Plant the seeds on your farm at the top right corner of the grass field.";//done
    case 16: 
      return "The farmland can be found at the top right corner of the grass field";//done
    case 17: 
      return "Plant the tree seeds by using them near the middle of the grass field";//done
    case 18: 
      return "Wait for the tree to grow, then use the axe to gather wood!";//done
    case 19: 
      return "You got some wheat! You can craft 5 wheat into bread if you have a workbench. If not, you can feed it to the pigs.";//done
    case 20: 
      return "If you kill a pig by using your axe on them you'll get more porkchops if the pig has eaten a lot of wheat";//done
    case 21: 
      return "Now place the wood by pressing use near the middle of the grass field.";//done
    case 22: 
      return "Now press interact with the axe near the wood block to get a workbench.";//done
    case 23: 
      return "You can now craft! Press interact near the workbench to open the crafting menu!";//done
    case 24: 
      return "You found a lighter. Use it near a placed woodblock to light it on fire!";//done
    case 25: 
      return "Use a porkchop near a litten woodblock to turn it into cooked porkchop!";//done
    case 26: 
      return "You got shoes! Use the shoes to wear them and then press the sprint button to activate sprint mode!";//done
    case 27: 
      return "You found magic dust! Use it to create a map and find out about the exit of the maze!";//done
    case 28: 
      return "To win the game use the map to find out the place the bugs come from.";//done
    case 29: 
      return "But watch out! This place changes every night!";//done
    case 30: 
      return "Use the pickaxe near the mine to gather iron.";//done
    case 31: 
      return "You made your way back, but didn't find any wheat seeds. To survive I would suggest you to kill your pigs by using your axe near them.";//done
    }
    return "Don't die!";
  }
}
