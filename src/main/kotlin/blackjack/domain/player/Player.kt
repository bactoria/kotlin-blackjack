package blackjack.domain.player

import blackjack.domain.Card
import blackjack.domain.Cards
import blackjack.domain.Score

data class Player(val name: String, val cards: Cards) {
    init {
        require(name.isNotBlank())
    }

    fun isNotTakeable(): Boolean {
        return cards.score >= Score.BLACKJACK
    }

    fun takeCard(card: Card) {
        cards.add(card)
    }
}
