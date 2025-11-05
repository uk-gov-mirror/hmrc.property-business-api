/*
 * Copyright 2025 HM Revenue & Customs
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

package v6.createForeignPropertyDetails.def1

import cats.data.Validated
import cats.data.Validated.Invalid
import shared.controllers.validators.RulesValidator
import shared.controllers.validators.resolvers.{ResolveIsoDate, ResolveParsedCountryCode}
import shared.models.errors.*
import v6.createForeignPropertyDetails.def1.model.request.Def1_CreateForeignPropertyDetailsRequestData

object Def1_CreateForeignPropertyDetailsRulesValidator extends RulesValidator[Def1_CreateForeignPropertyDetailsRequestData] {

  def validateBusinessRules(
      parsed: Def1_CreateForeignPropertyDetailsRequestData): Validated[Seq[MtdError], Def1_CreateForeignPropertyDetailsRequestData] = {
    import parsed.body.*

    combine(
      validatePropertyName(propertyName),
      validateCountryCode(countryCode),
      validateEndDate(endDate),
      validateEndReason(endReason)
    ).onSuccess(parsed)
  }

  private def validatePropertyName(propertyName: String) = {
    if (propertyName.length <= 105) valid else Invalid(List(RuleFormatPropertyNameError))
  }

  private def validateCountryCode(countryCode: String) = ResolveParsedCountryCode(countryCode, "/countryCode")

  private def validateEndDate(endDate: Option[String]) = {
    endDate match {
      case Some(date) => ResolveIsoDate(RuleFormatEndDateError).resolver(date)
      case None       => valid
    }
  }

  private def validateEndReason(endReason: Option[String]) = {
    endReason match {
      case Some(endReason) if (endReason == "no-longer-renting-property-out" || endReason == "disposal") => valid
      case None                                                                                          => valid
      case _                                                                                             => Invalid(List(RuleFormatEndReasonError))
    }
  }

}
