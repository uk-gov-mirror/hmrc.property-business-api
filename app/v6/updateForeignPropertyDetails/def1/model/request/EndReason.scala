package v6.updateForeignPropertyDetails.def1.model.request

import shared.utils.enums.Enums

enum EndReason(val toDownstream: String) {
  case `no-longer-renting-property-out` extends EndReason("noLongerRentingPropertyOut")
  case disposal                         extends EndReason("disposal")
  case `added-in-error`                 extends EndReason("addedInError")
}
object EndReason {
  val parser: PartialFunction[String, EndReason] = Enums.parser(values)
}
