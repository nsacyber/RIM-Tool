#!/bin/bash

# Calls the rim-tool and passes in parameters
scriptDir=$(dirname -- "$(readlink -f -- "${BASH_SOURCE[0]}")")
baseDir=${scriptDir%/*}
libDir=$baseDir"/lib/"
jar="rim-tool-*.jar";
java -jar $libDir$jar "$@"