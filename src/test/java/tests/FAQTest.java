package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import utils.WebDriverSetup;
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
    @DisplayName("При клике на вопрос отображается ответ")
    public void testQuestionShowsAnswer() {

        mainPage.clickQuestion(1);
        assertTrue(mainPage.isAnswerDisplayed(1), "После клика на вопрос должен отобразиться ответ");
        String answerText = mainPage.getAnswerText(1);
        assertFalse(answerText.isEmpty(), "Текст ответа не должен быть пустым");
    }

    @Test
    @DisplayName("При клике на другой вопрос, предыдущий ответ закрывается")
    public void testOnlyOneAnswerOpenAtTime() {

        mainPage.clickQuestion(1);
        assertTrue(mainPage.isAnswerDisplayed(1), "Первый ответ должен быть виден");
        mainPage.clickQuestion(2);
        assertTrue(mainPage.isAnswerDisplayed(2), "Второй ответ должен быть виден");
        assertFalse(mainPage.isAnswerDisplayed(1), "Первый ответ должен закрыться после клика на второй вопрос");
    }

    @Test
    @DisplayName("Повторный клик на вопрос НЕ закрывает ответ")
    public void testDoubleClickDoesNotCloseAnswer() {
        mainPage.clickQuestion(4);
        assertTrue(mainPage.isAnswerDisplayed(4), "После первого клика ответ должен открыться");
        mainPage.clickQuestion(4);
        assertTrue(mainPage.isAnswerDisplayed(4), "После второго клика на тот же вопрос ответ должен остаться открытым");
    }

    @Test
    @DisplayName("Можно открыть несколько вопросов последовательно")
    public void testSequentialQuestionOpening() {
        // Открываем вопрос 3
        mainPage.clickQuestion(3);
        assertTrue(mainPage.isAnswerDisplayed(3), "Ответ 3 должен быть виден");
        assertFalse(mainPage.isAnswerDisplayed(1), "Ответ 1 не должен быть виден");
        assertFalse(mainPage.isAnswerDisplayed(2), "Ответ 2 не должен быть виден");

        // Открываем вопрос 5 - вопрос 3 должен закрыться
        mainPage.clickQuestion(5);
        assertTrue(mainPage.isAnswerDisplayed(5), "Ответ 5 должен быть виден");
        assertFalse(mainPage.isAnswerDisplayed(3), "Ответ 3 должен закрыться");

        // Открываем вопрос 2 - вопрос 5 должен закрыться
        mainPage.clickQuestion(2);
        assertTrue(mainPage.isAnswerDisplayed(2), "Ответ 2 должен быть виден");
        assertFalse(mainPage.isAnswerDisplayed(5), "Ответ 5 должен закрыться");
    }

    @Test
    @DisplayName("Все ответы содержат текст")
    public void testAllAnswersHaveText() {
        int questionsCount = mainPage.getQuestionsCount();

        for (int i = 1; i <= questionsCount; i++) {
            // Открываем вопрос
            mainPage.clickQuestion(i);

            // Проверяем, что ответ содержит текст
            String answerText = mainPage.getAnswerText(i);
            assertFalse(answerText.isEmpty(), "Ответ на вопрос " + i + " не должен быть пустым");
        }
    }

    @Test
    @DisplayName("Быстрая проверка работы аккордеона")
    public void testAccordionFunctionality() {
        // Проверяем работу аккордеона на нескольких вопросах
        int[] testQuestions = {1, 3, 6, 8};

        for (int i = 0; i < testQuestions.length; i++) {
            int currentQuestion = testQuestions[i];

            // Открываем текущий вопрос
            mainPage.clickQuestion(currentQuestion);
            assertTrue(mainPage.isAnswerDisplayed(currentQuestion),
                    "Ответ на вопрос " + currentQuestion + " должен быть виден");

            // Проверяем, что предыдущие вопросы закрыты (кроме первого итерации)
            if (i > 0) {
                int previousQuestion = testQuestions[i - 1];
                assertFalse(mainPage.isAnswerDisplayed(previousQuestion),
                        "Ответ на вопрос " + previousQuestion + " должен закрыться");
            }

            // Проверяем, что ответ не пустой
            String answerText = mainPage.getAnswerText(currentQuestion);
            assertTrue(answerText.length() > 10, "Ответ должен содержать развернутую информацию");
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}