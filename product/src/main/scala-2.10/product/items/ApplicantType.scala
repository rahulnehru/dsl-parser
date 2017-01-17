package product.items


object ApplicantType extends RuleItems {
  type Applicant = Value
  val MALE, FEMALE = Value

  val keywords = Seq("male", "female", "applicant")
}