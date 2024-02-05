
import java.util.*;

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
class Category{
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class NewsItem{
    private String title;
    private Date publishingDate;
    private Category category;

    public NewsItem(Category category){
        this.category=category;
    }

    public NewsItem(Category category, String title, Date date) {
        this.category=category;
        this.title=title;
        this.publishingDate=date;
    }

    public String getTitle() {
        return title;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public Category getCategory() {
        return category;
    }

    public boolean compareTo(NewsItem o) {
        return category.equals(o.getCategory());
    }
}
class TextNewsItem extends NewsItem{
    private String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(category, title, date);
        this.text=text;
    }

    public String getTeaser(){
        StringBuilder sb=new StringBuilder();
        sb.append(getTitle());
        sb.append("\n");
        Calendar cal=Calendar.getInstance();
        Date a=cal.getTime();
        long minutes=Math.abs(getPublishingDate().getTime()-a.getTime());
        minutes=minutes/1000/60;
        sb.append(minutes);
        sb.append("\n");
        if(text.length()<=80){
            sb.append(text);
            sb.append("\n");
        }
        else {
            for(int i=0; i<80; i++){
                sb.append(text.charAt(i));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
class MediaNewsItem extends NewsItem{
    private String url;
    private int numberOfViews;


    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(category, title, date);
        this.numberOfViews=views;
        this.url=url;
    }

    public String getTeaser(){
        StringBuilder sb=new StringBuilder();
        sb.append(getTitle());
        sb.append("\n");
        Calendar cal=Calendar.getInstance();
        Date a=cal.getTime();
        long minutes=Math.abs(getPublishingDate().getTime()-a.getTime());
        minutes=minutes/1000/60;
        sb.append(minutes);
        sb.append("\n");
        sb.append(url);
        sb.append("\n");
        sb.append(numberOfViews);
        sb.append("\n");
        return sb.toString();
    }
}
class FrontPage{
    List<NewsItem> items;
    Category[]categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        items=new ArrayList<NewsItem>();
    }
    public void addNewsItem(NewsItem newsItem){
       items.add(newsItem);
       //todo if(checkDescription(newsItem.getCategory()));
    }
    public List<NewsItem> listByCategory(Category category){
        NewsItem def=new NewsItem(category);
        List<NewsItem> sameCategory=new ArrayList<NewsItem>();
        for(int i=0; i<items.size(); i++){
            if(def.getCategory().equals(items.get(i).getCategory())){
                sameCategory.add(items.get(i));
            }
        }
        return sameCategory;
    }
    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        int counter=0;
        boolean cat=false;
        List<NewsItem> filtered=new ArrayList<NewsItem>();
        for(int i=0; i<items.size(); i++){
            if(Objects.equals(category, items.get(i).getCategory().getName())){
                counter++;
                filtered.add(items.get(i));
            }
        }
        for(int i=0; i<categories.length; i++){
            if(Objects.equals(category, categories[i].getName()) ){
                cat=true;
            }
        }
        if(counter>0 || cat){
            return filtered;
        }
        else {
            throw new CategoryNotFoundException(category);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<items.size(); i++){
            if(items.get(i) instanceof MediaNewsItem){
                MediaNewsItem temp=(MediaNewsItem)items.get(i);
                sb.append(temp.getTeaser());
            }
            else {
                TextNewsItem temp=(TextNewsItem)items.get(i);
                sb.append(temp.getTeaser());
            }
        }
        return sb.toString();
    }
}
class CategoryNotFoundException extends Throwable {
    String category;
    public CategoryNotFoundException(String category) {
        this.category=category;
    }

    public String getMessage() {
        return String.format("Category %s was not found", category);
    }
}