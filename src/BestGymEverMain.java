import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BestGymEverMain {
    public static void main(String[] args) {
        //Path till filen för personuppgifter, samt till PT:ns loggnings-fil.
        Path customersFilePath = Path.of("src/customers.txt");
        Path besökLoggningFilePath = Paths.get("src/besökLoggning.txt");
        String inputFrånAnvändaren = null;

        Medlemmar medlemmar = new Medlemmar();
        // While-loop ifall man skriver in fel format på namn eller personnummer så får man försöka igen.
        // Eller så klickar man på cancel så avslutas programmet.
        while (true) {
            String[] options = {"Ange namn", "Ange personnummer"};
            int choice = JOptionPane.showOptionDialog(null, "Vad vill du ange?", "Val", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice == JOptionPane.CLOSED_OPTION) {
                break;
            }
            if (choice == 0) {
                inputFrånAnvändaren = JOptionPane.showInputDialog("Ange ditt namn (förnamn och efternamn med ett mellanslag):");
            } else if (choice == 1) {
                inputFrånAnvändaren = JOptionPane.showInputDialog("Ange ditt personnummer (10 siffror):");
            }
            if (inputFrånAnvändaren == null) {

                break;
            }
            //Använder try&catch för att metoden finnsAnvändareISystem har throws IO Exception
            // för att kunna hantera Exception tester.
            try {
                // Input skickas vidare till metoden finnsAnvändareISystemet ifall input är korrekt format.(isValidInput)
                if (medlemmar.isValidInput(inputFrånAnvändaren, choice)) {
                    List<String> result = medlemmar.finnsAnvändareISystem(inputFrånAnvändaren, customersFilePath);

                    if (result == null) {
                        // ifall personen inte finns med i systemet så skickas det ut att denne inte är medlem.
                        System.out.println(MedlemsTyp.ICKE_MEDLEM.MedlemsTypMeddelande);
                    } else {
                        // result listan skickas vidare till vilkenTypAvMedlem metoden, och får tillbaka true/false beroende på medlemstyp.
                        boolean ärNuvarandeMedlem = medlemmar.vilkenTypAvMedlem(result);
                        // ifall det är en nuvarande medlem så skickas result till metoden skrivTillFilVidBesök.
                        if (ärNuvarandeMedlem) {
                            medlemmar.skrivTillFilVidBesök(result, besökLoggningFilePath);
                        }
                    }
                    break;
                } else {
                    String felMeddelande = choice == 0
                            ? "Ogiltig inmatning för namn. Ange förnamn och efternamn med ett mellanslag."
                            : "Ogiltig inmatning för personnummer. Ange 10 siffror.";
                    JOptionPane.showMessageDialog(null, felMeddelande);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Det gick inte att läsa från filen!");
            }
        }
    }
}