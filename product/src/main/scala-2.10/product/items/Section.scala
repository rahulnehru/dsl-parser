package product.items


object Section extends RuleItems {
  type Section = Value
  val START, APPLICATION, FINANCE, DOCUMENTS, DECLARATION, PAYMENT = Value
  val keywords = Seq("section", "start", "payment", "application", "finance", "documents", "declaration")
}

