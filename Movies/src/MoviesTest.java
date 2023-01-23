import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}
class Movie implements Comparable<Movie>{
    String title;
    List<Integer> ratings;

    public Movie(String title, int[] ratings) {
        this.title=title;
        this.ratings=new ArrayList<>();
        for(int i : ratings){
            this.ratings.add(i);
        }
    }
    public double average(){
        double sum=0;
        for(int i=0; i<ratings.size(); i++){
            sum+=ratings.get(i);
        }
        return sum/ratings.size();
    }

    @Override
    public int compareTo(Movie o) {
        if(Double.compare(average(), o.average())==0){
            return  o.title.compareTo(title);
        }
        return Double.compare(average(), o.average());
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",title, average(), ratings.size());
    }
}
class MoviesList {
    List<Movie> movies;
    int maxRating;

    public MoviesList() {
        movies=new ArrayList<>();
        maxRating=0;
    }

    public void addMovie(String title, int[] ratings){
        movies.add(new Movie(title, ratings));
        if(ratings.length>maxRating){
            maxRating=ratings.length;
        }
    }
    public List<Movie> top10ByAvgRating(){
        return movies.stream().sorted(Comparator.reverseOrder()).limit(10).collect(Collectors.toList());
    }
    public List<Movie> top10ByRatingCoef(){
        return movies.stream()
                .sorted(new CoefficientComparator(maxRating).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
class CoefficientComparator implements Comparator<Movie> {
    int maxRating;
    public CoefficientComparator(int maxRating) {
        this.maxRating=maxRating;
    }

    @Override
    public int compare(Movie o1, Movie o2) {
        if(Double.compare((o1.average()*o1.ratings.size()/maxRating),(o2.average()*o2.ratings.size()/maxRating))==0){
            return o2.title.compareTo(o1.title);
        }
        return Double.compare((o1.average()*o1.ratings.size()/maxRating),(o2.average()*o2.ratings.size()/maxRating));
    }
}