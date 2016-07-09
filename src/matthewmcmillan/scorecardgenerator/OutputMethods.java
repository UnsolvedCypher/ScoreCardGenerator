package matthewmcmillan.scorecardgenerator;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by mmcmillan on 6/13/16.
 */
public class OutputMethods {
    private static void execute(String command) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            int exitVal = pr.waitFor();
            //System.out.println("Exited with error code "+exitVal);
        } catch (Exception e) {
            System.out.println("fuck");
        }
    }

    public static void writeToSkeleton(List<String> lines) {
        try {
            FileWriter writer = new FileWriter("skeleton/content.xml");
            for (String line : lines) {
                writer.write(line);
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("writeToSkeleton failed");
        }
    }
    public static void compressSkeleton() {
        Zip.zipToOdf("skeleton");
    }
    public static void skeletonToPDF() {
        execute("unoconv skeleton.odt");
    }
    public static void moveToOutput(int pdf_number) {
        try {
            Files.deleteIfExists(Paths.get("skeleton.odt"));
            Files.move(Paths.get("skeleton.pdf"), Paths.get("output/" + pdf_number + ".pdf"));
        } catch (Exception e) {
            System.out.println("error in moveToOutput");
        }
    }
    public static void unitePDFs(int number_of_files) {
        String command = "pdfunite ";
        for (int i = 0; i < number_of_files; i++) {
            command += "output/" + i + ".pdf ";
        }
        command += "scorecards.pdf";
        execute(command);
    }
    public static void deleteOutput() {
        try {
            Files.deleteIfExists(Paths.get("output"));
        } catch (Exception e) {
            System.out.println("error deleting output");
        }
    }
    public static void resetAll() {
        try {
            Files.deleteIfExists(Paths.get("output"));
            Files.createDirectory(Paths.get("output"));
            Files.deleteIfExists(Paths.get("heats.txt"));
            Files.deleteIfExists(Paths.get("scorecards.pdf"));
            Files.deleteIfExists(Paths.get("skeleton.odt"));
        } catch (Exception e) {
            System.out.println("Error in resetAll");
        }
    }

}
