/*
 * Copyright 2021 HM Revenue & Customs
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

package v1.services

import cats.implicits._
import cats.data.EitherT
import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.http.HeaderCarrier
import utils.Logging
import v1.connectors.AmendForeignPropertyAnnualSubmissionConnector
import v1.controllers.EndpointLogContext
import v1.models.errors._
import v1.models.request.amendForeignPropertyAnnualSubmission.AmendForeignPropertyAnnualSubmissionRequest
import v1.support.DesResponseMappingSupport

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AmendForeignPropertyAnnualSubmissionService @Inject()
(connector: AmendForeignPropertyAnnualSubmissionConnector) extends DesResponseMappingSupport with Logging {

  def amendForeignPropertyAnnualSubmission(request: AmendForeignPropertyAnnualSubmissionRequest)(
    implicit hc: HeaderCarrier,
    ec: ExecutionContext,
    logContext: EndpointLogContext,
    correlationId: String): Future[AmendForeignPropertyAnnualSubmissionServiceOutcome] = {

    val result = for {
      desResponseWrapper <- EitherT(connector.amendForeignPropertyAnnualSubmission(request)).leftMap(mapDesErrors(desErrorMap))
    } yield desResponseWrapper

    result.value
  }

  private def desErrorMap =
    Map(
      "INVALID_TAXABLE_ENTITY_ID" -> NinoFormatError,
      "INVALID_INCOME_SOURCE_ID" -> BusinessIdFormatError,
      "INVALID_SUBMISSION_ID" -> BusinessIdFormatError,
      "INVALID_TAX_YEAR" -> DownstreamError,
      "INVALID_PAYLOAD" -> DownstreamError,
      "INVALID_CORRELATION_ID" -> DownstreamError,
      "UNPROCESSABLE_ENTITY" -> DownstreamError,
      "SERVER_ERROR" -> DownstreamError,
      "SERVICE_UNAVAILABLE" -> DownstreamError
    )
}