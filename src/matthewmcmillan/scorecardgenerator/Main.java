package matthewmcmillan.scorecardgenerator;

public class Main {
    public static String competition_name;
    public static int blank_scorecards;
    public static int[] heat_sizes = {0};
    public static String workbook_name;
    public static ConfigReader conf = new ConfigReader();
    public static void main(String[] args) {
        EventsManager.generateSheets();
    }
}