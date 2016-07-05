class Poort {
  int x, y;
  //float moving =0;
  //int lastmove = millis()+int(random(10000));
  int an;
  int toBeAn;
  //float[] moves = new float[0];
  klep[] kleppen = new klep[0];
  Poort  (int _x, int _y) {
    an =  int(random(4))*90; 
    toBeAn=an;
    x=_x; 
    y=_y; 
    int n=0;
    for (int a=0; a<4; a++) {
      if ((n<3 && random(1.8)<1) || ( n==0 && a==3 )) {
        n++; 
        kleppen = (klep[]) append(kleppen, new klep((a*90)));
      }
    }
    //poorten = (Poort[]) append(poorten, this);
  }

  void checkUpdate() {
    if (toBeAn!=an) {
      if (an>toBeAn) {
        an--;
        if (veryCloseToPlayer()) {
          for (int i = 0; i<kleppen.length; i++) {
            player.checkCollisionAndMove(kleppen[i].an+an);
          }
          //println(this+"is very close");
        }
      } else {
        an++;
        if (veryCloseToPlayer()) {
          for (int i = 0; i<kleppen.length; i++) {
            player.checkCollisionAndMove(kleppen[i].an+an+180);
          }
          //println(this+"is very close");
        }
      }
      /*if(veryCloseToPlayer()){
       for(int i = 0; i<kleppen.length; i++){
       player.checkCollisionAndMove(kleppen[i].an+an);
       }
       //println(this+"is very close");
       }*/
    }
  }

  boolean veryCloseToPlayer() {
    //println((x+camx)+", "+player.x+", "+(y+camy)+", "+player.y);
    if (isCloseTo(x+camx, tileSize/2, player.x+(player.size/2)) && isCloseTo(y+camy, tileSize/2, player.y+(player.size/2))) {
      return true;
    }
    return false;
  }

  void teken(/*int m*/) {
    for (int a=0; a<kleppen.length; a++) { 
      kleppen[a].teken(x, y, an);
    }
    /*if (moves.length!=0) {
      an = int(moves[0]);
      moves = subsetTransparentImagemoves, 1);
    } else { 
      if (m>lastmove) {
        lastmove = m+10000;
        moves = getMoves(an);
      }
    }*/
  }
}

