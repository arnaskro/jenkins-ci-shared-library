#!/usr/bin/env groovy

def call() {
  node {
    gitDifferences = sh (
      script: "git diff --name-only ${env.before} ${env.after} ${env.SERVICE_DIR}",
      returnStatus: true
    )

    if (gitDifferences == 0) {
      return "0-${env.ref}"
    } else {
      return "1-${env.ref}"
    }
  }
}