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
        assertTrue(output.contains("�r en nuvarande medlem"));


        //�r det en nuvarande medlem?
        assertTrue(isCurrentMember);
    }

    @Test
    public void testF�reDettaMedlem() {
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
        assertTrue(output.contains("�r en f�re detta medlem"));


        //�r det en f�re detta medlem?
        assertTrue(!isCurrentMember);
    }

    @Test
    public void testSkrivTillFilVidBes�k() throws IOException {
    //Skapar en path och en fil f�r att testa
        Path testFilePath = Paths.get("src/test_bes�kLoggning.txt");

        Files.createFile(testFilePath);

        try {
          //L�gger till data till listan som vi testar och sen kallar vi p� metoden skrivTillFilVidBes�k
            List<String> result = Arrays.asList("John Doe", "12345", "2023-10-17");
            medlemmar.skrivTillFilVidBes�k(result, testFilePath);


            try (BufferedReader reader = Files.newBufferedReader(testFilePath)) {
                String line = reader.readLine();
               //L�ser av filen f�r att se ifall korrekt data blev sparat med r�tt format
                //Kan k�ras n�r som helst d� vi anv�nder alltid dagens datum.
                //Resultatet j�mf�rs med det faktiska resultatet
                String expectedLine = "Namn: John Doe, personnummer: 12345, datum f�r tr�ning: " + LocalDate.now();
                assertEquals(expectedLine, line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            //Tar bort test-filen n�r vi �r klara med testet.
            try {
                Files.deleteIfExists(testFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

