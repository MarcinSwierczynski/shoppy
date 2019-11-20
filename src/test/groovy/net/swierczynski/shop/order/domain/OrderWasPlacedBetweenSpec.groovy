package net.swierczynski.shop.order.domain

import spock.lang.Specification

import java.time.Clock
import java.time.Duration

/**
 * @author Marcin Świerczyński
 * @since 19/11/2019
 */
class OrderWasPlacedBetweenSpec extends Specification {

    private static Clock now = Clock.systemDefaultZone()

    def "should indicate order was not placed in time range"() {
        given:
            Order order = OrderFixture.newOrder(now)
        expect:
            order.wasPlacedBetween(from, to) == isInRange
        where:
            from           || to             || isInRange
            aSecondAgo()   || null           || true
            null           || aSecondLater() || true
            aSecondAgo()   || aSecondLater() || true
            null           || null           || true
            aSecondLater() || null           || false
            null           || aSecondAgo()   || false
            aSecondLater() || aSecondAgo()   || false
    }

    private static Clock aSecondAgo() {
        Clock.offset(now, Duration.ofSeconds(-1))
    }

    private static Clock aSecondLater() {
        Clock.offset(now, Duration.ofSeconds(1))
    }

}
