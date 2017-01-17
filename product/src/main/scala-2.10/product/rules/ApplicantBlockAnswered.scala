package product.rules

import product.Engine
import product.items.ApplicantType

case class ApplicantBlockAnswered(blockName: String, questionId: Option[String], answerId: Option[String], applicant: ApplicantType.Value) extends CustomRule {

  def calculate(engine: Engine) = {
    this match {
      case ApplicantBlockAnswered(form, Some(q),Some(ans), appl) => engine.engineData.exists(data=> data._1==form && data._2.exists(datum => datum._1==q && datum._2==ans))
      case ApplicantBlockAnswered(form, Some(q), None, appl) => engine.engineData.exists(data=> data._1==form && data._2.exists(datum => datum._1==q))
      case _ =>  false
    }
  }

}
