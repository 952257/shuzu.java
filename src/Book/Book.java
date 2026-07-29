package Book;

public class Book {
    String name;
    String author;
    double price;
    int pageCount;

    public Book(String name, String author, double price, int pageCount) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.pageCount = pageCount;
    }

    public void showInfo() {
        System.out.println("书名：" + name);
        System.out.println("作者：" + author);
        System.out.println("价格：" + price + "元");
        System.out.println("页数：" + pageCount + "页");
        System.out.println();
    }
}
