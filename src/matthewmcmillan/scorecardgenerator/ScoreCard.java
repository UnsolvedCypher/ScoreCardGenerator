package matthewmcmillan.scorecardgenerator;

/**
 * Created by mmcmillan on 6/13/16.
 */
public class ScoreCard {
    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public ScoreCard(String name, String WCAID, String event, String round) {
        this.name = name;
        this.WCAID = WCAID;
        this.event = event;
        this.round = round;
    }
    public ScoreCard() {

    }
    public ScoreCard(String event, String round) {
        this.event = event;
        this.round = round;
    }
    private String round = "";
    private String WCAID = "";
    private String event = "";
    private String name = "";

    public String getHeat() {
        return this.heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    private String heat = "";

    public String getName() {
        return this.name;
    }


    public String getRound() {
        return this.round;
    }

    public String getWCAID() {
        return this.WCAID;
    }
}
