package matthewmcmillan.scorecardgenerator;

/**
 * Created by mmcmillan on 6/13/16.
 */
public class ScoreCard {

    public String getEvent() {
        return event;
    }

    public String getSoftCutoff() {
        return soft_cutoff;
    }

    public String getHardCutoff() {
        return hard_cutoff;
    }

    public String getCubecompsId() {
        return cubecomps_id;
    }

    public ScoreCard(String name, String wcaid, String round, String cubecomps_id, String event, String hard_cutoff,
                     String soft_cutoff, boolean mean) {
        this.name = name;
        this.wcaid = wcaid;
        this.round = round;
        this.event = event;
        this.hard_cutoff = hard_cutoff;
        this.soft_cutoff = soft_cutoff;
        this.cubecomps_id = cubecomps_id;
        this.mean = mean;
    }
    public ScoreCard() {

    }
    public ScoreCard(String event, String round, boolean mean) {
        this.event = event;
        this.round = round;
        this.mean = mean;
    }
    private String round = "";
    private String wcaid = "";
    private String name = "";
    private String event = "";
    private String soft_cutoff = "";
    private String hard_cutoff = "";
    private String cubecomps_id = "";
    private boolean mean = false;

    public String getHeat() {
        return heat;
    }
    public boolean isMean() {
        return mean;
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

    public String getWcaid() {
        return wcaid;
    }
}
