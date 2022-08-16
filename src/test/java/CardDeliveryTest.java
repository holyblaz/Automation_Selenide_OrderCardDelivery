import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class CardDeliveryTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldSendForm() {
        String planningDate = generateDate(5);

        $("[data-test-id='city'] input").setValue("Липецк");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(planningDate);
        $x("//input[@name='name']").setValue("Пронин Илья");
        $x("//input[@name='phone']").setValue("+79099878106");
        $("[data-test-id='agreement']").click();
        $x("//span[@class='button__text']").click();
        $x("//*[contains(text(),'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    void shouldSendFormWithDateAndCity() {

        String validateDate = generateDate(7);
        String calendarDate = String.valueOf(LocalDate.now().plusDays(7).getDayOfMonth());
        String planningDate = String.valueOf(LocalDate.now().plusDays(7).getMonth());
        String deliveryDate = String.valueOf(LocalDate.now().plusDays(3).getMonth());

        $("[data-test-id='city'] input").setValue("Пе");
        $$x("//span[@class='menu-item__control']").get(1).click();
        $x("//span[@class='input__icon']").click();

        if (!(Objects.equals(planningDate,deliveryDate))) {
            $("[data-step='1']").click();
        }

        $$("table.calendar__layout td").find(text(calendarDate)).click();
        $x("//input[@name='name']").setValue("Пронин Илья");
        $x("//input[@name='phone']").setValue("+79099878106");
        $("[data-test-id='agreement']").click();
        $x("//span[@class='button__text']").click();
        $x("//*[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + validateDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
}
