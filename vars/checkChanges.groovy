#!/usr/bin/env groovy

def call() {
  node {
    sh "printenv"
    echo env.before
    echo env.after
    echo env.SERVICE_DIR

    hasChanges = sh (
      script: "git diff --name-only ${env.before} ${env.after} ${env.SERVICE_DIR}",
      returnStatus: true
    ) != 0

    if (hasChanges) {
      return 1
    } else {
      return 0
    }
  }
}