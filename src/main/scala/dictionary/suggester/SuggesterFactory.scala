package dictionary.suggester

object SuggesterFactory {
  def getSuggester(name: String, params: Int*): Suggester =
    name match {
      case "bktree" => new BurkhardKellerTree(params(0), params(1))
      case _        => new BurkhardKellerTree(12, 3)
    }
}
