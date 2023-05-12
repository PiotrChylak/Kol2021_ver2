public class CountryNotFoundException extends Exception{
    private String countryName;
    public CountryNotFoundException(String countryName) {
        this.countryName=countryName;
    }
    public String getMessage(){
        return "Country not found: "+countryName;
    }
}
