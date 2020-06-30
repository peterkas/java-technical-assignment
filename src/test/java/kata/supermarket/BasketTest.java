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

    private static DiscountScheme noDiscount() {
        return new DefaultDiscountScheme(BigDecimal.ONE,1);
    }

    private static DiscountScheme buyOneGetOne() {
        return new DiscountSchemeByVolume(BigDecimal.ONE,2);
    }

    private static DiscountScheme twoForOnePound() {
        return new DiscountSchemeByFixedPrice(BigDecimal.ONE,2);
    }

    private static DiscountScheme threeForTwo() {
        return new DiscountSchemeByVolume(BigDecimal.valueOf(2),3);
    }

    private static DiscountScheme oneKgHalfPrice() {
        return new DiscountSchemeByWeight(BigDecimal.valueOf(0.5),1);
    }

    private static Arguments aSingleItemPricedByWeight() {
        return Arguments.of("a single weighed item", "1.25", Collections.singleton(twoFiftyGramsOfAmericanSweets()),
                noDiscount());
    }

    private static Arguments multipleItemsPricedByWeight() {
        return Arguments.of("multiple weighed items", "1.85",
                Arrays.asList(twoFiftyGramsOfAmericanSweets(), twoHundredGramsOfPickAndMix()),
                noDiscount()
        );
    }

    private static Arguments vegetablesHalfPriceByWeight() {
        return Arguments.of("vegetables weighed items with half price discount per kilo", "2.99",
                Arrays.asList(aKiloAndTwoFiftyGramsOfPotatos(oneKgHalfPrice())),
                oneKgHalfPrice()
        );
    }

    private static Arguments multipleItemsPricedPerUnit() {
        return Arguments.of("multiple items priced per unit", "2.04",
                Arrays.asList(aPackOfDigestives(), aPintOfMilk()), noDiscount());
    }

    private static Arguments buyOneGetOneFree() {
        return Arguments.of("buy two items per price of one", "1.55",
                nPackOfDigestives(2), buyOneGetOne());
    }

    private static Arguments buyTwoItemsForOnePound() {
        return Arguments.of("buy two items for one pound", "1.00",
                Arrays.asList(aCanOfCoke(), aCanOfCoke()), twoForOnePound());
    }

    private static Arguments buyThreeItemsPerPriceOfTwo() {
        return Arguments.of("buy three items per price of two", "0.98",
                nPintOfMilk(3), threeForTwo());
    }

    private static Arguments aSingleItemPricedPerUnit() {
        return Arguments.of("a single item priced per unit", "0.49", Collections.singleton(aPintOfMilk()),
                noDiscount());
    }

    private static Arguments noItems() {
        return Arguments.of("no items", "0.00", Collections.emptyList(), noDiscount());
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