package matthewmcmillan.scorecardgenerator;

public final class Main {
    private Main() {
    }

    public static int[] heat_sizes = {0};
    public static void main(String[] args) {
        ConfigReader.readConfig();
        EventsManager.generateSheets();
    }
}