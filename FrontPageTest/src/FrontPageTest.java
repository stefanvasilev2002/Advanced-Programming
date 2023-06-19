import java.util.*;
import java.util.stream.Collectors;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
class Category {
    String category;

    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Category category1 = (Category) o;

        return Comparator.comparing(Category::getCategory).compare(this, category1) == 0;
    }

    @Override
    public int hashCode() {
        return category != null ? category.hashCode() : 0;
    }
}
abstract class NewsItem {
    String title;
    Date date;
    Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public String getCategory() {
        return category.getCategory();
    }
    public abstract String  getTeaser();
}
class TextNewsItem extends NewsItem{
    String text;
    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    public String getTeaser() {
        Date tmp = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s\n", title));
        sb.append(String.format("%d\n", (tmp.getTime() - date.getTime()) / 1000 / 60));
        if (text.length() > 80){
            sb.append(text.substring(0, 80));
        }
        else {
            sb.append(text);
        }
        sb.append("\n");
        return sb.toString();
    }
}
class MediaNewsItem extends NewsItem{
    String url;
    int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        Date tmp = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s\n", title));
        sb.append(String.format("%d\n", (int)(tmp.getTime() - date.getTime()) / 1000 / 60));
        sb.append(String.format("%s\n", url));
        sb.append(String.format("%d\n", views));
        return sb.toString();
    }
}
class FrontPage {
    List<NewsItem> news;
    List<Category> categories;
    public FrontPage(Category[] categories){
        news = new ArrayList<>();
        this.categories = Arrays.stream(categories).collect(Collectors.toList());
    }
    public void addNewsItem(NewsItem newsItem){
        news.add(newsItem);
    }
    public List<NewsItem> listByCategory(Category category){
        return news
                .stream()
                .filter(x-> Objects.equals(x.getCategory(), category.getCategory()))
                .collect(Collectors.toList());
    }
    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        Category c = new Category(category);
        if (!categories.contains(c)){
            throw new CategoryNotFoundException(category);
        }
        return news
                .stream()
                .filter(x-> Objects.equals(x.getCategory(), c.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (NewsItem n : news){
            sb.append(n.getTeaser());
        }
        return sb.toString();
    }
}
class CategoryNotFoundException extends Exception{

    public CategoryNotFoundException(String category) {
        super(String.format("Category %s was not found", category));
    }
}