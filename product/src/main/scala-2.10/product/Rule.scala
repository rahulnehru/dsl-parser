package product

import product.items.ApplicantType.{FEMALE, MALE}
import product.items.EnabledItems._

import scala.util.parsing.combinator.PackratParsers
import scala.util.parsing.combinator.syntactical.StandardTokenParsers
import product.rules._
import product.items.{ApplicantType, Section, EnabledItems}



trait KeywordLibrary {

  private val questionDomain = Seq("block", "question", "answer")
  private val logicalDomain = Seq("and", "or", "not", "either", "with", "is", "has", "to", "a")
  private val engineDomain = Seq("product", "existent", "non" , "existent", "group")

  val delimiters: Seq[String] = Seq("(", ")", "or", "and", "with")
  val keywords: Seq[String] = engineDomain ++ questionDomain ++ logicalDomain  ++ ApplicantType.keywords ++ Section.keywords ++ EnabledItems.keywords
}


object RuleDsl extends StandardTokenParsers with PackratParsers with KeywordLibrary {


  lexical.delimiters ++= delimiters
  lexical.reserved ++= keywords


  private lazy val rule: PackratParser[Rule] =  denial|disjunction| conjunction  |applicantBlockAnsweredWithAnswer | applicantHasMetadata  | sectionHasMetadata |
    applicantBlockAnsweredForQuestion  | applicationIsExistent  | applicationIsAGroupApplication  | itemEnabled

  private lazy val conjunction: Parser[ConjunctionRule] =rule ~ rep(("and"|"with") ~ rule) ^^ {
    case a ~ b if b.nonEmpty => {
      new ConjunctionRule(a, b.headOption.map(_._2).getOrElse(a))
    }
    case a ~ b => ConjunctionRule(a, TrueRule)
  }

  private lazy val disjunction: Parser[DisjunctionRule] = "either" ~ rule ~ rep(("or") ~ rule) ^^ {
    case "either"~a ~ b => {
      new DisjunctionRule(a, b.headOption.map(_._2).getOrElse(a))
    }
  }

  private lazy val denial: Parser[DenialRule] = "not" ~ "(" ~ rule ~ ")" ^^ {
    case "not" ~"(" ~ a ~")" => new DenialRule(a)
  }


  private lazy val applicantBlockAnsweredWithAnswer: Parser[ApplicantBlockAnswered] =  applicant   ~ "has" ~ "block" ~ ident ~ "with" ~ "answer" ~ ident ~ "to" ~ ident ^^ {
    case a ~ "has" ~ "block" ~ block ~ "with" ~ "answer" ~ answer ~ "to" ~ question if a.isInstanceOf[ApplicantType.Value]=> new ApplicantBlockAnswered(blockName = block, questionId = Some(question), answerId = Some(answer), applicant = a)
  }

  private lazy val applicantBlockAnsweredForQuestion: Parser[ApplicantBlockAnswered] = applicant   ~ "has" ~ "block" ~ ident ~ "with" ~ "answer" ~ "to" ~ ident ^^ {
    case a ~ "has" ~ "block" ~ block ~ "with" ~ "answer" ~ "to" ~ question if a.isInstanceOf[ApplicantType.Value]=> new ApplicantBlockAnswered(blockName = block, questionId = Some(question), answerId = None, applicant = a)
  }

  private lazy val applicantHasMetadata: Parser[ApplicantHasMetadataActivated] = applicant  ~ "has" ~ "metadata" ~ ident ^^ {
    case a ~ "has" ~ "metadata" ~ evidence => new ApplicantHasMetadataActivated(metadata = evidence, applicant = a)
  }

  private lazy val sectionHasMetadata: Parser[SectionHasMetadataActivated] = section  ~ "has" ~ "metadata" ~ ident ^^ {
    case s ~ "has" ~ "metadata" ~ evidence => new SectionHasMetadataActivated(metadata = evidence, section = s)
  }

  private lazy val applicationIsExistent: Parser[ApplicationStateCheck] = "application" ~ "is" ~ isExistent ^^ {
    case "application" ~ "is" ~  existent => new ApplicationStateCheck(existent)
  }

  private lazy val applicationIsAGroupApplication: Parser[ApplicationStateIsActive] = "application" ~ "is" ~ groupApplication ^^ {
    case "application" ~ "is" ~  group => new ApplicationStateIsActive(group.asInstanceOf[Boolean])
  }

  private lazy val itemEnabled: Parser[ItemsAreEnabled] = enabledItems ~ ident ~ "is" ~ enabled ^^ {
    case itemType ~ name ~ "is" ~ enabled => new ItemsAreEnabled(itemType, name, enabled)
  }


  def applicant: Parser[ApplicantType.Value ] = {
    ("male" | "female") ~ "applicant" ^^ {
      case "male" ~  "applicant" => MALE
      case "female" ~ "applicant" => FEMALE
    }
  }

  def section: Parser[Section.Value ] = {
    ("start" | "application" |"finance"|"documents"|"declaration"|"payment" ) ~ "section" ^^ {
      case "start"~ "section" => Section.START
      case "application"~"section" => Section.APPLICATION
      case "finance"~"section" => Section.FINANCE
      case "documents"~"section" => Section.DOCUMENTS
      case "declaration"~"section" => Section.DECLARATION
      case "payment"~"section" => Section.PAYMENT
    }
  }

  def enabledItems: Parser[EnabledItems.Value ] = {
    ("feature" ~ "switch" | "attribute" | "metadata") ^^ {
      case "feature" ~ "switch" => FEATURE_SWITCH
      case "attribute" => ATTRIBUTE
      case "metadata" => METADATA
    }
  }

  def enabled: Parser[Boolean] = {
    ("enabled" | "disabled" | "not" ~ "enabled")  ^^ {
      case "enabled" =>  true
      case _ =>  false
    }
  }

  def isExistent: Parser[Boolean] = {
    ("existent"| "non"~"existent")  ^^ {
      case "existent" =>  true
      case "non"~"existent" =>  false
    }
  }

  def groupApplication: Parser[Boolean] = {
    ("not" ~ "a" ~ "group" ~ "application"| "a" ~ "group" ~ "application")  ^^ {
      case "not" ~ "a" ~ "group" ~ "application" =>  false
      case "a" ~ "group" ~ "application" =>  true
    }
  }


  def parse(s: String) = {
    val tokens = new lexical.Scanner(s)
    phrase(rule)(tokens)
  }

}


