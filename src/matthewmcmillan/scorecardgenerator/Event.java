package matthewmcmillan.scorecardgenerator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Event {
    public Event(String abbreviation) {
        this.abbreviation = abbreviation;
        Row event_row = ConfigReader.getEventRow(abbreviation);
        readNumberOfAttempts(event_row);
        readAdditionalRounds(event_row);
        readSoftCutoff(event_row);
        readHardCutoff(event_row);
        name = EventsManager.getEventName(abbreviation);
    }
    private static final int ATTEMPTS_CELL = 1, SOFT_CUTOFF_CELL = 2, HARD_CUTOFF_CELL = 3, ROUND_2_CARDS_CELL = 4,
            ROUND_3_CARDS_CELL = 5, ROUND_4_CARDS_CELL = 6, HEAT_SIZE_CELL = 7;
    private String abbreviation = "";
    private int[] additional_rounds = {0, 0, 0};
    private boolean mean = false;
    private String hard_cutoff = "";
    private final ArrayList<ScoreCard> scorecards = new ArrayList<>();
    private String soft_cutoff = "";
    private String name = "";

    public String getName() {
        return name;
    }


    /**
     * Add a scorecard to this event
     * @param scorecard the scorecard to be added
     */
    public void addCard(ScoreCard scorecard) {
        scorecards.add(scorecard);
    }

    /**
     * Get the abbreviation code for this event
     * @return a String with the abbreviation code
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Get scorecards for additional rounds of this event
     * @return an ArrayList of scorecards for additional rounds of this event
     */
    public ArrayList<ScoreCard> getAdditionalScorecards() {
        ArrayList<ScoreCard> additonalScoreCards = new ArrayList<>();
        for (int i = 0; i < additional_rounds.length; i++) {
            for (int j = 0; j < additional_rounds[i]; j++) {
                if (i == 2 || i < 2 && additional_rounds[i + 1] == 0) {
                    additonalScoreCards.add(new ScoreCard(name, "Final Round", mean));
                } else {
                    additonalScoreCards.add(new ScoreCard(name, "Round " + (i + 2), mean));
                }
            }
        }
        return additonalScoreCards;
    }

    /**
     * The number of attempt_type allowed in the event
     * @return an int with the number of attempt_type for this event
     */
    public boolean isMean() {
        return mean;
    }

    /**
     * Get the hard cutoff to be displayed on the scorecard
     * @return the hard cutoff that will be displayed (including the text description of it)
     */
    public String getHardCutoff() {
        return hard_cutoff;
    }


    /**
     * Returns the first round scorecards for this event
     * @return an ArrayList of scorecards for the first round of the event
     */
    public ArrayList<ScoreCard> getScoreCards() {
        return scorecards;
    }

    /**
     * Returns the soft cutoff as displayed on the scorecard
     * @return the soft cutoff as displayed on the scorecard
     */
    public String getSoftCutoff() {
        return soft_cutoff;
    }


    public void readAdditionalRounds(Row event_row) {
        int[] additional_rounds = new int[3];
        additional_rounds[0] = (int)event_row.getCell(ROUND_2_CARDS_CELL).getNumericCellValue();
        additional_rounds[1] = (int)event_row.getCell(ROUND_3_CARDS_CELL).getNumericCellValue();
        additional_rounds[2] = (int)event_row.getCell(ROUND_4_CARDS_CELL).getNumericCellValue();
        this.additional_rounds = additional_rounds;
    }

    /**
     * Set the numerical soft cutoff
     */
    public void readSoftCutoff(Row event_row) {
        String spreadsheet_soft_cutoff;
        if (event_row.getCell(SOFT_CUTOFF_CELL).getCellType() == Cell.CELL_TYPE_STRING) {
            spreadsheet_soft_cutoff = event_row.getCell(SOFT_CUTOFF_CELL).getStringCellValue();
        } else {
            spreadsheet_soft_cutoff = convertCellToString(event_row.getCell(SOFT_CUTOFF_CELL));
        }
        if (!spreadsheet_soft_cutoff.equals("none")) {
            String attempt_type = mean? "mean" : "average";
            soft_cutoff = "--------must get under " + spreadsheet_soft_cutoff + " to finish " + attempt_type
                    + "--------";
        } else {
            soft_cutoff= "";
        }
    }
    public void readHardCutoff(Row event_row) {
        String spreadsheet_hard_cutoff;
        if (event_row.getCell(HARD_CUTOFF_CELL).getCellType() == Cell.CELL_TYPE_STRING) {
            spreadsheet_hard_cutoff = event_row.getCell(HARD_CUTOFF_CELL).getStringCellValue();
        } else {
            spreadsheet_hard_cutoff = convertCellToString(event_row.getCell(HARD_CUTOFF_CELL));
        }
        if (!spreadsheet_hard_cutoff.equals("none")) {
            hard_cutoff = "Attempts over " + spreadsheet_hard_cutoff + " must be stopped and given a DNF";
        } else {
            hard_cutoff = "";
        }
    }

    public void readNumberOfAttempts(Row event_row) {
        mean = (event_row.getCell(ATTEMPTS_CELL).getNumericCellValue() == 3);
    }
    private String convertCellToString(Cell cell) {
        Date date = cell.getDateCellValue();
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date);
    }

}
