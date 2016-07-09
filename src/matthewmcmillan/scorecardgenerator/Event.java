package matthewmcmillan.scorecardgenerator;

import java.util.ArrayList;
import java.util.Scanner;

public class Event {
    public Event(String name) {
        this.name = name;
        Scanner scanner = new Scanner(System.in);
        boolean ask_next_round = true;
        for (int i = 0; i < 3; i++) {
            if (ask_next_round) {
                System.out.print("How many round " + (i + 2) + " cards for " + name + "? ");
                additional_rounds[i] = scanner.nextInt();
                if (additional_rounds[i] == 0) {
                    ask_next_round = false;
                }
            }
        }
    }
    public String getName() {
        return this.name;
    }

    private int[] additional_rounds = new int[3];
    private String name;
    private ArrayList<ScoreCard> scorecards = new ArrayList<>();
    public void addCard(ScoreCard scorecard) {
        scorecards.add(scorecard);
    }
    public ArrayList<ScoreCard> getAdditionalScorecards() {
        ArrayList<ScoreCard> additonalScoreCards = new ArrayList<>();
        for (int i = 0; i < additional_rounds.length; i++) {
            for (int j = 0; j < additional_rounds[i]; j++) {
                if (i == 2 || (i < 2 && additional_rounds[i + 1] == 0)) {
                    additonalScoreCards.add(new ScoreCard(name, "Final Round"));
                } else {
                    additonalScoreCards.add(new ScoreCard(name, "Round " + (i + 2)));
                }
            }
        }
        return additonalScoreCards;
    }

    public ArrayList<ScoreCard> getScoreCards() {
        return scorecards;
    }
}
