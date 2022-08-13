package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.holdBrowserOpen;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class cardDeliveryService {
    public static String getLocalDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy",new Locale("ru")));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        holdBrowserOpen = true;
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    void shouldBeValid() {
        String meetingDate = getLocalDate(19);
        $("[placeholder=Город]").setValue("Москва");
        $("[data-test-id=\"date\"] span.input__box [placeholder=\"Дата встречи\"").doubleClick().sendKeys(meetingDate);
        $x("//input[@name=\"name\"]").val("Максим Шевченко-Григориев");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79125357174");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно забронирована на " + meetingDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldTestEnglishCity() {
        String planningDate = getLocalDate(5);

        $x("//input[@placeholder=\"Город\"]").val("Вашингтон");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Степан Чижиков");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79125357174");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
    }

    @Test
    void shouldTestCityInEnglish() {
        String planningDate = getLocalDate(6);

        $x("//input[@placeholder=\"Город\"]").val("Washington D.C.");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Степан Чижиков");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79125357174");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
    }

    @Test
    void shouldTestPhoneWithoutPlus() {
        String planningDate = getLocalDate(90);

        $x("//input[@placeholder=\"Город\"]").val("Иркутск");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Люк");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("89125357174");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
    }

    @Test
    void shouldTestPhoneLessThen11Digits() {
        String planningDate = getLocalDate(3);

        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Боб");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+7912535717");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
    }


    @Test
    void shouldTestNextDayMeeting() {
        String planningDate = getLocalDate(1);

        $x("//input[@placeholder=\"Город\"]").val("Йошкар-Ола");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Закария");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79876543221");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
    }


    @Test
    void shouldTestFebruaryDays() {

        $x("//input[@placeholder=\"Город\"]").val("Владивосток");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys("29.02.2023");
        $x("//input[@name=\"name\"]").val("Закария");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79876543221");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Неверно введена дата")).should(visible, Duration.ofSeconds(5));
    }

    @Test
    void shouldTestUncheckedBox() {
        String planningDate = getLocalDate(4);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Анна-Мария");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79876543221");
        $x("//*[@class=\"checkbox__text\"]").doubleClick();
        $x("//*[@class=\"button__text\"]").click();
        $("[data-test-id=agreement].checkbox_checked").shouldNot(exist);
    }

    @Test
    void shouldTestCheckedCheckedBox() {
        String planningDate = getLocalDate(4);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Анна-Мария");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79876543221");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $("[data-test-id=agreement].checkbox_checked").should(exist);
    }

}


