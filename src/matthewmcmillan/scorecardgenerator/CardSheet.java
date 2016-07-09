package matthewmcmillan.scorecardgenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by mmcmillan on 6/13/16.
 */
public class CardSheet {
    public CardSheet(ScoreCard[] cards, int sheet_number) {
        this.cards = cards;
        this.sheet_number = sheet_number;
    }
    private int sheet_number;
    private ScoreCard[] cards = new ScoreCard[4];
    public void generateSheet() {
        try {
            ArrayList<String> new_lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("content_backup.xml"));
            String line;
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < 4; i++) {
                    line = line.replaceAll("COMPETITOR_NAME" + (i + 1), cards[i].getName());
                    line = line.replaceAll("WCAID" + (i + 1), cards[i].getWCAID());
                    line = line.replaceAll("EVENT_NAME" + (i + 1), cards[i].getEvent());
                    line = line.replaceAll("ROUND_NAME" + (i + 1), cards[i].getRound());
                    line = line.replaceAll("HEAT_NAME" + (i + 1), cards[i].getHeat());
                    line = line.replaceAll("COMPETITION_NAME", Main.competition_name);
                }
                new_lines.add(line);
            }
            OutputMethods.writeToSkeleton(new_lines);
            OutputMethods.compressSkeleton();
            OutputMethods.skeletonToPDF();
            OutputMethods.moveToOutput(sheet_number);
        } catch (Exception e) {
            System.out.println("sheet.generate failed...");
        }
    }
}
