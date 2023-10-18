import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Medlemmar {
    LocalDate nuvarandeDatum = LocalDate.now();

    public List<String> finnsAnvändareISystem(String input, Path path) throws IOException {
        //Läser alla rader från filen och sparar varje rad i en lista (lines) som typen String.
        List<String> lines = Files.readAllLines(path);

        //Variabel för att hålla reda på vilken rad vi är på i listan.
        int radIndex = 0;

            /*While loop som går igenom listan tills den är slut.
            Eftersom namn och personnummer är separerade i filen med ett komma och mellanslag,
            så delar programmet upp data till en array "parts".

            Datumet tas ut genom att läsa av raden under personnummer & namnet(radIndex +1).
             */
        while (radIndex < lines.size()) {
            String line = lines.get(radIndex);
            String[] parts = line.split(", ");
            String datumRad = lines.get(radIndex + 1).trim();

            if (parts.length >= 2) {
                String personNummer = parts[0].trim();
                String namn = parts[1].trim();

                //Här kollar vi ifall input som användaren skrev in matchar med
                // personnummer eller namn från parts arrayen.
                if (personNummer.equals(input) || namn.equalsIgnoreCase(input)) {

                    //Skapar en ny lista för att spara resultatet ifall vi fick ett
                    //namn eller personnummer som matchade och returnerar det.
                    List<String> result = new ArrayList<>();
                    result.add(namn);
                    result.add(personNummer);
                    result.add(datumRad);
                    return result;
                }
            }
            radIndex += 2;
        }

        //main metoden får emot ett null-värde ifall namn/nr inte stämmer med listan.
        return null;
    }

    public boolean vilkenTypAvMedlem(List<String> result) {
        //En boolean som denna metod returnerar, om det är en nuvarande medlem kommer denna bli true,
        // om det är en före detta så blir den false.
        boolean ärNuvarandeMedlem = false;
        //När vi tar in listan så kollar vi ifall den inte är null samt att det finns 3 "index" på den.
        if (result != null && result.size() == 3) {
            //I denna metod behöver jag bara använda funnetNamn, men i metoden under behöver jag både namn och personnummer.
            String funnetNamn = result.get(0);

            //Parsar datumet så vi kan använda funktioner i LocalDate
            LocalDate avgiftDatum = LocalDate.parse(result.get(2));
            LocalDate datumEttÅrSen = nuvarandeDatum.minus(Period.ofYears(1));
            //Ifall avgiftsDatum är senare än datumEttÅrSen så kommer meddelandet nuvarande medlem visas.
            //Ifall den är före så visas före detta meddelandet.
            if (avgiftDatum.isAfter(datumEttÅrSen)) {
                ärNuvarandeMedlem = true;

                System.out.println(funnetNamn + " " + MedlemsTyp.NUVARANDE_MEDLEM.MedlemsTypMeddelande + ", avgiften betalades senast: " + avgiftDatum);

            } else if
            (avgiftDatum.isBefore(datumEttÅrSen)) {
                System.out.println(funnetNamn + " " + MedlemsTyp.FÖREDETTA_MEDLEM.MedlemsTypMeddelande + ", avgiften betalades senast: " + avgiftDatum);
            }

            return ärNuvarandeMedlem;
        }

        //Returnerar även false om result listan är null eller storleken inte är 3.
        return false;

    }

    public void skrivTillFilVidBesök(List<String> result, Path path) {
        //Checkar av ifall filen besökLoggning redan finns så skapar den inte filen.
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                //Unchecked exception
                throw new RuntimeException(e);
            }
        } else {
            if (result != null && result.size() == 3) {
                String funnetNamn = result.get(0);
                String personNummer = result.get(1);

                //Try with resources
                //Skapar upp en buffered writer. Nya inlägg läggs alltid till och skrivs inte över med hjälp av append.
                try (BufferedWriter skrivaTillFil = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {

                    String samladDataText = "Namn: " + funnetNamn + ", personnummer: "
                            + personNummer + ", datum för träning: " + nuvarandeDatum;

                    //Skriver strängen för samlad data och lägger till en ny rad för varje inlägg.
                    skrivaTillFil.write(samladDataText);
                    skrivaTillFil.newLine();

                    System.out.println("Följande data har blivit loggat till PT:ns fil:\n" + samladDataText);

                    //Exception för att fånga upp ifall man inte kommer åt filen(skrivskyddad)
                } catch (AccessDeniedException e) {
                    System.out.println("Det går inte att skriva till filen(skrivskyddad?)");
                    //IOException för att hantera eventuellt andra fel vid skrivning till filen.
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("FEL: Kunde inte skriva till fil!");
                    //Unchecked exception
                    throw new RuntimeException(e);

                }
            }
        }
    }

    // Får in vilken typ av input det är, 0 för namn och 1 för person-nr.
    // Som sedan skickar input vidare till en annan metod, beroende på om det är 0 eller 1, för att kolla ifall det är rätt format på input.
    // Den blir true om input är korrekt, false om det inte är korrekt.
    public boolean isValidInput(String input, int choice) {
        if (choice == 0) {
            return isValidNamn(input);
        } else if (choice == 1) {
            return isValidPersonnummer(input);
        }
        return false;
    }

    // Metod för att kolla ifall namnet är giltigt a till ö på båda sidor av mellantecken.
    public boolean isValidNamn(String input) {
        return input.matches("^[a-öA-Ö]+\\s[a-öA-Ö]+$");
    }

    // Metod för att kolla ifall personnummer är 10 siffror.
    public boolean isValidPersonnummer(String input) {
        return input.matches("^\\d{10}$");
    }
}