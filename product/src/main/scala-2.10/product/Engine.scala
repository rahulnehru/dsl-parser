package product

case class Engine(engineData : Map[String, Map[String, String]], state: Option[Map[String, Boolean]] = None)
