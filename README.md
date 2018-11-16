# AWS XRay + Scala

## Config

| Environment Variable | System Property | Value |
| - | - | - |
|NT_AWS_XRAY_CAPTURE_QUERY|net.toqoz.aws.xray.captureQuery| `off`, `on` |

### XRay Native Config

https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java-configuration.html

| Environment Variable | System Property |
| - | - |
| AWS_XRAY_TRACING_NAME | com.amazonaws.xray.strategy.tracingName |
| AWS_XRAY_DAEMON_ADDRESS | com.amazonaws.xray.emitters.daemonAddress |
| AWS_XRAY_CONTEXT_MISSING | com.amazonaws.xray.strategy.contextMissingStrategy |
