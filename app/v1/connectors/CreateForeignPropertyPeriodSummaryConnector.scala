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

package v1.connectors

import config.AppConfig

import javax.inject.{Inject, Singleton}
import play.api.http.Status
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import v1.connectors.httpparsers.StandardDesHttpParser._
import v1.models.request.createForeignPropertyPeriodSummary.CreateForeignPropertyPeriodSummaryRequest
import v1.models.response.createForeignPropertyPeriodSummary.CreateForeignPropertyPeriodSummaryResponse

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreateForeignPropertyPeriodSummaryConnector @Inject()(val http: HttpClient,
                                                            val appConfig: AppConfig) extends BaseDesConnector {

  def createForeignProperty(request: CreateForeignPropertyPeriodSummaryRequest)(
    implicit hc: HeaderCarrier,
    ec: ExecutionContext,
    correlationId: String): Future[DesOutcome[CreateForeignPropertyPeriodSummaryResponse]] = {

    implicit val desSuccessCode: SuccessCode = SuccessCode(Status.OK)

    post(
      body = request.body,
      uri = DesUri[CreateForeignPropertyPeriodSummaryResponse](s"income-tax/business/property/periodic/${request.nino}/${request.businessId}")
    )
  }
}
