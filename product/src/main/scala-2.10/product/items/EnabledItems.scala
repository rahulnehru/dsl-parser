package product.items

import product.items.RuleItems

object EnabledItems extends RuleItems {
  type Section = Value
  val FEATURE_SWITCH, ATTRIBUTE, METADATA = Value

  val keywords = Seq("feature", "switch", "attribute", "metadata", "enabled", "disabled")
}
