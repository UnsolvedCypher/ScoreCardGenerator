package matthewmcmillan.scorecardgenerator;

import java.io.BufferedReader;
import java.io.FileReader;

public class CardSheet {
    public CardSheet(ScoreCard[] cards, int sheet_number) {
        this.cards = cards;
        this.sheet_number = sheet_number;
    }
    private final int sheet_number;
    private ScoreCard[] cards = new ScoreCard[4];
    public void generateSheet() {
        try {
            InsertableXMLFile new_lines = new InsertableXMLFile();
            BufferedReader br = new BufferedReader(new FileReader("content_backup.xml"));
            String line;
            while ((line = br.readLine()) != null) {
                new_lines.add(line);
            }
            for (int i = 0; i < 4; i++) {
                new_lines.insertNumbered(
                        new String[][]{
                                {"name", cards[i].getName()},
                                {"wcaid", cards[i].getWCAID()},
                                {"event", cards[i].getEvent().getName()},
                                {"round", cards[i].getRound()},
                                {"heat", cards[i].getHeat()},
                                {"hard_cutoff", cards[i].getEvent().getHardCutoff()},
                                {"ccid", EventsManager.getCubeCompsFromName(cards[i].getName())}
                        }, i
                        );
                new_lines.insert("comp_name", Main.competition_name);
                if (cards[i].getEvent().getAttemptType().equals("mean")) {
                    new_lines.insertNumbered(
                        new String[][] {
                                {"solve4", ""},
                                {"solve5", ""},
                                {"average_soft_cutoff", ""},
                                {"mean_soft_cutoff", cards[i].getEvent().getSoftCutoff()}
                        }, i);
                } else if (cards[i].getEvent().getAttemptType().equals("average")) {
                    new_lines.insertNumbered(
                            new String[][] {
                                    {"solve4", "Solve 4"},
                                    {"solve5", "Solve 5"},
                                    {"average_soft_cutoff", cards[i].getEvent().getSoftCutoff()},
                                    {"mean_soft_cutoff", ""}
                            }, i
                    );
                }
            }
            OutputMethods.writeToSkeleton(new_lines.toStringArray());
            OutputMethods.compressSkeleton();
            OutputMethods.skeletonToPDF();
            OutputMethods.moveToOutput(sheet_number);
        } catch (Exception e) {
            System.out.println("sheet.generate failed..." + e + e.getCause());
        }
    }
}