class Crop {
  int x, y, state=0, type;

  Crop(int x, int y) {
    this.x=x;
    this.y=y;
  }

  void teken() {
    //println((x+farmland.x+camx)+", "+(y+farmland.y+camy));
    setTransparentImage(x+farmland.x+camx, y+farmland.y+camy, farmland.farmLand);
    if (state>0) {
      setTransparentImage(x+farmland.x+camx, y+farmland.y+camy, farmland.cropImage[type][state-1]);
    }
  }
}