#!/usr/bin/env groovy

def call(previous_commit, new_commit) {
  node {
    sh "printenv"
    echo previous_commit
    echo new_commit
    echo env.SERVICE_DIR
    
    hasChanges = sh (
      script: "git diff --name-only ${previous_commit} ${new_commit} ${env.SERVICE_DIR}",
      returnStatus: true
    ) != 0

    if (hasChanges) {
      return 1
    } else {
      return 0
    }
  }
}