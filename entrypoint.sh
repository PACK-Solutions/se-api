#!/bin/sh
# Docker entrypoint for Suite Epargne API
# - Conditionally enable Datadog Java agent when DD_AGENT_ENABLED=true
# - Never downloads the agent at runtime; inclusion is decided at image build time

set -e

# Default JAVA_OPTS if not provided
JAVA_OPTS=${JAVA_OPTS:-"-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"}

# Start application (optionally with Datadog agent if present and enabled)
if [ "${DD_AGENT_ENABLED:-false}" = "true" ]; then
  if [ -f /app/dd-java-agent.jar ]; then
    echo "Datadog enabled: starting with Java agent"
    exec java $JAVA_OPTS \
      -javaagent:/app/dd-java-agent.jar \
      -Ddd.logs.injection=true \
      -Ddd.trace.sample.rate=1 \
      -jar /app/app.jar
  else
    echo "[WARN] DD_AGENT_ENABLED=true but /app/dd-java-agent.jar not found. Starting without Datadog agent."
    exec java $JAVA_OPTS -jar /app/app.jar
  fi
else
  echo "Datadog disabled: starting app without agent"
  exec java $JAVA_OPTS -jar /app/app.jar
fi
