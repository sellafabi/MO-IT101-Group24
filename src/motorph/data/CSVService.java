package motorph.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVService {

    static final String SSS_FILE         = "src/motorph/resources/sss.csv";
    static final String PAGIBIG_FILE     = "src/motorph/resources/pagibig.csv";

    public static List<String[]> SSSTable() {
        List<String[]> sssTable = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(SSS_FILE))) {

            br.readLine();
            String sssLine;

            while ((sssLine = br.readLine()) != null) {
                if (!sssLine.trim().isEmpty()) {

                    // Split CSV row while handling quoted values
                    sssTable.add(sssLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                }
            }

        } catch (IOException e) {
            System.out.println("Error: SSS table file not found. Check that sss.csv exists in src/.");
        }
        return sssTable;
    }

    public static List<String[]> pagibigTable() {
        List<String[]> pagibigTable = new ArrayList<>();

        // Load Pag-IBIG contribution table
        try (BufferedReader br = new BufferedReader(new FileReader(PAGIBIG_FILE))) {

            br.readLine();
            String pagibigLine;

            while ((pagibigLine = br.readLine()) != null) {
                if (!pagibigLine.trim().isEmpty()) {

                    // Split CSV row while handling quoted values
                    pagibigTable.add(pagibigLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error: Pag-IBIG table file not found. Check that pagibig.csv exists in src/.");
        }
        return pagibigTable;
    }
}
