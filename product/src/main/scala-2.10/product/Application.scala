package product

import product.rules.{ConjunctionRule, Rule, DisjunctionRule}

object Application {


  def evaluate(rule: Rule): Boolean = {
    rule match {
      case l: ConjunctionRule => {evaluate(l.left) && evaluate(l.right)}
      case l: DisjunctionRule => evaluate(l.left) || evaluate(l.right)
      case _ => true
    }
  }


  def main(args: Array[String]) {


    val x = List(
      "female applicant has block BlockId with answer AnswerId to QuestionId",
      "male applicant has block BlockId with answer AnswerId to QuestionId",
      "male applicant has block BlockId with answer to QuestionId",
      "female applicant has metadata someIdentityDocument",
      "male applicant has metadata eeaFinancialRequirement",
      "application is non existent",
      "application is a group application",
      "application is not a group application",
      "feature switch featureSwitchName is enabled",
      "feature switch featureSwitchName is disabled",
      "attribute ATTR is enabled",
      "metadata META is not enabled",
      "feature switch FEATURE is disabled",
      "feature switch FEATURE is disabled and metadata META is not enabled and female applicant has block BlockId with answer AnswerId to QuestionId",
      "feature switch SWITCH is disabled with attribute X is enabled",
      "either feature switch TOGGLE is disabled or attribute COMPOUND is enabled or attribute MOLECULE is disabled",
      "not ( attribute ATTRIBUTE is enabled )",
      "either female applicant has metadata someIdentityDocument or not ( attribute ATTRIBUTE is enabled and application is non existent )"
    )




    x.zipWithIndex.foreach(t => RuleDsl.parse(t._1) match {
      case RuleDsl.Success(l, r) => {println(s"${t._2} completed and called rule $l"); evaluate(l.asInstanceOf[Rule]); l }
      case RuleDsl.Failure(msg, _) => {
        throw new IllegalStateException(s"FAILED $msg")
      }
      case RuleDsl.Error(msg, _) => throw new IllegalStateException("ERRORED")

    }
    )
  }

}

