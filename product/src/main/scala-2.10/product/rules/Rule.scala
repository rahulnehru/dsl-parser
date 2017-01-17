package product.rules

import product.Engine


sealed trait Rule
trait CustomRule extends Rule {
  def calculate(engine: Engine) : Boolean
}

object TrueRule extends Rule



case class ConjunctionRule(left: Rule, right: Rule) extends Rule
case class DenialRule(denialRule: Rule) extends Rule
case class DisjunctionRule(left: Rule, right: Rule) extends Rule



