import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by jdavi on 3/17/17.
 */

@RunWith(Cucumber.class)
@CucumberOptions(format = {"pretty","html:reports/test-report"}
                )

public class CucumberRunner {
}
