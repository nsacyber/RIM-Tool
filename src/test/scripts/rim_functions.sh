#!/bin/bash
#Rim system test support functions.
#exit status functions for rim create and rim verify.
rim_expected_pass_status(){
  if [ $1 -eq 0 ]; then
     # echo "********"
      echo "PASSED: RIM test \"$2\""
      echo "********"
  else
   #   echo "********"
      echo "FAILED: rim test \"$2\""
      echo "********"
      ((failCount++))
    #     exit 1
  fi
}

rim_expected_fail_status(){
    if [ $1 -ne 0 ]; then
     #   echo "********"
        echo "PASSED: RIM test \"$2\" FAILED as expected."
        echo "********"
#        exit 0
    else
     #   echo "********"
        echo "FAILED: RIM test \"$2\"  PASSED but was expected to FAIL."
        echo "********"
         ((failCount++))
 #       exit 1
    fi
}

