package matthewmcmillan.scorecardgenerator;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public final class EventsManager {
    private EventsManager() {
    }

    private static HashMap<String, String> event_abbreviations = new HashMap<String, String>() {{
        put("2x2", "2x2 Speedsolve");
        put("3x3", "3x3 Speedsolve");
        put("4x4", "4x4 Speedsolve");
        put("5x5", "5x5 Speedsolve");
        put("6x6", "6x6 Speedsolve");
        put("7x7", "7x7 Speedsolve");
        put("333oh", "3x3 One-handed");
        put("333bld", "3x3 Blindfolded");
        put("fmc", "Fewest Move Count");
        put("333ft", "3x3 with Feet");
        put("magic", "Magic");
        put("mmagic", "Master Magic");
        put("sq1", "Square-1");
        put("pyra", "Pyraminx");
        put("mega", "Megaminx");
        put("clock", "Clock Speedsolve");
        put("444bld", "4x4 Blindfolded");
        put("555bld", "5x5 Blindfolded");
        put("333mlt", "3x3 Multi-Blind");
        put("skewb", "Skewb Speedsolve");
    }};
    private static void useShortNames() {
        System.out.println("Competition has a long name; using short event names");
        event_abbreviations = new HashMap<String, String>() {{
                put("2x2", "2x2");
                put("3x3", "3x3");
                put("4x4", "4x4");
                put("5x5", "5x5");
                put("6x6", "6x6");
                put("7x7", "7x7");
                put("333oh", "3x3 OH");
                put("333bld", "3x3 BLD");
                put("fmc", "FMC");
                put("333ft", "3x3 Feet");
                put("magic", "Magic");
                put("mmagic", "Master Magic");
                put("sq1", "Square-1");
                put("pyra", "Pyraminx");
                put("mega", "Megaminx");
                put("clock", "Clock");
                put("444bld", "4x4 BLD");
                put("555bld", "5x5 BLD");
                put("333mlt", "3x3 MBLD");
                put("skewb", "Skewb");
        }};
    }

    public static Sheet sheet;
    private static final ArrayList<Event> events = new ArrayList<>(20);
    //private static HashMap<String, String> event_abbreviations;

    private static void openWorkbook() {
        try {
      /* opens the workbook stored in the file, and takes the first
         sheet. That is the only sheet we will be using */
            Workbook wb = WorkbookFactory.create(new File(ConfigReader.getWorkbookName()));
            sheet = wb.getSheetAt(0);
        } catch (Exception e) {
            System.out.println("Searching for " + ConfigReader.getWorkbookName());
            System.out.println("Error: xls file not found. Please check your spelling and make sure you are " +
                                       "running the terminal in the right directory.");
            System.exit(0);
        }
    }


    public static void generateSheets() {
        ConfigReader.readConfig();
        if (ConfigReader.getCompetitionName().length() >= 30) {
            useShortNames();
        }
        openWorkbook();
        loadCompetitors();
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
        for (int i = 0; i < ConfigReader.getBlankScorecards(); i++) {
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

    private static void loadCompetitors() {
        for (int column = 9; sheet.getRow(2).getCell(column) != null && sheet.getRow(2).getCell(column).getCellType() != Cell
                .CELL_TYPE_BLANK; column++) {
            scanColumn(column);
        }
    }
    private static void scanColumn(int column) {
        String abbreviation = (sheet.getRow(2).getCell(column).getStringCellValue());
        if (!abbreviation.equals("fmc")) {
            Event current_event = new Event(abbreviation);
            for (int row = 3; row < sheet.getPhysicalNumberOfRows(); row++) {
                Row current_row = sheet.getRow(row);
                if (current_row.getCell(column).getNumericCellValue() == 1) {
                    String name = current_row.getCell(1).getStringCellValue();
                    String wcaid = "";
                    if (current_row.getCell(3) != null && current_row.getCell(3).getCellType() != Cell.CELL_TYPE_BLANK) {
                        wcaid = current_row.getCell(3).getStringCellValue();
                    }
                    String cubecomps_id = getCubeCompsFromRow(row);
                    String event = current_event.getName();
                    String hard_cutoff = current_event.getHardCutoff();
                    String soft_cutoff = current_event.getSoftCutoff();
                    boolean mean = current_event.isMean();
                    ScoreCard competitor_card = new ScoreCard(name, wcaid, "Round 1", cubecomps_id, event, hard_cutoff,
                                                              soft_cutoff, mean);
                    current_event.addCard(competitor_card);
                }
            }
            events.add(current_event);
        }
    }

    public static String getEventName(String abbreviation) {
        return event_abbreviations.get(abbreviation);
    }

    private static String getCubeCompsFromRow(int row) {
        String cubecomps_id = Integer.toString(row - 2);
        while (cubecomps_id.length() < 3) {
            cubecomps_id = "0" + cubecomps_id;
        }
        return cubecomps_id;
    }
    private static void setHeats() {
        try {
            FileWriter writer = new FileWriter("heats.txt");
            for (Event event : events) {
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