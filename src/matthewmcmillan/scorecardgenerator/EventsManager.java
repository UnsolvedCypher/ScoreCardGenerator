package matthewmcmillan.scorecardgenerator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class EventsManager {
    private static ArrayList<Event> events = new ArrayList<>();
    public static void generateSheets(String filename) {
        initializeEvents();
        createCards(filename);
        OutputMethods.resetAll();
        if (Main.heat_sizes[0] > 0) {
            setHeats();
        }
        ArrayList<ScoreCard> allCards = new ArrayList<>();
        for (Event event : events) {
            for (ScoreCard card : event.getScoreCards()) {
                allCards.add(card);
            }
            for (ScoreCard card : event.getAdditionalScorecards()) {
                allCards.add(card);
            }
        }
        for (int i = 0; i < Main.blank_scorecards; i++) {
            allCards.add(new ScoreCard());
        }
        // add blank cards if they aren't divisible by four
        while (allCards.size() % 4 != 0) {
            allCards.add(new ScoreCard());
        }
        for (int i = 0; i < allCards.size() / 4; i++) {
            ScoreCard card1 = allCards.get(i);
            ScoreCard card2 = allCards.get(i + (allCards.size() / 4));
            ScoreCard card3 = allCards.get(i + 2 * (allCards.size() / 4));
            ScoreCard card4 = allCards.get(i + 3 * (allCards.size() / 4));
            ScoreCard[] sheet_cards = {card1, card2, card3, card4};
            System.out.println("Generating sheet " + (i + 1));
            new CardSheet(sheet_cards, i).generateSheet();
        }
        System.out.println("Generating PDF...");
        OutputMethods.unitePDFs(allCards.size() / 4);
        OutputMethods.deleteOutput();
        System.out.println("Generated scorecards.pdf");
    }
    private static void setHeats() {
        try {
            FileWriter writer = new FileWriter("heats.txt");
            for (Event event : events) {
                if (!event.getName().equals("7x7 Speedsolve") && !event.getName().equals("6x6 Speedsolve")) {
                    if (event.getScoreCards().size() > 0) {
                        writer.write('\n' + event.getName() + ":\nHeat 1:\n");
                    }
                    int heat_number = 0;
                    int place_in_heat = 0;
                    for (int i = 0; i < event.getScoreCards().size(); i++) {
                        if (place_in_heat == Main.heat_sizes[heat_number % Main.heat_sizes.length]) {
                            place_in_heat = 0;
                            heat_number++;
                            writer.write("\nHeat " + (heat_number + 1) + ":\n");
                        }
                        event.getScoreCards().get(i).setHeat("Heat " + (heat_number + 1));
                        if (place_in_heat != 0) {
                            writer.write(", ");
                        }
                        writer.write(event.getScoreCards().get(i).getName());
                        place_in_heat++;
                    }
                } else {
                    for (ScoreCard card : event.getScoreCards()) {
                        card.setHeat("Heat");
                    }
                }
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("error making heats");
        }
    }
    public static void initializeEvents() {
        events.add(new Event("2x2 Speedsolve"));
        events.add(new Event("3x3 Speedsolve"));
        events.add(new Event("4x4 Speedsolve"));
        events.add(new Event("5x5 Speedsolve"));
        events.add(new Event("6x6 Speedsolve"));
        events.add(new Event("7x7 Speedsolve"));
        events.add(new Event("3x3 Blindfolded"));
        events.add(new Event("4x4 Blindfolded"));
        events.add(new Event("5x5 Blindfolded"));
        events.add(new Event("3x3 One-handed"));
        events.add(new Event("Pyraminx Speedsolve"));
        events.add(new Event("Skewb Speedsolve"));
    }
    private static String getEventFromAbbreviation(String abbreviation) {
        switch(abbreviation) {
            case "2x2":
                return "2x2 Speedsolve";
            case "3x3":
                return "3x3 Speedsolve";
            case "4x4":
                return "4x4 Speedsolve";
            case "5x5":
                return "5x5 Speedsolve";
            case "6x6":
                return "6x6 Speedsolve";
            case "7x7":
                return "7x7 Speedsolve";
            case "skewb":
                return "Skewb Speedsolve";
            case "pyra":
                return "Pyraminx Speedsolve";
            case "333oh":
                return "3x3 One-handed";
            case "333bld":
                return "3x3 Blindfolded";
            case "444bld":
                return "4x4 Blindfolded";
            case "555bld":
                return "5x5 Blindfolded";
            case "feet":
                return "3x3 with Feet";
            case "fmc":
                return "Fewest Move Count";
            default:
                System.out.println("Error: unable to interpret abbreviation \"" + abbreviation + "\"");
                System.exit(1);
                return null;
        }
    }
    public static int getEventIndex(String event_name) {
        for (int i = 0; i < events.size(); i++) {
            if (event_name.equals(events.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }
    private static void identifyColumns(Sheet sheet) {
        int column = 9;
    /* keep looking while cell isn't null or blank */
        while (sheet.getRow(2).getCell(column) != null &&
            sheet.getRow(2).getCell(column).getCellType() != Cell.CELL_TYPE_BLANK) {
            addCardToEvent(sheet, getEventFromAbbreviation(sheet.getRow(2).getCell(column).toString()), column);
            column++;
        }
    }
    public static void createCards(String filename) {
    /* this will need much better error + exception handling later on, probably
       separated into multiple try/catch blocks */
        try {
      /* opens the workbook stored in the file, and takes the first
         sheet. That is the only sheet we will be using */
            Workbook wb = WorkbookFactory.create(new File(filename));
            org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);
            identifyColumns(sheet);
        }
        catch (Exception e) {
            try {
        /* try again by adding xls to the end - maybe they forgot the extension */
                filename += ".xls";
      /* opens the workbook stored in the file, and takes the first
         sheet. That is the only sheet we will be using */
                Workbook wb = WorkbookFactory.create(new File(filename));
                Sheet sheet = wb.getSheetAt(0);
                identifyColumns(sheet);
            }
            catch (Exception f) {
                System.out.println("Error: xls file not found. Please check your spelling and make sure you are running the terminal in the right directory.");
                System.exit(0);
            }
        }
    }
    private static void addCardToEvent(Sheet sheet, String event_name, int eventColumn) {
        int currentRow = 3;
        try {
      /* add competitors that have a "1" in the column eventColumn to the event
         that eventColumn represents
       */
            //TODO: also check other cell, and check for blanks
            while (sheet.getRow(currentRow).getCell(1) != null) {
                //TODO: maybe change to numeric value rather than string?
                if (sheet.getRow(currentRow).getCell(eventColumn).toString().equals("1.0")) {
          /* if the competitor is registered for event, add his/her name */
                    String  name = sheet.getRow(currentRow).getCell(1).toString();
                    String id = sheet.getRow(currentRow).getCell(3) == null ? "" : '(' + sheet.getRow(currentRow)
                            .getCell(3).toString() + ')';
                    events.get(getEventIndex(event_name)).addCard(new ScoreCard(name, id, event_name, "Round 1"));
                }
                currentRow++;
            }
        }
        catch (Exception e) {
            //TODO: why is there an exception???
            //System.out.println("exception in addCardToEvent()");
        }
    }
}