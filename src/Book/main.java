package Book;

public class main {
    public static void main(String[] args) {
        Book book1 = new Book("西游记", "吴承恩", 39.9, 500);
        Book book2 = new Book("水浒传", "施耐庵", 45.0, 600);
        Book book3 = new Book("三国演义", "罗贯中", 49.9, 700);

        book1.showInfo();
        book2.showInfo();
        book3.showInfo();
    }
}
