package matthewmcmillan.scorecardgenerator;

import java.util.ArrayList;

public class InsertableXMLFile {

    private ArrayList<String> xml_contents = new ArrayList<String>(0);
    public void insert(String placeholder, String to_insert) {
        for (int i = 0; i < xml_contents.size(); i++) {
            xml_contents.set(i, xml_contents.get(i).replaceAll(placeholder, to_insert));
        }
    }
    public void insertNumbered(String placeholder, String to_insert, int num) {
        for (int i = 0; i < xml_contents.size(); i++) {
            xml_contents.set(i, xml_contents.get(i).replaceAll(placeholder + num, to_insert));
        }
    }
    public ArrayList<String> toStringArray() {
        return xml_contents;
    }

    public void insert(String[][] insertions) {
        for (String[] pair : insertions) {
            insert(pair[0], pair[1]);
        }
    }
    public void insertNumbered(String[][] insertions, int num) {
        for (String[] pair : insertions) {
            insertNumbered(pair[0], pair[1], num);
        }
    }
    public void add(String line) {
        xml_contents.add(line);
    }
}
