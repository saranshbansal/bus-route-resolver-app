#!/usr/bin/env bash

# This script will run under bus_route_challenge folder irrespective of where it was invoked from
cd $(dirname $0)

dev_build() {
  true
}

dev_run() {
  # Do what you need to run your app in the foreground
  # e.g. java -jar target/magic.jar $*
  mv spring-boot:run
  sleep 600
}

dev_smoke() {
  if _run_smoke; then
    echo "Tests Passed"
    exit 0
  else
    echo "Tests Failed"
    exit 1
  fi
}

_run_smoke() {
  baseUrl="http://localhost:8088"
  echo "Running smoke tests on $baseUrl..." && \
    (curl -fsS "$baseUrl/api/direct?dep_sid=3&arr_sid=4" | grep -E 'true|false') && \
    (curl -fsS "$baseUrl/api/direct?dep_sid=0&arr_sid=1" | grep -E 'true|false')
}

docker_build() {
  docker build -t goeuro:devtest .
}

docker_run() {
  docker run --rm -it -p 8088:8088 goeuro:devtest
}

docker_smoke() {
  containerId=$(docker run -d goeuro:devtest)
  echo "Waiting 10 seconds for service to start..."
  sleep 10
  docker exec $containerId /src/service.sh dev_smoke
  retval=$?
  docker rm -f $containerId
  exit $retval
}

usage() {
  cat <<EOF
Usage:
  $0 <command> <args>
Local machine commands:
  dev_build        : builds and packages your app
  dev_run <file>   : starts your app in the foreground
  dev_smoke        : runs our integration test suite on localhost
Docker commands:
  docker_build     : packages your app into a docker image
  docker_run       : runs your app using a docker image
  docker_smoke     : runs same smoke tests inside a docker container
EOF
}

action=$1
action=${action:-"usage"}
action=${action/help/usage}
shift
if type -t $action >/dev/null; then
  echo "Invoking: $action"
  $action $*
else
  echo "Unknown action: $action"
  usage
  exit 1
fi
