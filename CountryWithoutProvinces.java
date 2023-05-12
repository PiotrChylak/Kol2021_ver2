import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CountryWithoutProvinces extends Country{
    private class DailyCasesAndDeaths{
        public final LocalDate date;
        public final int cases;
        public final int deaths;

        public DailyCasesAndDeaths(LocalDate date, int cases, int deaths) {
            this.date = date;
            this.cases = cases;
            this.deaths = deaths;
        }
    }
    public List<DailyCasesAndDeaths> dailyStatistics = new ArrayList<>();
    public CountryWithoutProvinces(String name){
        super(name);
    }
    public void addDailyStatistics(LocalDate day, int casesThisDay, int deathsThisDay){
        dailyStatistics.add(new DailyCasesAndDeaths(day,casesThisDay,deathsThisDay));
    }

}
