import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Country test = null;
        try {
            test = Country.fromCsv("Australia");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CountryNotFoundException e) {
            e.getMessage();
        }
        System.out.println(test);
    }
}
