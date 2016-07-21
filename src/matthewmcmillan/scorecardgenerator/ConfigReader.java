package matthewmcmillan.scorecardgenerator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.util.HashMap;

public final class ConfigReader {
    private ConfigReader() {
    }

    private static Sheet sheet;
    private static String competition_name;

    public static String getWorkbookName() {
        return workbook_name;
    }

    public static int getBlankScorecards() {
        return blank_scorecards;
    }

    public static String getCompetitionName() {
        return competition_name;
    }

    private static int blank_scorecards;
    private static String workbook_name;
    public static void readConfig() {
    /* this will need much better error + exception handling later on, probably
       separated into multiple try/catch blocks */
        try {
      /* opens the workbook stored in the file, and takes the first
         sheet. That is the only sheet we will be using */
            Workbook wb = WorkbookFactory.create(new File("config.xls"));
            sheet = wb.getSheetAt(0);
            readCompName();
            readWorkbookName();
            readBlankScorecards();
        }
        catch (Exception e) {
            System.out.println("Error: Configuration file not readable");
            System.exit(0);
        }
    }
    public static Row getEventRow(String abbreviation) {
        return sheet.getRow(event_rows.get(abbreviation));
    }

    private static void readCompName() {
        competition_name = sheet.getRow(22).getCell(2).getStringCellValue();
    }

    private static void readWorkbookName() {
        workbook_name = sheet.getRow(24).getCell(2).getStringCellValue();
    }
    private static void readBlankScorecards() {
        blank_scorecards = (int)sheet.getRow(23).getCell(2).getNumericCellValue();
    }
    private final static HashMap<String, Integer> event_rows = new HashMap<String, Integer>() {{
        put("2x2", 1);
        put("3x3", 2);
        put("4x4", 3);
        put("5x5", 4);
        put("6x6", 5);
        put("7x7", 6);
        put("333oh", 7);
        put("333bld", 8);
        put("fmc", 9);
        put("333ft", 10);
        put("magic", 11);
        put("mmagic", 12);
        put("sq1", 13);
        put("pyra", 14);
        put("mega", 15);
        put("clock", 16);
        put("444bld", 17);
        put("555bld", 18);
        put("333mlt", 19);
        put("skewb", 20);
    }};
}
