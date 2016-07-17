package matthewmcmillan.scorecardgenerator;

import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;

public class Event {
    public Event(String[] abbreviation_and_name) {
        abbreviation = abbreviation_and_name[0];
        name = abbreviation_and_name[1];
    }
    public Event() {

    }
    private static final int ATTEMPTS_CELL = 1, SOFT_CUTOFF_CELL = 2, HARD_CUTOFF_CELL = 3, ROUND_2_CARDS_CELL = 4,
            ROUND_3_CARDS_CELL = 5, ROUND_4_CARDS_CELL = 6, HEAT_SIZE_CELL = 7;
    private String abbreviation = "";
    private int[] additional_rounds = {0, 0, 0};
    private String attempt_type = "average";
    private String hard_cutoff = "";
    private String name = "";
    private final ArrayList<ScoreCard> scorecards = new ArrayList<>();
    private String soft_cutoff = "";

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
                if (i == 2 || i < 2 && this.additional_rounds[i + 1] == 0) {
                    additonalScoreCards.add(new ScoreCard(this, "Final Round"));
                } else {
                    additonalScoreCards.add(new ScoreCard(this, "Round " + (i + 2)));
                }
            }
        }
        return additonalScoreCards;
    }

    /**
     * The number of attempt_type allowed in the event
     * @return an int with the number of attempt_type for this event
     */
    public String getAttemptType() {
        return attempt_type;
    }

    /**
     * Get the hard cutoff to be displayed on the scorecard
     * @return the hard cutoff that will be displayed (including the text description of it)
     */
    public String getHardCutoff() {
        return hard_cutoff;
    }


    /**
     * Returns the full event name
     * @return the full event name
     */
    public String getName() {
        return name;
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

    /**
     * Set the number of additional rounds and number of cards for each additional round
     * @param additional_rounds an int where each element of the int represents an additional round (so the first
     *                          element is round 2) and the value of each element is the number of cards for that
     *                          round. There must be no more than 3 additional rounds (for four total)
     */
    public void setAdditional_rounds(int[] additional_rounds) {
        this.additional_rounds = additional_rounds;
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
        if (attempt_type == null) {
            System.out.println("Error! Cannot set soft cutoffs until we know number of attempt_type!");
        }
        String spreadsheet_soft_cutoff = event_row.getCell(SOFT_CUTOFF_CELL).getStringCellValue();
        if (!spreadsheet_soft_cutoff.equals("none")) {
            soft_cutoff = "--------must get under " + spreadsheet_soft_cutoff + " to finish " + attempt_type
                    + "--------";
        } else {
            soft_cutoff= "";
        }
    }
    public void readHardCutoff(Row event_row) {
        String spreadsheet_hard_cutoff = event_row.getCell(HARD_CUTOFF_CELL).getStringCellValue();
        if (!spreadsheet_hard_cutoff.equals("none")) {
            hard_cutoff = "Attempts over " + spreadsheet_hard_cutoff + " must be stopped and given a DNF";
        } else {
            hard_cutoff = "";
        }
    }

    public void readNumberOfAttempts(Row event_row) {
        attempt_type = event_row.getCell(ATTEMPTS_CELL).getStringCellValue();
    }

}
