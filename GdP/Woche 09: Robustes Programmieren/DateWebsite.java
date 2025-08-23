import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class DateWebsite_L {

    private static String getDateString() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return date.toString();
    }

    private static String generateHTML(String date) {
        // Since Java 15, it is possible to use """ (triple quotes) to create text blocks for Strings that
        // span multiple lines, like this HTML here. Of course, you can still use normal Strings as well
        // and concatenate them with System.lineSeparator():
        // return "<!DOCTYPE html>" + System.lineSeparator()
        //        + "    <body>" + System.lineSeparator()
        //        + "        " + date + System.lineSeparator()
        //        + "    </body>" + date + System.lineSeparator()
        //        + "</html>";

        return """
                <!DOCTYPE html>
                    <body>
                """
                + "        " + date + System.lineSeparator() +
                """
                    </body>
                </html>
                """;
    }

    private static boolean saveHTMLtoFile(String filename, String html) {
        FileWriter file = null;
        try {
            file = new FileWriter(filename);
            file.write(html);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void printContent(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            System.out.println(scanner.nextLine());
        }
    }


    public static void main(String[] args) {
        String date = DateWebsite_L.getDateString();
        String html = DateWebsite_L.generateHTML(date);

        String filename = "date.html";
        boolean success = DateWebsite_L.saveHTMLtoFile(filename, html);

        if (success) {
            System.out.println("The file was written successfully!");
            System.out.println("The following is written in the file:");

            try {
                DateWebsite_L.printContent(filename);
            } catch (FileNotFoundException e) {
                // An unexpected error occurred!
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("An error occurred when writing the file");
        }
    }
}
