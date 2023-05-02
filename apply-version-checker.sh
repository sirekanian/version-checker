#!/usr/bin/env bash

set -e
set -o pipefail

sed -i 's/^\s*\/\/\///' settings.gradle.kts build.gradle.kts
