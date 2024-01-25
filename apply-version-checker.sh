#!/usr/bin/env bash

set -e
set -o pipefail

apply() {
  sed -i.bak 's/^\s*\/\/\///' "$1" && rm "$1.bak"
}

apply settings.gradle.kts
apply build.gradle.kts
