import org.junit.Test;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;


import static org.junit.Assert.assertThrows;

public class ExceptionTest {
    @Test
    public void noSuchFileExceptionTest(){

        Medlemmar medlemmar = new Medlemmar();
        Path testPath = Paths.get("src/ingenfilhär.txt");
        String input = "Evert Glans";

        Throwable exception = assertThrows(NoSuchFileException.class,
                () -> medlemmar.finnsAnvändareISystem(input, testPath));

        }
    }

