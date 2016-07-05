class klep {
  int an;
  boolean hasChest = int(random(chestSpawnRate))==1;
  klep(int _a) {
    an=_a;
  }
  void teken(int x, int y, float _an) { 
    float nx = x+cos(radians(int(an+_an)))*(tileSize/2);
    float ny = y+sin(radians(int(an+_an)))*(tileSize/2);
    line(x+camx, y+camy, nx+camx, ny+camy);
    if(hasChest){
      setTransparentImage(x+camx+(tileSize/3), y+camy+(tileSize/3), chestImage);
    }
  }
}

