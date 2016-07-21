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
                                {"wcaid", cards[i].getWcaid()},
                                {"event", cards[i].getEvent()},
                                {"round", cards[i].getRound()},
                                {"heat", cards[i].getHeat()},
                                {"hard_cutoff", cards[i].getHardCutoff()},
                                {"ccid", cards[i].getCubecompsId()}
                        }, i
                        );
                if (cards[i].getCubecompsId().equals("")) {
                    new_lines.insertNumbered("ccid_label", "", i);
                } else {
                    new_lines.insertNumbered("ccid_label", "CubeComps ID:", i);
                }
                new_lines.insert("comp_name", ConfigReader.getCompetitionName());
                if (cards[i].isMean()) {
                    new_lines.insertNumbered(
                        new String[][] {
                                {"solve4", ""},
                                {"solve5", ""},
                                {"average_soft_cutoff", ""},
                                {"mean_soft_cutoff", cards[i].getSoftCutoff()}
                        }, i);
                } else {
                    new_lines.insertNumbered(
                            new String[][] {
                                    {"solve4", "Solve 4"},
                                    {"solve5", "Solve 5"},
                                    {"average_soft_cutoff", cards[i].getSoftCutoff()},
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