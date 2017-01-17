package product.rules

import product.Engine
import product.items.EnabledItems

case class ApplicationStateCheck(active: Boolean) extends CustomRule {
  override def calculate(engine: Engine): Boolean = engine.state.isDefined
}


case class ApplicationStateIsActive(isActive: Boolean) extends CustomRule {
  override def calculate(engine: Engine): Boolean = engine.state.exists(p => p.getOrElse("active", false))
}

case class ItemsAreEnabled(itemType: EnabledItems.Value, itemName: String, enabled: Boolean) extends CustomRule {
  override def calculate(engine: Engine): Boolean = engine.state.exists(p => p.get(itemName).equals(Some(enabled)))
}



