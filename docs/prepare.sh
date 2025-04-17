#!/usr/bin/env bash

set -eux pipefail

cwd=$(cd "$(dirname "$0")"; pwd)

pushd $cwd/..
for pl in playtest-core playtest-http playtest-jdbc playtest-wiremock ;
do
  echo 'steps:' > $cwd/_data/$pl.yaml
  mvn --no-transfer-progress clean compile -Pannotation-processing -pl=${pl} \
      -Dorg.slf4j.simpleLogger.defaultLogLevel=error \
      | awk '{ print "  - " $1 }' >> $cwd/_data/$pl.yaml
done
