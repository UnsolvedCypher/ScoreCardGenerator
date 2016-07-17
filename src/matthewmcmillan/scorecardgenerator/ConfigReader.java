package matthewmcmillan.scorecardgenerator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;

public class ConfigReader {
    public static void readConfig() {
    /* this will need much better error + exception handling later on, probably
       separated into multiple try/catch blocks */
        try {
      /* opens the workbook stored in the file, and takes the first
         sheet. That is the only sheet we will be using */
            Workbook wb = WorkbookFactory.create(new File("config.xls"));
            Sheet sheet = wb.getSheetAt(0);
            setCompName(sheet);
            setWorkbookName(sheet);
            for (Event event : EventsManager.getEvents()) {
                Row event_row = sheet.getRow(getEventRow(event, sheet));
                event.readAdditionalRounds(event_row);
                event.readNumberOfAttempts(event_row);
                event.readHardCutoff(event_row);
                event.readSoftCutoff(event_row);
            }
        }
        catch (Exception e) {
            System.out.println("Error: Configuration file not readable");
            System.exit(0);
        }
    }
    private static int getEventRow(Event event, Sheet sheet) {
        int current_row = 1;
        while (sheet.getRow(current_row).getCell(0) != null) {
            if (sheet.getRow(current_row).getCell(0).getStringCellValue().equals(event.getAbbreviation())) {
                return current_row;
            }
            current_row++;
        }
        System.out.println("unable to find row for event");
        System.exit(1);
        return -1;
    }

    private static void setCompName(Sheet sheet) {
        Main.competition_name = sheet.getRow(22).getCell(2).getStringCellValue();
    }

    private static void setWorkbookName(Sheet sheet) {
        Main.workbook_name = sheet.getRow(24).getCell(2).getStringCellValue();
        System.out.println(Main.workbook_name);
    }
}
