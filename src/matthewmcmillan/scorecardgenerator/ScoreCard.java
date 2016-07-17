package matthewmcmillan.scorecardgenerator;

/**
 * Created by mmcmillan on 6/13/16.
 */
public class ScoreCard {
    public Event getEvent() {
        return event;
    }

    public ScoreCard(String name, String WCAID, String round, Event event) {
        this.name = name;
        this.WCAID = WCAID;
        this.event = event;
        this.round = round;
    }
    public ScoreCard() {

    }
    public ScoreCard(Event event, String round) {
        this.event = event;
        this.round = round;
    }
    private String round = "";
    private String WCAID = "";
    private Event event = new Event();
    private String name = "";

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    private String heat = "";

    public String getName() {
        return name;
    }


    public String getRound() {
        return round;
    }

    public String getWCAID() {
        return WCAID;
    }
}
