package tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;

public class GismeteoParamTest extends TestBase {


    @CsvSource(value = {
            "Новости, https://www.gismeteo.ru/news/",
            "Карты, https://www.gismeteo.ru/maps/",
            "Информеры, https://www.gismeteo.ru/informers/"})
    @DisplayName("Проверка работоспособности хэдера")
    @Tags({@Tag("WEB"), @Tag("SMOKE")})
    @ParameterizedTest()
    void headeaButtonsTest(String headerButton, String defaultUrl) {
        $$(".link").findBy(Condition.text(headerButton)).click();
        webdriver().shouldHave(url(defaultUrl));
    }

    @ValueSource(strings = {"Ярославль", "Кострома", "Москва"})
    @DisplayName("Поиск города")
    @Tag("WEB")
    @ParameterizedTest
    void searchCityTest(String searchCity) {
        $(".input").setValue(searchCity);
        $$(".search-item").findBy(Condition.text(searchCity)).click();
        $(".page-title").shouldHave(Condition.text("Погода"));
    }

    static Stream<Arguments> breadCrumpsTest() {
        return Stream.of(
                Arguments.of("Москва", List.of("Россия", "Москва (город федерального значения)")),
                Arguments.of("Ярославль", List.of("Россия", "Ярославская область", "Ярославль (городской округ"))
        );
    }

    @MethodSource
    @DisplayName("Переключение даты")
    @Tag("WEB")
    @ParameterizedTest()
    void breadCrumpsTest(String city, List<String> breadCrumps) {
        $(".input").setValue(city);
        $$(".search-item").findBy(Condition.text(city)).click();
        $$(".breadcrumbs-links a").shouldHave(CollectionCondition.texts(breadCrumps));
    }
}