/*
 * Copyright 2020 HM Revenue & Customs
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

package v1.models.request.createForeignProperty

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class ForeignFhlEeaExpenditure(premisesRunningCosts: Option[BigDecimal],
                                    repairsAndMaintenance: Option[BigDecimal],
                                    financialCosts: Option[BigDecimal],
                                    professionalFees: Option[BigDecimal],
                                    costsOfServices: Option[BigDecimal],
                                    travelCosts: Option[BigDecimal],
                                    other: Option[BigDecimal],
                                    consolidatedExpenses: Option[BigDecimal]
                                   ) {
  def isEmpty: Boolean = premisesRunningCosts.isEmpty &&
    repairsAndMaintenance.isEmpty &&
    financialCosts.isEmpty &&
    professionalFees.isEmpty &&
    costsOfServices.isEmpty &&
    travelCosts.isEmpty &&
    other.isEmpty &&
    consolidatedExpenses.isEmpty
}

object ForeignFhlEeaExpenditure {
  implicit val reads: Reads[ForeignFhlEeaExpenditure] = Json.reads[ForeignFhlEeaExpenditure]
  implicit val writes: Writes[ForeignFhlEeaExpenditure] = (
    (JsPath \ "premisesRunningCostsAmount").writeNullable[BigDecimal] and
      (JsPath \ "repairsAndMaintenanceAmount").writeNullable[BigDecimal] and
      (JsPath \ "financialCostsAmount").writeNullable[BigDecimal] and
      (JsPath \ "professionalFeesAmount").writeNullable[BigDecimal] and
      (JsPath \ "costsOfServicesAmount").writeNullable[BigDecimal] and
      (JsPath \ "travelCostsAmount").writeNullable[BigDecimal] and
      (JsPath \ "otherAmount").writeNullable[BigDecimal] and
      (JsPath \ "consolidatedExpensesAmount").writeNullable[BigDecimal]
    )(unlift(ForeignFhlEeaExpenditure.unapply))
}
