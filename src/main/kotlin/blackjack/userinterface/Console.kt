package blackjack.userinterface

import blackjack.dto.GamerDto
import blackjack.dto.GamersDto
import blackjack.dto.ResultDto

object Console : UserInterface {

    private const val PLAYER_NAME_DELIMITER = ","

    override fun inputPlayerNames(): List<String> {
        println("게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)")
        val input = readLine() ?: throw RuntimeException("입력값 오류")
        return input.split(PLAYER_NAME_DELIMITER).map { it.trim() }.also {
            validateDuplicateName(it)
        }
    }

    private fun validateDuplicateName(playerNames: List<String>) {
        if (playerNames.size != playerNames.distinct().size) {
            throw RuntimeException("입력한 이름에 중복이 있어요. 입력한 이름: $playerNames")
        }
    }

    override tailrec fun inputCardTakenWhether(playerName: String): Boolean {
        println("$playerName 는 한장의 카드를 더 받겠습니까? (예는 y, 아니오는 n)")
        return when (val input = readLine()) {
            "y" -> true
            "n" -> false
            else -> {
                println("올바르지 않은 입력입니다. 입력값: $input")
                inputCardTakenWhether(playerName)
            }
        }
    }

    override fun outputCurrentCards(gamerDto: GamerDto) {
        println(gamerDto.viewFormat())
    }

    override fun outputDealerTaken(dealerLimitScore: Int) {
        println("딜러는 $dealerLimitScore 이하라 한장의 카드를 더 받았습니다.")
    }

    override fun outputGamerCards(gamersDto: GamersDto) {
        val players = gamersDto.players
        val dealerCards = gamersDto.dealerCards

        println("딜러와 ${players.joinToString(", ") { it.name }} 에게 2장의 카드를 나누었습니다.")
        println("딜러 카드: ${dealerCards.joinToString()}")
        players.forEach { println(it.viewFormat()) }
    }

    override fun outputGameResult(result: List<ResultDto>) {
        result.forEach { println("${it.name}카드: ${it.cards.joinToString()} - 결과: ${it.score}") }
    }

    private fun GamerDto.viewFormat(): String = "${this.name}카드: ${this.cards.joinToString()}"
}
