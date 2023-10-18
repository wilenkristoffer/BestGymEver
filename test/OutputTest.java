import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OutputTest {

    Medlemmar medlemmar = new Medlemmar();


    @Test
    public void testNuvarandeMedlem() {
        List<String> result = new ArrayList<>();
        result.add("Evert Glans");
        result.add("1234567890");
        result.add("2023-07-16");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        boolean isCurrentMember = medlemmar.vilkenTypAvMedlem(result);

        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("Evert Glans"));
        assertTrue(output.contains("avgiften betalades senast: 2023-07-16"));
        assertTrue(output.contains("är en nuvarande medlem"));


        //Är det en nuvarande medlem?
        assertTrue(isCurrentMember);
    }

    @Test
    public void testFöreDettaMedlem() {
        List<String> result = new ArrayList<>();
        result.add("Evert Glans");
        result.add("1234567890");
        result.add("2021-06-26");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        boolean isCurrentMember = medlemmar.vilkenTypAvMedlem(result);

        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("Evert Glans"));
        assertTrue(output.contains("avgiften betalades senast: 2021-06-26"));
        assertTrue(output.contains("är en före detta medlem"));


        //Är det en före detta medlem?
        assertTrue(!isCurrentMember);
    }

    @Test
    public void testSkrivTillFilVidBesök() throws IOException {
    //Skapar en path och en fil för att testa
        Path testFilePath = Paths.get("src/test_besökLoggning.txt");

        Files.createFile(testFilePath);

        try {
          //Lägger till data till listan som vi testar och sen kallar vi på metoden skrivTillFilVidBesök
            List<String> result = Arrays.asList("John Doe", "12345", "2023-10-17");
            medlemmar.skrivTillFilVidBesök(result, testFilePath);


            try (BufferedReader reader = Files.newBufferedReader(testFilePath)) {
                String line = reader.readLine();
               //Läser av filen för att se ifall korrekt data blev sparat med rätt format
                //Kan köras när som helst då vi använder alltid dagens datum.
                //Resultatet jämförs med det faktiska resultatet
                String expectedLine = "Namn: John Doe, personnummer: 12345, datum för träning: " + LocalDate.now();
                assertEquals(expectedLine, line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            //Tar bort test-filen när vi är klara med testet.
            try {
                Files.deleteIfExists(testFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

