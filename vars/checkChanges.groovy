#!/usr/bin/env groovy

def call(previous_commit, new_commit, service_folder) {
  node {
    sh "printenv"
    return sh (
      script: "git diff --name-only ${previous_commit} ${new_commit} ${service_folder}",
      returnStatus: true
    ) != 0
  }
}