package uz.jaxadev.newsapp;

public class News {

    private String title;
    private String section;
    private String link;
    private String date;
    private String author;

    public News(String title, String section, String link, String date, String author) {
        this.title = title;
        this.section = section;
        this.link = link;
        this.date = date;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

}
