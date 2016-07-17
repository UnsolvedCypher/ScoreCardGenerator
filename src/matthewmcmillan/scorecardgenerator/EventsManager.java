package matthewmcmillan.scorecardgenerator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class EventsManager {
    private static final ArrayList<Event> events = new ArrayList<>();
    private static ArrayList<String> cubingUSA_ids;
    private static final String[][] possible_events = {
            {"2x2", "2x2 Speedsolve"},
            {"3x3", "3x3 Speedsolve"},
            {"4x4", "4x4 Speedsolve"},
            {"5x5", "5x5 Speedsolve"},
            {"6x6", "6x6 Speedsolve"},
            {"7x7", "7x7 Speedsolve"},
            {"333oh", "3x3 One-handed"},
            {"333bld", "3x3 Blindfolded"},
            {"fmc", "Fewest Move Count"},
            {"333ft", "3x3 with Feet"},
            {"magic", "Magic"},
            {"mmagic", "Master Magic"},
            {"sq1", "Square-1 Speedsolve"},
            {"pyra", "Pyraminx Speedsolve"},
            {"mega", "Megaminx Speedsolve"},
            {"clock", "Clock Speedsolve"},
            {"444bld", "4x4 Blindfolded"},
            {"555bld", "5x5 Blindfolded"},
            {"333mlt", "3x3 Multiple Blindfolded"},
            {"skewb", "Skewb Speedsolve"},

    };

    public static void createCards() {
    /* this will need much better error + exception handling later on, probably
       separated into multiple try/catch blocks */
        try {
      /* opens the workbook stored in the file, and takes the first
         sheet. That is the only sheet we will be using */
            Workbook wb = WorkbookFactory.create(new File(Main.workbook_name));
            Sheet sheet = wb.getSheetAt(0);
            EventsManager.identifyColumns(sheet);
        } catch (Exception e) {
                System.out.println("Searching for " + Main.workbook_name);
                System.out.println("Error: xls file not found. Please check your spelling and make sure you are running the terminal in the right directory.");
                System.exit(0);
        }
    }

    public static void generateSheets() {
        initializeEvents();
        Main.conf.readConfig();
        createCards();
        OutputMethods.resetAll();
        if (Main.heat_sizes[0] > 0) {
            setHeats();
        }
        ArrayList<ScoreCard> allCards = new ArrayList<>();
        for (Event event : EventsManager.events) {
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
        // add blank cards until the total is divisible by four
        while (allCards.size() % 4 != 0) {
            allCards.add(new ScoreCard());
        }
        int quarter_size = allCards.size() / 4;
        for (int i = 0; i < allCards.size() / 4; i++) {
            ScoreCard card1 = allCards.get(i);
            ScoreCard card2 = allCards.get(i + quarter_size);
            ScoreCard card3 = allCards.get(i + 2 * quarter_size);
            ScoreCard card4 = allCards.get(i + 3 * quarter_size);
            ScoreCard[] sheet_cards = {card1, card2, card3, card4};
            if (card1 == null || card2 == null || card3 == null || card4 == null) {
                System.out.println("oh noes! There's a null card!!!");
            }
            System.out.println("Generating sheet " + (i + 1));
            new CardSheet(sheet_cards, i).generateSheet();
        }
        System.out.println("Generating PDF...");
        OutputMethods.unitePDFs(allCards.size() / 4);
        OutputMethods.deleteOutput();
        System.out.println("Generated scorecards.pdf");
    }

    public static int getEventIndex(String event_name) {
        for (int i = 0; i < EventsManager.events.size(); i++) {
            if (event_name.equals(EventsManager.events.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

    public static void initializeEvents() {
        for (String[] event_names : possible_events) {
            events.add(new Event(event_names));
        }
    }

    private static void addCardToEvent(Sheet sheet, Event event, int eventColumn) {
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
                    String name = sheet.getRow(currentRow).getCell(1).toString();
                    String id = sheet.getRow(currentRow).getCell(3) == null ? "" : '(' + sheet.getRow(currentRow)
                            .getCell(3).toString() + ')';
                    events.get(getEventIndex(event.getName())).addCard(new ScoreCard(name, id,
                                                                                                            "Round " +
                                                                                                                    "1", event));
                }
                currentRow++;
            }
        } catch (Exception e) {
            //TODO: why is there an exception???
            //System.out.println("exception in addCardToEvent()");
        }
    }

    private static String getEventFromAbbreviation(String abbreviation) {
        for (String[] name_abbr_pair : EventsManager.possible_events) {
            if (name_abbr_pair[0].equals(abbreviation)) {
                return name_abbr_pair[1];
            }
        }
        System.out.println("Error: unable to interpret abbreviation \"" + abbreviation + "\"");
        System.exit(1);
        return null;
    }

    public static ArrayList<Event> getEvents() {
        return EventsManager.events;
    }

    private static void identifyColumns(Sheet sheet) {
        int column = 9;
    /* keep looking while cell isn't null or blank */
        while (sheet.getRow(2).getCell(column) != null &&
                sheet.getRow(2).getCell(column).getCellType() != Cell.CELL_TYPE_BLANK) {
            EventsManager.addCardToEvent(sheet, events.get(getEventIndex(getEventFromAbbreviation(sheet.getRow(2)
                                                                                                          .getCell(column)
                                                                                      .toString()))), column);
            column++;
        }
    }
    private static void initializeCubingUSAIDs(Sheet sheet) {
        
    }
    private static void setHeats() {
        try {
            FileWriter writer = new FileWriter("heats.txt");
            for (Event event : EventsManager.events) {
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
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("error making heats");
        }
    }

}