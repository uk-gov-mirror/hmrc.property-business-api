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

package v6.createForeignPropertyDetails

import play.api.libs.json.JsValue
import shared.controllers.validators.Validator
import v6.createForeignPropertyDetails.def1.Def1_CreateForeignPropertyDetailsValidator
import v6.createForeignPropertyDetails.model.request.CreateForeignPropertyDetailsRequestData

import javax.inject.Inject

class CreateForeignPropertyDetailsValidatorFactory @Inject() () {

  def validator(nino: String, businessId: String, taxYear: String, body: JsValue): Validator[CreateForeignPropertyDetailsRequestData] = {
    new Def1_CreateForeignPropertyDetailsValidator(nino, businessId, taxYear, body)
  }

}
