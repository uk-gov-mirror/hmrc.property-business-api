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

package v1.endpoints

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.HeaderNames.ACCEPT
import play.api.http.Status
import play.api.libs.json.Json
import play.api.libs.ws.{WSRequest, WSResponse}
import support.IntegrationBaseSpec
import v1.models.errors._
import v1.stubs.{AuditStub, AuthStub, DesStub, MtdIdLookupStub}

class RetrieveForeignPropertyAnnualSubmissionControllerISpec extends IntegrationBaseSpec {

  private trait Test {

    val nino = "AA123456A"
    val businessId = "XAIS12345678910"
    val taxYear = "2021-22"

    val responseBody = Json.parse(
      s"""
         |{
         |  "foreignFhlEea": {
         |    "adjustments": {
         |      "privateUseAdjustment": 100.25,
         |      "balancingCharge": 100.25,
         |      "periodOfGraceAdjustment": true
         |    },
         |    "allowances": {
         |      "annualInvestmentAllowance": 100.25,
         |      "otherCapitalAllowance": 100.25,
         |      "propertyAllowance": 100.25,
         |      "electricChargePointAllowance": 100.25
         |    }
         |  },
         |  "foreignProperty": [
         |    {
         |      "countryCode": "FRA",
         |      "adjustments": {
         |        "privateUseAdjustment": 100.25,
         |        "balancingCharge": 100.25
         |      },
         |      "allowances": {
         |        "annualInvestmentAllowance": 100.25,
         |        "costOfReplacingDomesticItems": 100.25,
         |        "zeroEmissionsGoodsVehicleAllowance": 100.25,
         |        "propertyAllowance": 100.25,
         |        "otherCapitalAllowance": 100.25,
         |        "electricChargePointAllowance": 100.25
         |      }
         |    }
         |  ],
         |  "links": [
         |    {
         |      "href": "/individuals/business/property/AA123456A/XAIS12345678910/annual/2021-22",
         |      "method": "PUT",
         |      "rel": "amend-property-annual-submission"
         |    },
         |    {
         |      "href": "/individuals/business/property/AA123456A/XAIS12345678910/annual/2021-22",
         |      "method": "GET",
         |      "rel": "self"
         |    },
         |    {
         |      "href": "/individuals/business/property/AA123456A/XAIS12345678910/annual/2021-22",
         |      "method": "DELETE",
         |      "rel": "delete-property-annual-submission"
         |    }
         |  ]
         |}
         |""".stripMargin)

    val desResponseBody = Json.parse(
      s"""
         |{
         |  "foreignFhlEea": {
         |    "adjustments": {
         |      "privateUseAdjustment": 100.25,
         |      "balancingCharge": 100.25,
         |      "periodOfGraceAdjustment": true
         |    },
         |    "allowances": {
         |      "annualInvestmentAllowance": 100.25,
         |      "otherCapitalAllowance": 100.25,
         |      "propertyAllowance": 100.25,
         |      "electricChargePointAllowance": 100.25
         |    }
         |  },
         |  "foreignProperty": [
         |    {
         |      "countryCode": "FRA",
         |      "adjustments": {
         |        "privateUseAdjustment": 100.25,
         |        "balancingCharge": 100.25
         |      },
         |      "allowances": {
         |        "annualInvestmentAllowance": 100.25,
         |        "costOfReplacingDomesticItems": 100.25,
         |        "zeroEmissionsGoodsVehicleAllowance": 100.25,
         |        "propertyAllowance": 100.25,
         |        "otherCapitalAllowance": 100.25,
         |        "electricChargePointAllowance": 100.25
         |      }
         |    }
         |  ]
         |}
         |""".stripMargin)

    def setupStubs(): StubMapping

    def uri: String = s"/$nino/$businessId/annual/$taxYear"

    def desUri: String = s"/income-tax/business/property/annual/$nino/$businessId/$taxYear"

    def request(): WSRequest = {
      setupStubs()
      buildRequest(uri)
        .withHttpHeaders((ACCEPT, "application/vnd.hmrc.1.0+json"))
    }

