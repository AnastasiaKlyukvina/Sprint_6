package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import utils.WebDriverSetup;
import data.FAQData;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class FAQTest {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        driver = WebDriverSetup.getDriver();
        mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.scrollToFAQSection();
    }

    @Test
    public void testFAQSectionIsDisplayed() {
        assertTrue(mainPage.isFAQSectionDisplayed(), "Раздел 'Вопросы о важном' должен отображаться");
        assertEquals(8, mainPage.getQuestionsCount(), "Должно быть 8 вопросов");
    }

    static Stream<Object[]> faqDataProvider() {
        return Stream.of(FAQData.getFAQTestData());
    }

    @ParameterizedTest
    @MethodSource("faqDataProvider")
    public void testFAQAnswersText(int questionNumber, String expectedAnswer) {
        mainPage.clickQuestion(questionNumber);
        boolean isAnswerVisible = mainPage.isAnswerDisplayed(questionNumber);

        if (isAnswerVisible) {
            String actualAnswer = mainPage.getAnswerText(questionNumber);
            assertEquals(expectedAnswer, actualAnswer,
                    "Текст ответа на вопрос " + questionNumber + " должен совпадать");
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}