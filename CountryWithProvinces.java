import java.util.List;

public class CountryWithProvinces extends Country{
    private List<Country> provinces;

    public CountryWithProvinces(String name, List<Country> provinces) {
        super(name);
        this.provinces = provinces;
    }
}
