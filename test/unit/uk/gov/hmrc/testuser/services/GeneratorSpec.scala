/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unit.uk.gov.hmrc.testuser.services

import org.scalatest.matchers.{MatchResult, Matcher}
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.testuser.models.{TestAgent, TestIndividual, TestOrganisation, TestUser}
import uk.gov.hmrc.testuser.models.ServiceName._
import uk.gov.hmrc.testuser.services.Generator
import unit.uk.gov.hmrc.testuser.services.CustomMatchers.haveDifferentPropertiesThan

class GeneratorSpec extends UnitSpec {

  val underTest = Generator

  "generateTestIndividual" should {

    "create a different test individual at every run" in {
      val individual1 = underTest.generateTestIndividual(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX))
      val individual2 = underTest.generateTestIndividual(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX))

      individual1 should haveDifferentPropertiesThan(individual2)
    }
  }

  "generateTestOrganisation" should {

    "create a different test organisation at every run" in {
      val organisation1 = underTest.generateTestOrganisation(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX,
        CORPORATION_TAX, PAYE_FOR_EMPLOYERS, SUBMIT_VAT_RETURNS))
      val organisation2 = underTest.generateTestOrganisation(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX,
        CORPORATION_TAX, PAYE_FOR_EMPLOYERS, SUBMIT_VAT_RETURNS))

      organisation1 should haveDifferentPropertiesThan(organisation2)
    }
  }

  "generateTestAgent" should {

    "create a different test agent at every run" in {
      val agent1 = underTest.generateTestAgent(Seq(AGENT_SERVICES))
      val agent2 = underTest.generateTestAgent(Seq(AGENT_SERVICES))

      agent1 should haveDifferentPropertiesThan(agent2)
    }
  }
}

object CustomMatchers {
  class HaveDifferentPropertiesThan(right: TestUser) extends Matcher[TestUser] {

    def apply(left: TestUser) = {
      MatchResult(
        allFieldsDifferent(left),
        s"""Individuals $left and $right have same fields"""",
        s"""Individuals are different""""
      )
    }

    private def allFieldsDifferent(leftUser: TestUser) = {
      (leftUser, right) match {
        case (i1: TestIndividual, i2: TestIndividual) => i1._id != i2._id  &&
            i1.userId != i2.userId &&
            i1.password != i2.password &&
            i1.nino != i2.nino &&
            i1.saUtr != i2.saUtr
        case (o1: TestOrganisation, o2: TestOrganisation) => o1._id != o2._id &&
          o1.userId != o2.userId &&
          o1.password != o2.password &&
          o1.saUtr != o2.saUtr &&
          o1.ctUtr != o2.ctUtr &&
          o1.empRef != o2.empRef &&
          o1.vrn != o2.vrn
        case (a1: TestAgent, a2: TestAgent) => a1._id != a2._id &&
          a1.userId != a2.userId &&
          a1.password != a2.password &&
          a1.arn != a2.arn
        case _ => false
      }
    }
  }

  def haveDifferentPropertiesThan(right: TestUser) = new HaveDifferentPropertiesThan(right)
}
