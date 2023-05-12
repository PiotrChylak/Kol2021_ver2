import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Country {
    private static class CountryColumns{
        public final int firstColumnIndex;
        public final int columnCount;

        public CountryColumns(int firstColumnIndex, int columnCount) {
            this.firstColumnIndex = firstColumnIndex;
            this.columnCount = columnCount;
        }
    }
    private final String name;
    private static String INFECTIONS_FILE_PATH = "C:\\Users\\Olek\\IdeaProjects\\Kolokwium2021\\data\\confirmed_cases.csv";
    private static String DEATHS_FILE_PATH = "C:\\Users\\Olek\\IdeaProjects\\Kolokwium2021\\data\\deaths.csv";

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public static Country fromCsv(String country) throws IOException, CountryNotFoundException {
        BufferedReader infectionsReader = new BufferedReader(new FileReader(INFECTIONS_FILE_PATH));
        BufferedReader deathsReader = new BufferedReader(new FileReader(DEATHS_FILE_PATH));
        String line = infectionsReader.readLine();
        String[] headers = line.split(";");
        //infectionsReader.readLine();
        deathsReader.readLine();

        // Find country index
        int countryIndex = getCountryCollumns(line,country).firstColumnIndex;
            // Check if country was found
        if(getCountryCollumns(line,country).columnCount<1){
            throw new CountryNotFoundException(country);
        }
        else {
            // Read data
            List<String> provinces = new ArrayList<>();

            //read provinces
            String[] deathsTokens = deathsReader.readLine().split(";");
            boolean found = false;
            for (int i = countryIndex; i < countryIndex + getCountryCollumns(line, country).columnCount - 1; i++) {
                if (deathsTokens[i].equals("nan") && i == countryIndex) {
                    break;
                } else if (found == true && deathsTokens[i].equals("nan")) {
                    break;
                } else if (i >= countryIndex && !deathsTokens[i].equals("nan")) {
                    found = true;
                    //System.out.println(deathsTokens[i]);
                    provinces.add(deathsTokens[i]);
                }

            }
            infectionsReader.close();
            deathsReader.close();
            List<Country> provinces_country = new ArrayList<>();
            for (int i = 0; i < provinces.size(); i++) {
                provinces_country.add(new CountryWithoutProvinces(provinces.get(i)));
            }
            CountryColumns collumns = getCountryCollumns(line,country);
            if (provinces.size() > 1) {
                return new CountryWithProvinces(country, provinces_country);
            } else {
                CountryWithoutProvinces temp = new CountryWithoutProvinces(country);
                BufferedReader noProvincesCases = new BufferedReader(new FileReader(INFECTIONS_FILE_PATH));
                BufferedReader noProvincesDeaths = new BufferedReader(new FileReader(DEATHS_FILE_PATH));
                noProvincesCases.readLine();//skip 1st line
                noProvincesDeaths.readLine();
                noProvincesCases.readLine();//skip 2nd line
                noProvincesDeaths.readLine();
                String casesDateLine = noProvincesCases.readLine();//1st data line
                String[] fullLines=line.split(";");
                DateTimeFormatter dft = DateTimeFormatter.ofPattern("MM/dd/yy");
                LocalDate date = LocalDate.parse(fullLines[0],dft);
                int casesToday = Integer.parseInt(fullLines[collumns.firstColumnIndex]);
                int deathsToday = Integer.parseInt((fullLines[collumns.firstColumnIndex]));
                temp.addDailyStatistics(date,casesToday,deathsToday);
                while(noProvincesCases.readLine().contains("0")) {
                    fullLines = line.split(";");
                    date = LocalDate.parse(fullLines[0], dft);
                    casesToday = Integer.parseInt(fullLines[collumns.firstColumnIndex]);
                    deathsToday = Integer.parseInt((fullLines[collumns.firstColumnIndex]));
                    temp.addDailyStatistics(date,casesToday,deathsToday);
                }
                noProvincesCases.close();
                noProvincesDeaths.close();
                return new CountryWithoutProvinces(country);

            }
        }
    }
    private static CountryColumns getCountryCollumns(String line, String country){
        String[] countries =line.split(";");
        int counter = 0;
        int first = 0;
        for(int i=1;i<countries.length;i++){
            if(countries[i].equals(country)){
                first =i;
                counter++;
                break;
            }
        }
        for(int i=first+1;i<countries.length;i++){
            if(countries[first].equals(countries[i])){
                counter++;
            }
            else{
                break;
            }
        }
        return new CountryColumns(first,counter);
    }
}
