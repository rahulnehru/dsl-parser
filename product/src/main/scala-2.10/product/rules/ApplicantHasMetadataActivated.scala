package product.rules

import product.Engine
import product.items.ApplicantType

case class ApplicantHasMetadataActivated(metadata: String, applicant: ApplicantType.Value) extends CustomRule {

  def calculate(engine: Engine): Boolean = engine.engineData.exists( z => z._1=="metadata" && z._2.exists(y => y._1 == metadata && y._2 == "true" ) )

}



