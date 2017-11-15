package finatra.filters

import com.twitter.finagle.http.filter.AddResponseHeadersFilter

object CorsFilter {
  def apply(origin: String = "*", methods: String = "GET", headers: String = "x-requested-with") =
    new AddResponseHeadersFilter(
      Map("Access-Control-Allow-Origin"  -> origin,
          "Access-Control-Allow-Methods" -> methods,
          "Access-Control-Allow-Headers" -> headers))
}
