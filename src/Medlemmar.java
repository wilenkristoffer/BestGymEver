import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Medlemmar {
    LocalDate nuvarandeDatum = LocalDate.now();

    public List<String> finnsAnv�ndareISystem(String input, Path path) throws IOException {
        //L�ser alla rader fr�n filen och sparar varje rad i en lista (lines) som typen String.
        List<String> lines = Files.readAllLines(path);

        //Variabel f�r att h�lla reda p� vilken rad vi �r p� i listan.
        int radIndex = 0;

            /*While loop som g�r igenom listan tills den �r slut.
            Eftersom namn och personnummer �r separerade i filen med ett komma och mellanslag,
            s� delar programmet upp data till en array "parts".

            Datumet tas ut genom att l�sa av raden under personnummer & namnet(radIndex +1).
             */
        while (radIndex < lines.size()) {
            String line = lines.get(radIndex);
            String[] parts = line.split(", ");
            String datumRad = lines.get(radIndex + 1).trim();

            if (parts.length >= 2) {
                String personNummer = parts[0].trim();
                String namn = parts[1].trim();

                //H�r kollar vi ifall input som anv�ndaren skrev in matchar med
                // personnummer eller namn fr�n parts arrayen.
                if (personNummer.equals(input) || namn.equalsIgnoreCase(input)) {

                    //Skapar en ny lista f�r att spara resultatet ifall vi fick ett
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

        //main metoden f�r emot ett null-v�rde ifall namn/nr inte st�mmer med listan.
        return null;
    }

    public boolean vilkenTypAvMedlem(List<String> result) {
        //En boolean som denna metod returnerar, om det �r en nuvarande medlem kommer denna bli true,
        // om det �r en f�re detta s� blir den false.
        boolean �rNuvarandeMedlem = false;
        //N�r vi tar in listan s� kollar vi ifall den inte �r null samt att det finns 3 "index" p� den.
        if (result != null && result.size() == 3) {
            //I denna metod beh�ver jag bara anv�nda funnetNamn, men i metoden under beh�ver jag b�de namn och personnummer.
            String funnetNamn = result.get(0);

            //Parsar datumet s� vi kan anv�nda funktioner i LocalDate
            LocalDate avgiftDatum = LocalDate.parse(result.get(2));
            LocalDate datumEtt�rSen = nuvarandeDatum.minus(Period.ofYears(1));
            //Ifall avgiftsDatum �r senare �n datumEtt�rSen s� kommer meddelandet nuvarande medlem visas.
            //Ifall den �r f�re s� visas f�re detta meddelandet.
            if (avgiftDatum.isAfter(datumEtt�rSen)) {
                �rNuvarandeMedlem = true;

                System.out.println(funnetNamn + " " + MedlemsTyp.NUVARANDE_MEDLEM.MedlemsTypMeddelande + ", avgiften betalades senast: " + avgiftDatum);

            } else if
            (avgiftDatum.isBefore(datumEtt�rSen)) {
                System.out.println(funnetNamn + " " + MedlemsTyp.F�REDETTA_MEDLEM.MedlemsTypMeddelande + ", avgiften betalades senast: " + avgiftDatum);
            }

            return �rNuvarandeMedlem;
        }

        //Returnerar �ven false om result listan �r null eller storleken inte �r 3.
        return false;

    }

    public void skrivTillFilVidBes�k(List<String> result, Path path) {
        //Checkar av ifall filen bes�kLoggning redan finns s� skapar den inte filen.
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
                //Skapar upp en buffered writer. Nya inl�gg l�ggs alltid till och skrivs inte �ver med hj�lp av append.
                try (BufferedWriter skrivaTillFil = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {

                    String samladDataText = "Namn: " + funnetNamn + ", personnummer: "
                            + personNummer + ", datum f�r tr�ning: " + nuvarandeDatum;

                    //Skriver str�ngen f�r samlad data och l�gger till en ny rad f�r varje inl�gg.
                    skrivaTillFil.write(samladDataText);
                    skrivaTillFil.newLine();

                    System.out.println("F�ljande data har blivit loggat till PT:ns fil:\n" + samladDataText);

                    //Exception f�r att f�nga upp ifall man inte kommer �t filen(skrivskyddad)
                } catch (AccessDeniedException e) {
                    System.out.println("Det g�r inte att skriva till filen(skrivskyddad?)");
                    //IOException f�r att hantera eventuellt andra fel vid skrivning till filen.
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("FEL: Kunde inte skriva till fil!");
                    //Unchecked exception
                    throw new RuntimeException(e);

                }
            }
        }
    }

    // F�r in vilken typ av input det �r, 0 f�r namn och 1 f�r person-nr.
    // Som sedan skickar input vidare till en annan metod, beroende p� om det �r 0 eller 1, f�r att kolla ifall det �r r�tt format p� input.
    // Den blir true om input �r korrekt, false om det inte �r korrekt.
    public boolean isValidInput(String input, int choice) {
        if (choice == 0) {
            return isValidNamn(input);
        } else if (choice == 1) {
            return isValidPersonnummer(input);
        }
        return false;
    }

    // Metod f�r att kolla ifall namnet �r giltigt a till � p� b�da sidor av mellantecken.
    public boolean isValidNamn(String input) {
        return input.matches("^[a-�A-�]+\\s[a-�A-�]+$");
    }

    // Metod f�r att kolla ifall personnummer �r 10 siffror.
    public boolean isValidPersonnummer(String input) {
        return input.matches("^\\d{10}$");
    }
}