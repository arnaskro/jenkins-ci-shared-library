#!/usr/bin/env groovy

def call() {
  sh 'printenv'
  return sh (
    script: "git diff --name-only $GIT_PREVIOUS_COMMIT $GIT_COMMIT $SERVICE_FOLDER",
    returnStatus: true
  ) != 0
}