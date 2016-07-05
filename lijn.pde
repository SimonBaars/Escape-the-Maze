class lijn {
  int x, y, sizex, sizey;
  int toBeSizeX, toBeSizeY;
  boolean type;
  boolean isDoor;
  boolean outsideWall;

  lijn (int _x, int _y, int _sizex, int _sizey, boolean _type, boolean _isDoor, boolean _outsideWall) {
    x=_x; 
    y=_y; 
    sizex=_sizex; 
    sizey=_sizey;
    toBeSizeX=sizex;
    toBeSizeY=sizey;
    type=_type;
    isDoor=_isDoor;
    outsideWall=_outsideWall;
  }

  void teken() {
    line(x+camx, y+camy, x+sizex+camx, y+sizey+camy);
  }

  void checkUpdate() {
    if (toBeSizeX!=sizex) {
      if (sizex>toBeSizeX) {
        sizex--;
      } else {
        sizex++;
      }
    }
    if (toBeSizeY!=sizey) {
      if (sizey>toBeSizeY) {
        sizey--;
      } else {
        sizey++;
      }
    }
  }
}

