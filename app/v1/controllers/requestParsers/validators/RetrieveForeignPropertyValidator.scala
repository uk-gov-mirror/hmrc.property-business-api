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

package v1.controllers.requestParsers.validators

import v1.controllers.requestParsers.validators.validations.{BusinessIdValidation, NinoValidation, SubmissionIdValidation}
import v1.models.errors.MtdError
import v1.models.request.retrieveForeignProperty.RetrieveForeignPropertyRawData

class RetrieveForeignPropertyValidator extends Validator[RetrieveForeignPropertyRawData] {

  private val validationSet = List(parameterFormatValidation)

  private def parameterFormatValidation: RetrieveForeignPropertyRawData => List[List[MtdError]] = (data: RetrieveForeignPropertyRawData) => {
    List(
      NinoValidation.validate(data.nino),
      BusinessIdValidation.validate(data.businessId),
      SubmissionIdValidation.validate(data.submissionId)
    )
  }

  override def validate(data: RetrieveForeignPropertyRawData): List[MtdError] = {
    run(validationSet, data).distinct
  }
}
