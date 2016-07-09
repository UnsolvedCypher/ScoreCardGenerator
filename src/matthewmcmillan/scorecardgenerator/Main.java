package matthewmcmillan.scorecardgenerator;


import java.util.Scanner;

public class Main {
    public static String competition_name;
    public static int blank_scorecards;
    public static int[] heat_sizes;
    private static String workbook_name;
    public static void main(String[] args) {
        System.out.print("Please enter workbook name: ");
        workbook_name = new Scanner(System.in).nextLine();
        System.out.print("Please enter competition name: ");
        competition_name = new Scanner(System.in).nextLine();
        System.out.print("How many blank scorecards? ");
        blank_scorecards = Integer.parseInt(new Scanner(System.in).nextLine());
        System.out.print("Please enter heat sizes (or 0 for no heats): ");
        String heats = new Scanner(System.in).nextLine();
        heat_sizes = new int[heats.split(", ").length];
        for (int i = 0; i < heat_sizes.length; i++) {
            heat_sizes[i] = Integer.parseInt(heats.split(", ")[i]);
        }
        EventsManager.generateSheets(workbook_name);
    }
}
