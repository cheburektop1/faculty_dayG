package ru.tbank.education.school.lesson8.practise

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 *
 * Сценарии для тестирования:
 *
 * 1. Позитивные сценарии (happy path):
 *    - Обычный случай: basePrice = 1000, discount = 10%, tax = 20% → проверить корректность формулы.
 *    - Без скидки: discountPercent = 0 → итог = basePrice + налог.
 *    - Без налога: taxPercent = 0 → итог = basePrice минус скидка.
 *    - Без скидки и без налога: итог = basePrice.
 *
 * 2. Негативные сценарии (исключения):
 *    - Отрицательная цена: basePrice < 0 → IllegalArgumentException.
 *    - Скидка вне диапазона: discountPercent < 0 или > 100 → IllegalArgumentException.
 *    - Налог вне диапазона: taxPercent < 0 или > 30 → IllegalArgumentException.
 */


class CalculateFinalPriceTest {
    @Test
    fun `prices should return discount price + tax amount`() {
        Assertions.assertTrue(calculateFinalPrice(1000.0, 10, 20) == (1000.0 * 0.9 + 1000.0 * 0.9 * 0.2))
        Assertions.assertTrue(calculateFinalPrice(105.20, 5, 13) == (105.2 * 0.95 + 105.2 * 0.95 * 0.13))
    }

    @Test
    fun `prices should return their price and tax amount`() {
        Assertions.assertTrue(calculateFinalPrice(1000.0, 0, 20) == (1000.0 * 1.2))
        Assertions.assertTrue(calculateFinalPrice(105.20, 0, 13) == (105.2 + 105.2 * 0.13))
    }

    @Test
    fun `prices should return discount price`() {
        Assertions.assertTrue(calculateFinalPrice(1000.0, 20, 0) == (1000.0 * 0.8))
        Assertions.assertTrue(calculateFinalPrice(105.20, 13, 0) == (105.2 * 0.87))
    }

    @Test
    fun `prices should return just their base price`() {
        Assertions.assertTrue(calculateFinalPrice(1000.0, 0, 0) == (1000.0))
        Assertions.assertTrue(calculateFinalPrice(105.20, 0, 0) == (105.2))
    }

    @Test
    fun `illegal tests should return IllegalArgumentException`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(-5.0, 0, 0)
        }

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(1000.0, -1, 0)
        }

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(1000.0, 500, 0)
        }

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(1000.0, 0, -5)
        }

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(1000.0, 0, 31)
        }
    }
}