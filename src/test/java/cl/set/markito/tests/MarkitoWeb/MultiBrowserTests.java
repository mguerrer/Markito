package cl.set.markito.tests.MarkitoWeb;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import cl.set.markito.MarkitoWeb;

public class MultiBrowserTests extends MarkitoWeb {
    @ParameterizedTest
    @CsvSource({
        Browser.Chrome + ", 11",
        Browser.Edge + ", 7"
    })
    void MultiBrowserTest(String browser, int browserVersion) {
        println("Start test on " + browser);

    }
}
