class Audio {


  int music = 3;

  Audio() {
    grass.setMediaFile("grass.mp3");
    grass.setLooping(true);
    intro.setMediaFile("intro.mp3");
    intro.setLooping(true);
    click.setMediaFile("click.mp3");
    click.setLooping(false);
    maze.setMediaFile("maze.mp3");
    maze.setLooping(true);
    nightMusic.setMediaFile("night.mp3");
    nightMusic.setLooping(true);
    fire.setMediaFile("fire.mp3");
    fire.setLooping(false);
    playMusic(intro);
  }

  void playMusic(APMediaPlayer music) {
    music.seekTo(0);
    music.start();
  }

  void playSound(APMediaPlayer sound) {
    sound.seekTo(0);
    sound.start();
  }

  void stopAllMusic() {
    intro.pause();
    grass.pause();
    maze.pause();
    nightMusic.pause();
  }

  void update() {
    if (isCloseTo((player.x-camx)/tileSize, gladeSize, numOfColumns/2) && isCloseTo((player.y-camy)/tileSize, gladeSize, numOfColumns/2)) {
      if (music!=0) {
        stopAllMusic();
        playMusic(grass);
        music=0;
      }
    } else {
      if (night) {
        if (music!=1) {
          stopAllMusic();
          playMusic(nightMusic);
          music=1;
        }
      } else {
        if (music!=2) {
          stopAllMusic();
          playMusic(maze);
          music=2;
        }
      }
    }
  }
}