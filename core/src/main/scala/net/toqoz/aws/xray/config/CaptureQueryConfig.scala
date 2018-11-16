package net.toqoz.aws.xray.config

object CaptureQueryConfig extends ConfigReader {
  override val OVERRIDE_ENVIRONMENT_VARIABLE_KEY = "NT_AWS_XRAY_CAPTURE_QUERY"
  override val OVERRIDE_SYSTEM_PROPERTY_KEY = "net.toqoz.aws.xray.captureQuery"

  def isEnabled: Boolean = readValue.contains("on")
}
