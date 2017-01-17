package product.rules

import product.Engine
import product.items.{Section, ApplicantType}

case class SectionHasMetadataActivated(metadata: String, section: Section.Value) extends CustomRule {

  def calculate(engine: Engine) = true

}
