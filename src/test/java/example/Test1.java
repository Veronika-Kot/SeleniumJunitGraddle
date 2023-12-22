package example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import utils.AppProperties;
import utils.Driver;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("smoke")
public class Test1 {

    @BeforeAll
    public static void setupWebdriverChromeDriver() {
        Driver.ResetDriver();
    }

    @AfterAll
    public static void teardown()
    {
        Driver.Quit();
    }

    @Test
    @Order(1)
    @DisplayName("Открыть гланую стриницу")
    @Tag("slow")
    public void verifyGitHubTitle()
    {
        Driver.Navigate(AppProperties.Get("base_url"));
        Driver.CheckTitleContains("GitHub");
    }

    @Test
    @Order(2)
    @DisplayName("При клике на кнопку SignUp, открывается страница регистрации")
    @Tag("slow")
    public void verifyNavigatingToSignUpPage()
    {
        Driver.Click(Driver.FindBy.XPATH, "//*[contains(@href, 'signup')]");
        Driver.CheckTitleContains("Join GitHub");
        Driver.GoBack();
    }

    @Order(3)
    @Tag("smoke")
    @Nested
    class VerifyProductDrawer
    {
        @BeforeEach
        @DisplayName("При клике на кнопку Product, открывается окошко с продуктами")
        public void openDrawer()
        {
            Driver.Navigate(AppProperties.Get("base_url"));
            Driver.ClickAndWaitFor(
                    Driver.FindBy.XPATH,
                    "//button[contains(text(), 'Product')]",
                    Driver.FindBy.XPATH,
                    "//div[contains(@class, 'HeaderMenu-dropdown')]//a[contains(@href, '/actions')]");
        }

        @DisplayName("При нажатии на кнопки окошка Продукты")
        @ParameterizedTest(name = "{index} : кнопка {0} открывает страницу {1}")
        @CsvSource({
                "'//a[contains(@href, '/actions')]', GitHub Actions",
                "'//a[contains(@href, '/packages')]', GitHub Packages:",
                "'//a[contains(@href, '/security')]', Security · GitHub"
        })
        public void verifyClickOnProductLink(String button, String pageTitle)
        {
            Driver.Click(Driver.FindBy.XPATH, "//div[contains(@class, 'HeaderMenu-dropdown')]"+button.substring(1, button.length() - 1));
            Driver.CheckTitleContains(pageTitle);
        }
    }
}