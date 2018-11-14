#!/usr/bin/env groovy

def call() {
  node {
    sh "printenv"
    echo env.before
    echo env.after
    echo env.SERVICE_DIR
    echo env.ref

    gitDifferences = sh (
      script: "git diff --name-only ${env.before} ${env.after} ${env.SERVICE_DIR}",
      returnStatus: true
    )

    if (gitDifferences == 0) {
      return "0-${env.ref}-${env.SERVICE_DIR}-${env.after}"
    } else {
      return "1-${env.ref}-${env.SERVICE_DIR}-${env.after}"
    }
  }
}