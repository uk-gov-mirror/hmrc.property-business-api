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

package shared.controllers.validators.resolvers

import cats.data.Validated
import common.models.errors.PropertyIdFormatError
import shared.models.domain.PropertyId
import shared.models.errors.MtdError

object ResolvePropertyId extends ResolverSupport {

  private val propertyIdRegex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$".r

  val resolver: Resolver[String, PropertyId] =
    ResolveStringPattern(propertyIdRegex, PropertyIdFormatError).resolver.map(PropertyId.apply)

  def apply(value: String): Validated[Seq[MtdError], PropertyId] = resolver(value)

}
