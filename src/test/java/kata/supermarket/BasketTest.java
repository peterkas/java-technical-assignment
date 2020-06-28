package kata.supermarket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasketTest {

    @DisplayName("basket provides its total value when containing...")
    @MethodSource
    @ParameterizedTest(name = "{0}")
    void basketProvidesTotalValue(String description, String expectedTotal, Iterable<Item> items, DiscountScheme discountScheme) {
        final Basket basket = new Basket();
        items.forEach(i -> basket.add(i, discountScheme));
        assertEquals(new BigDecimal(expectedTotal), basket.total());
    }

    static Stream<Arguments> basketProvidesTotalValue() {
        return Stream.of(
                noItems(),
                aSingleItemPricedPerUnit(),
                multipleItemsPricedPerUnit(),
                buyOneGetOneFree(),
                buyTwoItemsForOnePound(),
                buyThreeItemsPerPriceOfTwo(),
                aSingleItemPricedByWeight(),
                multipleItemsPricedByWeight(),
                vegetablesHalfPriceByWeight()
        );
    }

    private static Arguments aSingleItemPricedByWeight() {
        return Arguments.of("a single weighed item", "1.25", Collections.singleton(twoFiftyGramsOfAmericanSweets()),
                DiscountScheme.NO_DISCOUNT);
    }

    private static Arguments multipleItemsPricedByWeight() {
        return Arguments.of("multiple weighed items", "1.85",
                Arrays.asList(twoFiftyGramsOfAmericanSweets(), twoHundredGramsOfPickAndMix()),
                DiscountScheme.NO_DISCOUNT
        );
    }

    private static Arguments vegetablesHalfPriceByWeight() {
        return Arguments.of("vegetables weighed items with half price discount per kilo", "2.99",
                Arrays.asList(aKiloAndTwoFiftyGramsOfPotatos(DiscountScheme.ONE_KG_HALF_PRICE)),
                DiscountScheme.ONE_KG_HALF_PRICE
        );
    }

    private static Arguments multipleItemsPricedPerUnit() {
        return Arguments.of("multiple items priced per unit", "2.04",
                Arrays.asList(aPackOfDigestives(), aPintOfMilk()), DiscountScheme.NO_DISCOUNT);
    }

    private static Arguments buyOneGetOneFree() {
        return Arguments.of("buy two items per price of one", "1.55",
                nPackOfDigestives(2), DiscountScheme.BUY_ONE_GET_ONE);
    }

    private static Arguments buyTwoItemsForOnePound() {
        return Arguments.of("buy two items for one pound", "1.00",
                Arrays.asList(aCanOfCoke(), aCanOfCoke()), DiscountScheme.TWO_FOR_ONE_POUND);
    }

    private static Arguments buyThreeItemsPerPriceOfTwo() {
        return Arguments.of("buy three items per price of two", "0.98",
                nPintOfMilk(3), DiscountScheme.THREE_FOR_TWO);
    }

    private static Arguments aSingleItemPricedPerUnit() {
        return Arguments.of("a single item priced per unit", "0.49", Collections.singleton(aPintOfMilk()), DiscountScheme.NO_DISCOUNT);
    }

    private static Arguments noItems() {
        return Arguments.of("no items", "0.00", Collections.emptyList(), DiscountScheme.NO_DISCOUNT);
    }

    private static Item aPintOfMilk() {
        return new Product(new BigDecimal("0.49")).oneOf();
    }

    private static Iterable<Item> nPintOfMilk(int n) {
        return new Product(new BigDecimal("0.49")).anyOf(n);
    }

    private static Item aPackOfDigestives() {
        return new Product(new BigDecimal("1.55")).oneOf();
    }

    private static Iterable<Item> nPackOfDigestives(int n) {
        return new Product(new BigDecimal("1.55")).anyOf(n);
    }

    private static Item aCanOfCoke() {
        return new Product(new BigDecimal("0.59")).oneOf();
    }

    private static WeighedProduct aKiloOfAmericanSweets() {
        return new WeighedProduct(new BigDecimal("4.99"));
    }

    private static Item twoFiftyGramsOfAmericanSweets() {
        return aKiloOfAmericanSweets().weighing(new BigDecimal(".25"));
    }

    private static WeighedProduct aKiloOfPotatos() {
        return new WeighedProduct(new BigDecimal("3.99"));
    }

    private static Item aKiloAndTwoFiftyGramsOfPotatos(DiscountScheme discountScheme) {
        return aKiloOfPotatos().weighing(new BigDecimal("1.25"), discountScheme);
    }

    private static WeighedProduct aKiloOfPickAndMix() {
        return new WeighedProduct(new BigDecimal("2.99"));
    }

    private static Item twoHundredGramsOfPickAndMix() {
        return aKiloOfPickAndMix().weighing(new BigDecimal(".2"));
    }
}