package utils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

final public class Driver {
    public enum FindBy
    {
        XPATH,
        ID,
        CLASS,
        NAME,
        LINKTEXT,
        TAG,
        CSS
    }
    private static WebDriver _webDriver;
    private static FluentWait<WebDriver> wait;
    private Driver() {}

    private static void SetupBrowser()
    {
        switch(AppProperties.Get("browser")){
            case "1":
            {
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/main/resources/chromedriver");
                _webDriver = new ChromeDriver();
                break;
            }
            case "2":
            {
                _webDriver = new FirefoxDriver();
                break;
            }
        }

        wait = new FluentWait<WebDriver>(_webDriver);
        //Specify the timout of the wait
        wait.withTimeout(Duration.ofMillis(Long.parseLong(AppProperties.Get("timeout"))));
        //Specify polling time
        wait.pollingEvery(Duration.ofMillis(Long.parseLong(AppProperties.Get("poll"))));
        //Specify what exceptions to ignore
        wait.ignoring(NoSuchElementException.class);
    }

    private static By GetSeleniumBy(FindBy by, String key)
    {
        switch(by)
        {
            case XPATH -> {
                return By.xpath(key);
            }
            case ID -> {
                return By.id(key);
            }
            case CLASS -> {
                return By.className(key);
            }
            case NAME -> {
                return By.name(key);
            }
            case LINKTEXT -> {
                return By.linkText(key);
            }
            case TAG -> {
                return By.tagName(key);
            }
            case CSS -> {
                return By.cssSelector(key);
            }
        }
        return null;
    }

    private static WebElement GetElement(By by)
    {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return GetDriver().findElement(by);
    }

    private static List<WebElement> GetElements(By by)
    {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return GetDriver().findElements(by);
    }

    private static Select GetSelectElement(By by)
    {
        return new Select(GetElement(by));
    }

    //*PUBLIC METHODS*/

    public static void ResetDriver()
    {
        if(_webDriver == null)
        {
            SetupBrowser();
        }
        else
        {
            switch(AppProperties.Get("browser")){
                case "1":
                {
                    _webDriver = new ChromeDriver();
                    break;
                }
                case "2":
                {
                    _webDriver = new FirefoxDriver();
                    break;
                }
            }
        }
    }

    public static WebDriver GetDriver()
    {
        if (_webDriver == null)
        {
            SetupBrowser();
        }

        return _webDriver;
    }

    public static void Quit()
    {
        if (_webDriver!=null)
        {
            _webDriver.quit();
        }
    }

    public static void Navigate(String url)
    {
        GetDriver().get(url);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//title")));
    }

    public static void GoBack()
    {
        GetDriver().navigate().back();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//title")));
    }

    public static void GoBackToPage(String title)
    {
        GetDriver().navigate().back();
        wait.until(ExpectedConditions.titleIs(title));
    }

    public static void CheckTitleContains(String title)
    {
        wait.until(ExpectedConditions.titleContains(title));
    }

    public static void CheckTitleEqual(String title)
    {
        wait.until(ExpectedConditions.titleIs(title));
    }

    //* ELEMENTS */
    public static void Click(FindBy by, String key)
    {
        wait.until(ExpectedConditions.elementToBeClickable(GetSeleniumBy(by, key)));
        GetElement(GetSeleniumBy(by, key)).click();
    }

    public static void ClickAndWaitFor(FindBy byForClick, String keyToClick, FindBy byForWait, String keyToWait)
    {
        wait.until(ExpectedConditions.elementToBeClickable(GetSeleniumBy(byForClick, keyToClick)));
        GetElement(GetSeleniumBy(byForClick, keyToClick)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(GetSeleniumBy(byForWait, keyToWait)));
    }

    public static void SendText(FindBy by, String key, String text)
    {
        WebElement el = GetElement(GetSeleniumBy(by, key));
        if (el.isEnabled())
        {
            el.sendKeys(text);
        }
        else
        {
            throw new ElementNotInteractableException("Элемент "+key+" не активен");
        }
    }

