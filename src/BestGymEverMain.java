import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BestGymEverMain {
    public static void main(String[] args) {
        //Path till filen f�r personuppgifter, samt till PT:ns loggnings-fil.
        Path customersFilePath = Path.of("src/customers.txt");
        Path bes�kLoggningFilePath = Paths.get("src/bes�kLoggning.txt");
        String inputFr�nAnv�ndaren = null;

        Medlemmar medlemmar = new Medlemmar();
        // While-loop ifall man skriver in fel format p� namn eller personnummer s� f�r man f�rs�ka igen.
        // Eller s� klickar man p� cancel s� avslutas programmet.
        while (true) {
            String[] options = {"Ange namn", "Ange personnummer"};
            int choice = JOptionPane.showOptionDialog(null, "Vad vill du ange?", "Val", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice == JOptionPane.CLOSED_OPTION) {
                break;
            }
            if (choice == 0) {
                inputFr�nAnv�ndaren = JOptionPane.showInputDialog("Ange ditt namn (f�rnamn och efternamn med ett mellanslag):");
            } else if (choice == 1) {
                inputFr�nAnv�ndaren = JOptionPane.showInputDialog("Ange ditt personnummer (10 siffror):");
            }
            if (inputFr�nAnv�ndaren == null) {

                break;
            }
            //Anv�nder try&catch f�r att metoden finnsAnv�ndareISystem har throws IO Exception
            // f�r att kunna hantera Exception tester.
            try {
                // Input skickas vidare till metoden finnsAnv�ndareISystemet ifall input �r korrekt format.(isValidInput)
                if (medlemmar.isValidInput(inputFr�nAnv�ndaren, choice)) {
                    List<String> result = medlemmar.finnsAnv�ndareISystem(inputFr�nAnv�ndaren, customersFilePath);

                    if (result == null) {
                        // ifall personen inte finns med i systemet s� skickas det ut att denne inte �r medlem.
                        System.out.println(MedlemsTyp.ICKE_MEDLEM.MedlemsTypMeddelande);
                    } else {
                        // result listan skickas vidare till vilkenTypAvMedlem metoden, och f�r tillbaka true/false beroende p� medlemstyp.
                        boolean �rNuvarandeMedlem = medlemmar.vilkenTypAvMedlem(result);
                        // ifall det �r en nuvarande medlem s� skickas result till metoden skrivTillFilVidBes�k.
                        if (�rNuvarandeMedlem) {
                            medlemmar.skrivTillFilVidBes�k(result, bes�kLoggningFilePath);
                        }
                    }
                    break;
                } else {
                    String felMeddelande = choice == 0
                            ? "Ogiltig inmatning f�r namn. Ange f�rnamn och efternamn med ett mellanslag."
                            : "Ogiltig inmatning f�r personnummer. Ange 10 siffror.";
                    JOptionPane.showMessageDialog(null, felMeddelande);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Det gick inte att l�sa fr�n filen!");
            }
        }
    }
}