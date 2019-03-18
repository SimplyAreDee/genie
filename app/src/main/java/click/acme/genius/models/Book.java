package click.acme.genius.models;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Modèle utilisé pour les requêtes REST sur l'api Ibanbook
 * Pas utilisé pour le moment
 */
public class Book {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("title_long")
    @Expose
    private String titleLong;
    @SerializedName("isbn")
    @Expose
    private String isbn;
    @SerializedName("isbn13")
    @Expose
    private String isbn13;
    @SerializedName("dewey_decimal")
    @Expose
    private String deweyDecimal;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("msrp")
    @Expose
    private String msrp;
    @SerializedName("binding")
    @Expose
    private String binding;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("date_published")
    @Expose
    private String datePublished;
    @SerializedName("edition")
    @Expose
    private String edition;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("dimensions")
    @Expose
    private String dimensions;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("excerpt")
    @Expose
    private String excerpt;
    @SerializedName("synopsys")
    @Expose
    private String synopsys;
    @SerializedName("authors")
    @Expose
    private List<String> authors = null;
    @SerializedName("subjects")
    @Expose
    private List<String> subjects = null;
    @SerializedName("reviews")
    @Expose
    private List<String> reviews = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleLong() {
        return titleLong;
    }

    public void setTitleLong(String titleLong) {
        this.titleLong = titleLong;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getDeweyDecimal() {
        return deweyDecimal;
    }

    public void setDeweyDecimal(String deweyDecimal) {
        this.deweyDecimal = deweyDecimal;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getSynopsys() {
        return synopsys;
    }

    public void setSynopsys(String synopsys) {
        this.synopsys = synopsys;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

}