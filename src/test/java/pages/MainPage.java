package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.Arrays;
import java.util.List;

public class MainPage {

    private WebDriver driver;

        // Кнопки "Заказать"
        private final By orderButtonTop = By.className("Button_Button__ra12g");
        private final By orderButtonBottom = By.xpath("//button[contains(@class, 'Button_Middle__1CSJM')]");

        // Заголовок раздела "Вопросы о важном"
        private final By questionTitle = By.xpath("//div[text()='Вопросы о важном']");

        // Список вопросов о важном
        private final List<By> questions = Arrays.asList(
                By.id("accordion__heading-0"),  // вопрос 1
                By.id("accordion__heading-1"),  // вопрос 2
                By.id("accordion__heading-2"),  // вопрос 3
                By.id("accordion__heading-3"),  // вопрос 4
                By.id("accordion__heading-4"),  // вопрос 5
                By.id("accordion__heading-5"),  // вопрос 6
                By.id("accordion__heading-6"),  // вопрос 7
                By.id("accordion__heading-7")   // вопрос 8
        );

        // Список ответов о важном
        private final List<By> answers = Arrays.asList(
                By.id("accordion__panel-0"),    // ответ 1
                By.id("accordion__panel-1"),    // ответ 2
                By.id("accordion__panel-2"),    // ответ 3
                By.id("accordion__panel-3"),    // ответ 4
                By.id("accordion__panel-4"),    // ответ 5
                By.id("accordion__panel-5"),    // ответ 6
                By.id("accordion__panel-6"),    // ответ 7
                By.id("accordion__panel-7")     // ответ 8
        );

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://qa-scooter.praktikum-services.ru");
    }

    public void scrollToFAQSection() {
        WebElement faqSection = driver.findElement(questionTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", faqSection);
    }

    public void clickTopOrderButton() {
        driver.findElement(orderButtonTop).click();
    }

    public void clickBottomOrderButton() {
        WebElement element = driver.findElement(orderButtonBottom);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }

    public void clickQuestion(int questionNumber) {
        int index = questionNumber - 1;
        if (index >= 0 && index < questions.size()) {
            WebElement question = driver.findElement(questions.get(index));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", question);
            question.click();
        }
    }

    public boolean isAnswerDisplayed(int questionNumber) {
        int index = questionNumber - 1;
        if (index >= 0 && index < answers.size()) {
            return driver.findElement(answers.get(index)).isDisplayed();
        }
        return false;
    }

    public String getAnswerText(int questionNumber) {
        int index = questionNumber - 1;
        if (index >= 0 && index < answers.size()) {
            return driver.findElement(answers.get(index)).getText();
        }
        return "";
    }

    public int getQuestionsCount() {
        return questions.size();
    }

    public boolean isMainPageLoaded() {
        return driver.getCurrentUrl().contains("qa-scooter.praktikum-services.ru");
    }

    public boolean isFAQSectionDisplayed() {
        return driver.findElement(questionTitle).isDisplayed();
    }
}