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
    public void testAllFAQAnswersText(int questionNumber, String expectedAnswer) {
        mainPage.clickQuestion(questionNumber);

        assertTrue(mainPage.isAnswerDisplayed(questionNumber),
                "Ответ на вопрос " + questionNumber + " должен отображаться");

        String actualAnswer = mainPage.getAnswerText(questionNumber);

        assertEquals(expectedAnswer, actualAnswer,
                "Текст ответа на вопрос " + questionNumber + " должен совпадать");
    }

    @ParameterizedTest
    @MethodSource("faqDataProvider")
    public void testOnlyOneAnswerOpenAtTime(int questionNumber, String expectedAnswer) {

        mainPage.clickQuestion(1);
        assertTrue(mainPage.isAnswerDisplayed(1), "Первый ответ должен быть виден");
        mainPage.clickQuestion(questionNumber);
        assertTrue(mainPage.isAnswerDisplayed(questionNumber),
                "Ответ на вопрос " + questionNumber + " должен быть виден");

        if (questionNumber != 1) {
            assertFalse(mainPage.isAnswerDisplayed(1),
                    "Первый ответ должен закрыться после клика на вопрос " + questionNumber);
        }

        String actualAnswer = mainPage.getAnswerText(questionNumber);
        assertEquals(expectedAnswer, actualAnswer,
                "Текст ответа на вопрос " + questionNumber + " должен совпадать");
    }

    @Test
    public void testAllQuestionsCanBeOpened() {
        // Все вопросы можно открыть по очереди
        for (int i = 1; i <= 8; i++) {
            mainPage.clickQuestion(i);
            assertTrue(mainPage.isAnswerDisplayed(i),
                    "Ответ на вопрос " + i + " должен отображаться");

            String answerText = mainPage.getAnswerText(i);
            assertFalse(answerText.isEmpty(),
                    "Текст ответа на вопрос " + i + " не должен быть пустым");
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}