package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrderPage {
    private WebDriver driver;

    // Локаторы для формы заказа
    private final By nameField = By.xpath("//input[@placeholder='* Имя']");
    private final By lastNameField = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroField = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[text()='Далее']");

    private final By dateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriod = By.className("Dropdown-placeholder");
    private final By rental1Day = By.xpath("//div[text()='сутки']");
    private final By rental5Days = By.xpath("//div[text()='пятеро суток']");
    private final By blackColor = By.id("black");
    private final By greyColor = By.id("grey");
    private final By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//button[contains(text(), 'Заказать')]");

    // Локатор для закрытия календаря
    private final By pageHeader = By.className("Order_Header__BZXOb");

    // Локаторы для подтверждения заказа
    private final By confirmButton = By.xpath("//button[text()='Да']");

    // Локаторы для успешного заказа
    private final By successModal = By.className("Order_Modal__YZ-d3");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillFirstPage(String name, String lastName, String address, String phone) {
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(addressField).sendKeys(address);
        driver.findElement(metroField).click();
        driver.findElement(By.xpath("//button[@value='1']")).click();
        driver.findElement(phoneField).sendKeys(phone);
        driver.findElement(nextButton).click();
    }

    public void fillSecondPage(String date, String period, String color, String comment) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateField));

        driver.findElement(dateField).sendKeys(date);
        closeCalendar();
        selectRentalPeriod(period);

        if (color.equals("black")) {
            driver.findElement(blackColor).click();
        } else {
            driver.findElement(greyColor).click();
        }

        driver.findElement(commentField).sendKeys(comment);
        driver.findElement(orderButton).click();
    }

    private void closeCalendar() {
        try {
            WebElement header = driver.findElement(pageHeader);
            header.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            // Если не получилось, пробуем альтернативный способ
            try {
                WebElement body = driver.findElement(By.tagName("body"));
                body.click();
                Thread.sleep(1000);
            } catch (Exception ex) {
                // Продолжаем выполнение
            }
        }
    }

    private void selectRentalPeriod(String period) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(rentalPeriod));
            driver.findElement(rentalPeriod).click();
        } catch (Exception e) {
            WebElement rentalElement = driver.findElement(rentalPeriod);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", rentalElement);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(rental1Day));

        if (period.equals("сутки")) {
            driver.findElement(rental1Day).click();
        } else {
            driver.findElement(rental5Days).click();
        }
    }

    public void confirmOrder() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        driver.findElement(confirmButton).click();
    }

    public boolean isOrderSuccessfullyCreated() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(successModal));
            return driver.findElement(successModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}