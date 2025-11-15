package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.OrderPage;
import utils.WebDriverSetup;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        driver = WebDriverSetup.getDriver();
        mainPage = new MainPage(driver);
        mainPage.open();
    }

    // Первый набор данных
    static Object[] firstOrderData() {
        return new Object[]{
                "Иван", "Иванов", "ул. Ленина, д. 10", "+79123456789",
                "01.12.2025", "сутки", "black", "Позвонить за час"
        };
    }

    // Второй набор данных
    static Object[] secondOrderData() {
        return new Object[]{
                "Мария", "Сидорова", "пр. Мира, д. 25", "+79098765432",
                "05.12.2025", "пятеро суток", "grey", "Оставить у двери"
        };
    }

    static Stream<Object[]> orderDataProvider() {
        return Stream.of(firstOrderData(), secondOrderData());
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    public void testOrderFromTopButton(String name, String lastName, String address, String phone,
                                       String date, String period, String color, String comment) {
        mainPage.clickTopOrderButton();
        OrderPage orderPage = new OrderPage(driver);
        orderPage.fillFirstPage(name, lastName, address, phone);
        orderPage.fillSecondPage(date, period, color, comment);
        orderPage.confirmOrder();
        assertTrue(orderPage.isOrderSuccessfullyCreated(),
                "После подтверждения заказа должно появиться сообщение об успехе");
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    public void testOrderFromBottomButton(String name, String lastName, String address, String phone,
                                          String date, String period, String color, String comment) {
        mainPage.clickBottomOrderButton();
        OrderPage orderPage = new OrderPage(driver);
        orderPage.fillFirstPage(name, lastName, address, phone);
        orderPage.fillSecondPage(date, period, color, comment);
        orderPage.confirmOrder();
        assertTrue(orderPage.isOrderSuccessfullyCreated(),
                "После подтверждения заказа должно появиться сообщение об успехе");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}