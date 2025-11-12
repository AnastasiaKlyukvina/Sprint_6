package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

        static Object[] firstOrderData() {
            return new Object[]{
                    "Иван",                    // имя
                    "Иванов",                  // фамилия
                    "ул. Ленина, д. 10",       // адрес
                    "+79123456789",            // телефон
                    "01.12.2024",              // дата
                    "сутки",                   // период аренды
                    "black",                   // цвет
                    "Позвонить за час"         // комментарий
            };
        }

        static Object[] secondOrderData() {
            return new Object[]{
                    "Мария",                   // имя
                    "Сидорова",                // фамилия
                    "пр. Мира, д. 25",         // адрес
                    "89098765432",            // телефон
                    "05.12.2024",              // дата
                    "пятеро суток",            // период аренды
                    "grey",                    // цвет
                    "Оставить у двери"         // комментарий
            };
        }

        static Stream<Object[]> orderDataProvider() {
            return Stream.of(firstOrderData(), secondOrderData());
        }

        @ParameterizedTest
        @MethodSource("orderDataProvider")
        @DisplayName("Оформление заказа через верхнюю кнопку")
        public void testOrderFromTopButton(String name, String lastName, String address, String phone,
                                           String date, String period, String color, String comment) {

            mainPage.clickTopOrderButton();
            OrderPage orderPage = new OrderPage(driver);

            orderPage.fillFirstPage(name, lastName, address, phone);

            orderPage.fillSecondPage(date, period, color, comment);

            orderPage.confirmOrder();
        }

        @ParameterizedTest
        @MethodSource("orderDataProvider")
        @DisplayName("Оформление заказа через нижнюю кнопку")
        public void testOrderFromBottomButton(String name, String lastName, String address, String phone,
                                              String date, String period, String color, String comment) {

            mainPage.clickBottomOrderButton();
            OrderPage orderPage = new OrderPage(driver);
            orderPage.fillFirstPage(name, lastName, address, phone);
            orderPage.fillSecondPage(date, period, color, comment);
            orderPage.confirmOrder();

        }

        @ParameterizedTest
        @MethodSource("orderDataProvider")
        @DisplayName("Проверка перехода на страницу заказа")
        public void testNavigateToOrderPage(String name, String lastName, String address, String phone,
                                            String date, String period, String color, String comment) {

            mainPage.clickTopOrderButton();
            OrderPage orderPage = new OrderPage(driver);

            assertTrue(driver.findElement(orderPage.nameField).isDisplayed(),
                    "Поле 'Имя' должно отображаться на странице заказа");
        }

        @AfterEach
        public void tearDown() {
            driver.quit();
        }
    }

