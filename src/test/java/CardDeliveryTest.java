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

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class CardDeliveryTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    Calendar calendar = Calendar.getInstance();
    List<String> months = Arrays.asList("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");

    public String setCalendar() {
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        return months.get(calendar.get(Calendar.MONTH));
    }

    public String setDate() {
        return Integer.toString(calendar.get(Calendar.DATE));
    }

    public String getValidDate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
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
        $("[data-test-id='city'] input").setValue("Пе");
        $$x("//span[@class='menu-item__control']").get(1).click();
        $x("//span[@class='input__icon']").click();

        while (!$("[class='calendar__name']").getText().contains(setCalendar())) {
            $("[data-step='1']").click();
        }

        $$("table.calendar__layout td").find(text(setDate())).click();
        $x("//input[@name='name']").setValue("Пронин Илья");
        $x("//input[@name='phone']").setValue("+79099878106");
        $("[data-test-id='agreement']").click();
        $x("//span[@class='button__text']").click();
        $x("//*[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + getValidDate()), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
}