    public static String GetText(FindBy by, String key)
    {
        return GetElement(GetSeleniumBy(by, key)).getText();
    }

    public static String GetAttribute(FindBy by, String key, String attr)
    {
        return GetElement(GetSeleniumBy(by, key)).getAttribute(attr);
    }

    //* KEYS */
    public static void SendSubmit(FindBy by, String key)
    {
        WebElement el = GetElement(GetSeleniumBy(by, key));
        if (el.isEnabled())
        {
            el.sendKeys(Keys.RETURN);
        }
        else
        {
            throw new ElementNotInteractableException("Элемент "+key+" не активен");
        }
    }

    public static void SendTab(FindBy by, String key)
    {
        WebElement el = GetElement(GetSeleniumBy(by, key));
        if (el.isEnabled())
        {
            el.sendKeys(Keys.TAB);
        }
        else
        {
            throw new ElementNotInteractableException("Элемент "+key+" не активен");
        }
    }

    //* Select Element*/
    public static void ToggleSelectByIndex(boolean select, FindBy byParent, String keyParent, int index)
    {
        if (select)
        {
            GetSelectElement(GetSeleniumBy(byParent, keyParent)).selectByIndex(index);
        }
        else
        {
            GetSelectElement(GetSeleniumBy(byParent, keyParent)).deselectByIndex(index);
        }
    }

    public static void ToggleSelectByValue(boolean select, FindBy byParent, String keyParent, String value)
    {
        if (select)
        {
            GetSelectElement(GetSeleniumBy(byParent, keyParent)).selectByValue(value);
        }
        else
        {
            GetSelectElement(GetSeleniumBy(byParent, keyParent)).deselectByValue(value);
        }
    }

    public static void ToggleSelectByVisibleText(boolean select, FindBy byParent, String keyParent, String text)
    {
        if (select)
        {
            GetSelectElement(GetSeleniumBy(byParent, keyParent)).selectByVisibleText(text);
        }
        else
        {
            GetSelectElement(GetSeleniumBy(byParent, keyParent)).deselectByVisibleText(text);
        }
    }

    public static void ToggleAllElementsInSelect(boolean selectAll, FindBy byParent, String keyParent)
    {
        if (selectAll)
        {
            Select select = GetSelectElement(GetSeleniumBy(byParent, keyParent));
            select.deselectAll();
            List<WebElement> options = select.getOptions();

            options.forEach(wl -> {
                select.selectByVisibleText(wl.getText());
            });
        }
        else
        {
            GetSelectElement(GetSeleniumBy(byParent, keyParent)).deselectAll();
        }
    }

    public static List<WebElement> GetAllSelectedOptions(FindBy byParent, String keyParent)
    {
        return GetSelectElement(GetSeleniumBy(byParent, keyParent)).getAllSelectedOptions();
    }

    public static boolean AllowMultiple(FindBy byParent, String keyParent)
    {
        return GetSelectElement(GetSeleniumBy(byParent, keyParent)).isMultiple();
    }

    //** POPUPS*/

    public static void SwitchToFrame(String locator)
    {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public static void SwitchToMainFrame()
    {
        GetDriver().switchTo().defaultContent();
    }

    public static void SwitchToAlert()
    {
        wait.until(ExpectedConditions.alertIsPresent());
        GetDriver().switchTo().alert().accept();
    }

    public static String switchTab()
    {
        String originalWindow = GetDriver().getWindowHandle();
        for (String windowHandle : GetDriver().getWindowHandles()) {
            if(!originalWindow.contentEquals(windowHandle)) {
                GetDriver().switchTo().window(windowHandle);
                break;
            }
        }
        return originalWindow;
    }

    public static void switchTab(String windowID)
    {
        GetDriver().switchTo().window(windowID);
    }
}
