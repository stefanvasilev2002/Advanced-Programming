import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatternTest {
    public static void main(String[] args) {
        List<Song> listSongs = new ArrayList<>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player);
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player);
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player);
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player);
    }
}

class Song{
    String title;
    String artist;


    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }
    @Override
    public String toString() {
        return "Song{" +
                "title=" + title +
                ", artist=" + artist +
                '}';
    }
}
class MP3Player{
    List<Song> songs;
    int currentSong;
    String state;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        currentSong = 0;
        this.state = "";
    }

    public void pressPlay() {
        if(Objects.equals(state, "PLAY")){
            System.out.println("Song is already playing");
        }
        else{
            System.out.printf("Song %d is playing\n", currentSong);
        }

        state = "PLAY";
    }
    public void pressStop() {
        if(Objects.equals(state, "PLAY")){
            System.out.printf("Song %d is paused\n", currentSong);
        }
        else if (Objects.equals(state, "FWD") || Objects.equals(state, "REW")){
            System.out.println("Songs are stopped");
            currentSong = 0;
        }
        else{
            System.out.println("Songs are already stopped");
            currentSong = 0;
        }
        state = "STOP";
    }

    public void pressFWD() {
        System.out.println("Forward...");

        currentSong++;
        if(currentSong >= songs.size()){
            currentSong = 0;
        }
        state = "FWD";
    }

    public void pressREW() {
        System.out.println("Reward...");
        currentSong--;
        if(currentSong < 0){
            currentSong = songs.size() - 1;
        }
        state = "REW";
    }
    public void printCurrentSong() {
        System.out.println(songs.get(currentSong));
    }

    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong = " + currentSong +
                ", songList = " + songs + '}';
    }
}