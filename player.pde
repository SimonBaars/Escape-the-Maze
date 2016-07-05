class Player {
  int x, y, size;
  float health=100;
  int movingSpeed=tileSize/40;
  int runningSpeed=movingSpeed*2;
  final int wallColor = #525252;
  final int amountOfItems=21;
  ArrayList<Item> inventory = new ArrayList<Item>();
  int currentFood=maxFood;
  int selectedItem=0;
  ArrayList<Integer> collisions = new ArrayList<Integer>();
  int[] animationFrames = new int[3];
  final int animationSpeed = 5;
  boolean canRun=false;
  String[] inventoryStrings = new String[amountOfItems];

  Player(int x, int y, int size) {
    this.x=x;
    this.y=y;
    this.size=size;

    animationFrames[0] = 2;
    animationFrames[1] = 0;
    animationFrames[2] = 0; 
  }

  void teken() {
    noStroke();
    setTransparentImage(x-(tileSize/12), y-(tileSize/12), playerImages[animationFrames[0]][animationFrames[1]]);
    //println(getLowestValue(x-camx, tileSize/3)+", "+getLowestValue(y-camy,tileSize/3));
  }
  
  void update(){
    if(player.x-camx>numOfColumns*tileSize || player.y-camy>numOfColumns*tileSize || player.x-camx<tileSize || player.y-camy<tileSize){
      notDrawn=true;
      scherm=3;
    }
  }
  
  boolean inMaze(){
    if(isCloseTo((x-camx)/tileSize,gladeSize,numOfColumns/2) && isCloseTo((y-camy)/tileSize,gladeSize,numOfColumns/2)){
      return false;
    }
    return true;
  }

  void checkOutOfScreen() {
    if (x<width/borderSize) {
      camx-=x-(width/borderSize);
      x-=x-(width/borderSize);
    } else if (x>width-(width/borderSize)) {
      camx-=x-(width-(width/borderSize));
      x-=x-(width-(width/borderSize));
    } else if (y<height/borderSize) {
      camy-=y-(height/borderSize);
      y-=y-(height/borderSize);
    } else if (y>height-(height/borderSize)) {
      camy-=y-(height-(height/borderSize));
      y-=y-(height-(height/borderSize));
    }
  }

  void feed(int amount) {
    currentFood+=amount;
    if (currentFood>maxFood) {
      currentFood=maxFood;
    }
    getSelectedItem().removeFromInventory();
  }

  int collidesAt(int angle, int distance, int spotSize, int coordX, int coordY) {
    int x = getCoordX(coordX, angle, distance+spotSize);
    int y = getCoordY(coordY, angle, distance+spotSize);
    if (get(x,y)==wallColor) {
      //println("ploff");
      for (int i = 0; true; i++) {
        x = getCoordX(coordX, angle, distance+spotSize-i);
        y = getCoordY(coordY, angle, distance+spotSize-i);
        if (get(x,y)!=wallColor) {
          distance=int(getDistance(x, y, coordX, coordY)-spotSize);
          return distance;
        }
      }
    } else if (isCollisionBox(x, y)) {
      return 0;
    }
    return distance;
  }

  boolean isCollisionBox(int x, int y) {
    x=x-camx;
    y=y-camy;
    for (int i = 0; i<collisions.size (); i+=4) {
      //println(x+", "+collisions.get(i));
      if (x>collisions.get(i) && y>collisions.get(i+1) && x<collisions.get(i)+collisions.get(i+2) && y<collisions.get(i+1)+collisions.get(i+3)) {
        return true;
      }
    }
    return false;
  }
  
  Item getSelectedItem(){
    for(int i = 0; i<inventory.size(); i++){
      if(inventory.get(i).id==selectedItem){
        return inventory.get(i);
      }
    }
    return null;
  }

  void moveTo(int moveX, int moveY) {
    int moveToX = x+moveX;
    int moveToY = y+moveY;
    int movementAngle = int(getAngle(x, y, moveToX, moveToY));
    int movementDistance = int(getDistance(moveToX, moveToY, x, y));
    if (movementAngle==0) {
      animationFrames[0]=3;
    } else if (movementAngle==90) {
      animationFrames[0]=1;
    } else if (movementAngle==180) {
      animationFrames[0]=2;
    } else if (movementAngle==270) {
      animationFrames[0]=0;
    } 
    animationFrames[2]+=(movementDistance/movingSpeed);
    if (animationFrames[2]>=animationSpeed) {
      animationFrames[2]=0;
      animationFrames[1]++;
      if (animationFrames[1]==4) {
        animationFrames[1]=0;
      }
    }
    int[] collisions = {
      collidesAt(movementAngle, movementDistance, size/2, x, y) /* collision directly in front of character */, collidesAt(movementAngle, movementDistance, size/2, getCoordX(x, movementAngle-90, (size/2)-1), getCoordY(y, movementAngle-90, (size/2)-1)), collidesAt(movementAngle, movementDistance, size/2, getCoordX(x, movementAngle+90, (size/2)-1), getCoordY(y, movementAngle+90, (size/2)-1))
      };
      //pixels[(getCoordY(moveToY, movementAngle-90, size/2)*height)+getCoordX(moveToX, movementAngle-90, size/2)]=#FF0000;
      //pixels[(getCoordY(moveToY, movementAngle+90, size/2)*height)+getCoordX(moveToX, movementAngle+90, size/2)]=#FF0000;
    setDirectionSpeed(movementAngle, getLowest(collisions));
  }

  int getLowest(int[] array) {
    int lowest = 9999999;
    for (int i  = 0; i<array.length; i++) {
      //println(array[i]);
      if (array[i]<lowest) {
        lowest=array[i];
      }
    }
    return lowest;
  }

  void setDirectionSpeed(double direction, double speed) {
    x = x+int((float)(speed*sin((float)(Math.toRadians(direction)))));
    y = y+int((float)(speed*-cos((float)(Math.toRadians(direction)))));
  }

  float getAngle(int x1, int y1, int x2, int y2) {
    int deltaX=x2-x1;
    int deltaY=y2-y1;
    if (deltaX>0 && deltaY>0) {
      return invertNumber((float)Math.toDegrees(atan2(x2-x1, y2-y1)), -45)+180+45;
    } else if (deltaX>0 && deltaY<0) {
      return invertNumber((float)Math.toDegrees(atan2(x2-x1, y2-y1))-90, 45)+45;
    }
    return invertNumber((float)Math.toDegrees(atan2(x2-x1, y2-y1)), 90)+90;
  }

  float invertNumber(float number, float median) {
    return number+((median-number*2));
  }

  float getDistance(int x1, int y1, int x2, int y2) {
    return sqrt(sq(x2-x1)+sq(y2-y1));
  }

  int getCoordX(int x, double direction, double distance) {
    return x+int((float)(distance*sin((float)(Math.toRadians(direction)))));
  }

  int getCoordY(int y, double direction, double distance) {
    return y+int((float)(distance*-cos((float)(Math.toRadians(direction)))));
  }

  void checkCollisionAndMove(int degrees) {
    //if(getCoordX(0, degrees-180, size/2)+(getCoordY(0, degrees-180, size/2)*height)>0 && getCoordX(0, degrees-180, size/2)+(getCoordY(0, degrees-180, size/2)*height)<width*height){
    //println((getCoordX(x, degrees-180, size/2)+(getCoordY(y, degrees-180, size/2)*height)));
    if (get(getCoordX(x, degrees-180, size/2+2),getCoordY(y, degrees-180, size/2+2))==wallColor) {
      //println("it's actually the color of the wall!");
      setDirectionSpeed(degrees, movingSpeed/2);
    }//}
  }

  boolean stuckInWall() {
    int collisions=0;
    int checkDegrees=0;
    for (int i = 0; i<4; i++) {
      if (get(getCoordX(x, checkDegrees, size/2+2),getCoordY(y, checkDegrees, size/2+2))==wallColor) {
        collisions++;
      }
      checkDegrees+=90;
    }
    return collisions>2;
  }

  boolean closeTo(int x, int y, int howClose) {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x, howClose, player.x+(player.size/2)) && isCloseTo(y, howClose, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }
  
  void outlinedText(String text, int x, int y) {
    fill(0, 0, 0);
    text(text, x-1, y); 
    text(text, x, y-1); 
    text(text, x+1, y); 
    text(text, x, y+1); 
    fill(255, 255, 255);
    text(text, x, y);
  }
}

