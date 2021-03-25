package blackjack

import blackjack.domain.Cards
import blackjack.domain.Score
import blackjack.domain.deck.Deck
import blackjack.domain.deck.DeckFactory
import blackjack.domain.player.Dealer
import blackjack.domain.player.Player
import blackjack.domain.player.PlayerNames
import blackjack.dto.PlayerDto
import blackjack.dto.ResultDto
import blackjack.userinterface.Console
import blackjack.userinterface.UserInterface

fun main() {
    val game = BlackJackGame(Console)
    game.run()
}

class BlackJackGame(private val userInterface: UserInterface) {

    private val deck: Deck = DeckFactory.create()

    fun run() {
        val dealer = Dealer("딜러", Cards(deck.draw(), deck.draw()))

        val players = run {
            val userNames = PlayerNames(userInterface.inputPlayerNames())
            userNames.map { Player(it, Cards(deck.draw(), deck.draw())) }
        }

        userInterface.outputPlayerCards((listOf(dealer) + players).map(::PlayerDto))
        players.forEach { takeCardsIfNecessary(it) }

        if (!dealer.isNotTakeable()) {
            dealer.takeCard(deck.draw())
            userInterface.outputDealerTaken(Score.DEALER_TAKEABLE_LIMIT.value)
        }

        userInterface.outputGameResult((listOf(dealer) + players).map(::ResultDto))
    }

    private tailrec fun takeCardsIfNecessary(player: Player) {
        if (player.isNotTakeable()) {
            return
        }

        when (userInterface.inputCardTakenWhether(player.name)) {
            false -> return
            true -> {
                player.takeCard(deck.draw())
                userInterface.outputCurrentCards(PlayerDto(player))
                return takeCardsIfNecessary(player)
            }
        }
    }
}
