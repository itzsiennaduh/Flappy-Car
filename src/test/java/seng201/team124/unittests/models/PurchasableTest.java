package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.vehicleUtility.Purchasable;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchasableTest {

    private Purchasable item;

    @BeforeEach
    void setUp() {
        item = new Purchasable() {
            @Override
            public String getName() {
                return "Test Item";
            }

            @Override
            public String getDescription() {
                return "Test Description";
            }

            @Override
            public double getCost() {
                return 0;
            }

            @Override
            public double getSellPrice() {
                return calculateSellPrice();
            }
        };
    }

    @Test
    void testCalculateSellPriceStandardCost() {
        setUpItemCost(100.0);
        double expectedSellPrice = 100.0 * 0.7;
        assertEquals(expectedSellPrice, item.calculateSellPrice(), 0.01);
    }

    @Test
    void testCalculateSellPriceZeroCost() {
        setUpItemCost(0.0);
        double expectedSellPrice = 0.0;
        assertEquals(expectedSellPrice, item.calculateSellPrice(), 0.01);
    }

    @Test
    void testCalculateSellPrice_ighCost() {
        setUpItemCost(1000000.0);
        double expectedSellPrice = 1000000.0 * 0.7;
        assertEquals(expectedSellPrice, item.calculateSellPrice(), 0.01);
    }

    @Test
    void testCalculateSellPriceFractionalCost() {
        setUpItemCost(0.99);
        double expectedSellPrice = 0.99 * 0.7;
        assertEquals(expectedSellPrice, item.calculateSellPrice(), 0.01);
    }

    @Test
    void testCalculateSellPriceNegativeCost() {
        setUpItemCost(-100.0);
        double expectedSellPrice = -100.0 * 0.7;
        assertEquals(expectedSellPrice, item.calculateSellPrice(), 0.01);
    }

    private void setUpItemCost(double cost) {
        item = new Purchasable() {
            @Override
            public String getName() {
                return "Test Item";
            }

            @Override
            public String getDescription() {
                return "Test Description";
            }

            @Override
            public double getCost() {
                return cost;
            }

            @Override
            public double getSellPrice() {
                return calculateSellPrice();
            }
        };
    }
}