    def errorBody(code: String): String =
      s"""
         |      {
         |        "code": "$code",
         |        "reason": "des message"
         |      }
    """.stripMargin
  }

  "calling the retrieve endpoint" should {

    "return a 200 status code" when {

      "any valid request is made" in new Test {

        override def setupStubs(): StubMapping = {
          AuditStub.audit()
          AuthStub.authorised()
          MtdIdLookupStub.ninoFound(nino)
          DesStub.onSuccess(DesStub.GET, desUri, Status.OK, desResponseBody)
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.OK
        response.json shouldBe responseBody
        response.header("X-CorrelationId").nonEmpty shouldBe true
        response.header("Content-Type") shouldBe Some("application/json")
      }
    }
    "return error according to spec" when {

      "validation error" when {
        def validationErrorTest(requestNino: String, requestBusinessId: String, requestTaxYear: String,
                                expectedStatus: Int, expectedBody: MtdError): Unit = {
          s"validation fails with ${expectedBody.code} error" in new Test {

            override val nino: String = requestNino
            override val businessId: String = requestBusinessId
            override val taxYear: String = requestTaxYear


            override def setupStubs(): StubMapping = {
              AuditStub.audit()
              AuthStub.authorised()
              MtdIdLookupStub.ninoFound(requestNino)
            }

            val response: WSResponse = await(request().get())
            response.status shouldBe expectedStatus
            response.json shouldBe Json.toJson(expectedBody)
          }
        }

        val input = Seq(
          ("AA123", "XAIS12345678910", "2021-22", Status.BAD_REQUEST, NinoFormatError),
          ("AA123456A", "203100", "2021-22", Status.BAD_REQUEST, BusinessIdFormatError),
          ("AA123456A", "XAIS12345678910", "2020", Status.BAD_REQUEST, TaxYearFormatError),
          ("AA123456A", "XAIS12345678910", "2020-22", Status.BAD_REQUEST, RuleTaxYearRangeInvalidError),
          ("AA123456A", "XAIS12345678910", "2019-20", Status.BAD_REQUEST, RuleTaxYearNotSupportedError)
        )


        input.foreach(args => (validationErrorTest _).tupled(args))
      }

      "des service error" when {
        def serviceErrorTest(desStatus: Int, desCode: String, expectedStatus: Int, expectedBody: MtdError): Unit = {
          s"des returns an $desCode error and status $desStatus" in new Test {


            override def setupStubs(): StubMapping = {
              AuditStub.audit()
              AuthStub.authorised()
              MtdIdLookupStub.ninoFound(nino)
              DesStub.onError(DesStub.GET, desUri, desStatus, errorBody(desCode))
            }

            val response: WSResponse = await(request().get())
            response.status shouldBe expectedStatus
            response.json shouldBe Json.toJson(expectedBody)
          }
        }

        val input = Seq(
          (Status.BAD_REQUEST, "INVALID_TAXABLE_ENTITY_ID", Status.BAD_REQUEST, NinoFormatError),
          (Status.BAD_REQUEST, "INVALID_INCOME_SOURCE_ID", Status.BAD_REQUEST, BusinessIdFormatError),
          (Status.NOT_FOUND, "NO_DATA_FOUND", Status.NOT_FOUND, NotFoundError),
          (Status.BAD_REQUEST, "INVALID_CORRELATIONID", Status.INTERNAL_SERVER_ERROR, DownstreamError),
          (Status.BAD_REQUEST, "INVALID_PAYLOAD", Status.INTERNAL_SERVER_ERROR, DownstreamError),
          (Status.BAD_REQUEST, "INVALID_TAX_YEAR", Status.INTERNAL_SERVER_ERROR, DownstreamError),
          (Status.INTERNAL_SERVER_ERROR, "SERVER_ERROR", Status.INTERNAL_SERVER_ERROR, DownstreamError),
          (Status.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE", Status.INTERNAL_SERVER_ERROR, DownstreamError)
        )

        input.foreach(args => (serviceErrorTest _).tupled(args))
      }
    }
  }
}
