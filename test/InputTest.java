import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputTest {

    Medlemmar medlemmar = new Medlemmar();
    Path customersFilePath = Path.of("src/customers.txt");

    //Testar ifall vi verkligen f�r ett personnummer som result n�r vi plockar ut index 1.
    @Test
    public void testListIndexMedPersonnummer() throws IOException {

        String testStr�ng = "7805211234";
        String testStr�ngFel = "790621281";
        List<String> result = medlemmar.finnsAnv�ndareISystem(testStr�ng, customersFilePath);
        //get(1) f�r tillg�ng till personnummer.
        assert (result.get(1).equals(testStr�ng));
        assert (!result.get(1).equals(testStr�ngFel));
        assert (!result.get(0).equals(testStr�ng));

    }

    @Test
    public void testListIndexMedNamn() throws IOException {
        String testStr�ng = "Nahema Ninsson";
        String testStr�ngFel = "Evert Glans";
        List<String> result = medlemmar.finnsAnv�ndareISystem(testStr�ng, customersFilePath);
        //get(0) f�r tillg�ng till namn.
        assert (result.get(0).equals(testStr�ng));
        assert (!result.get(0).equals(testStr�ngFel));



    }

    @Test
    public void testListIndexMedDatum() throws IOException {
        String testStr�ngInput = "Greger Ganache";
        String r�ttDatum = "2023-03-23";
        String felDatum = "2022-02-22";
        List<String> result = medlemmar.finnsAnv�ndareISystem(testStr�ngInput, customersFilePath);

        assert (result.get(2).equals(r�ttDatum));
        assert (!result.get(2).equals(felDatum));

    }

    @Test
    public void testTypAvMedlem() {

        List<String> test = new ArrayList<>();
        test.add("7512166544");
        test.add("Greger Ganache");
        test.add("2023-03-23");

        assertTrue(medlemmar.vilkenTypAvMedlem(test));


        List<String> test2 = new ArrayList<>();
        test.add("8512126545");
        test.add("Evert Glans");
        test.add("2018-03-12");

        assertFalse(medlemmar.vilkenTypAvMedlem(test2));
    }

    //Testar metoder f�r validering av texten/siffrorna namn/personnummer har korrekt format.

    @Test
    public void testValidName() {
        String validName = "Kris Hansson";
        int choice = 0;
        assertTrue(medlemmar.isValidInput(validName, choice));
    }

    @Test
    public void testInvalidName() {
        String invalidName = "Kris";
        int choice = 0;
        assertFalse(medlemmar.isValidInput(invalidName, choice));
    }


    @Test
    public void testValidPersonnummer() {
        String validPersonnummer = "1234567890";
        int choice = 1;
        assertTrue(medlemmar.isValidInput(validPersonnummer, choice));
    }

    @Test
    public void testInvalidPersonnummer() {
        String invalidPersonnummer = "108372";
        int choice = 1;
        assertFalse(medlemmar.isValidInput(invalidPersonnummer, choice));
    }

}