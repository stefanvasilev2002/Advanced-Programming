import java.util.*;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

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
class Movie{
    String name;
    List<Integer> ratings;
    int maxRatings;
    public Movie(String title, List<Integer> list) {
        this.name = title;
        this.ratings = list;
    }
    public double avgRating(){
        return ratings.stream().mapToDouble(x->x).sum() / ratings.size();
    }

    public String getName() {
        return name;
    }
    public double ratingCoef(){
        return avgRating() * ratings.size() / maxRatings;
    }

    public void setMaxRatings(int maxRatings) {
        this.maxRatings = maxRatings;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", name, avgRating(), ratings.size());
    }
}
class MoviesList{
    List<Movie>movies;
    public MoviesList(){
        this.movies = new ArrayList<>();
    }
    public void addMovie(String title, int[] ratings){
        movies.add(new Movie(title, Arrays.stream(ratings)
                .boxed()
                .collect(Collectors.toList())));
    }
    public List<Movie> top10ByAvgRating(){
        return movies
                .stream()
                .sorted(Comparator
                        .comparing(Movie::avgRating)
                        .reversed()
                        .thenComparing(Movie::getName))
                .limit(10)
                .collect(Collectors.toList());
    }
    public List<Movie> top10ByRatingCoef(){
        int maxNumRatings = movies
                .stream()
                .mapToInt(x->x.ratings.size())
                .max().orElse(0);
        for(Movie m : movies){
            m.setMaxRatings(maxNumRatings);
        }

        return movies
                .stream()
                .sorted(Comparator
                        .comparing(Movie::ratingCoef)
                        .reversed()
                        .thenComparing(Movie::getName))
                .limit(10)
                .collect(Collectors.toList());
    }
}