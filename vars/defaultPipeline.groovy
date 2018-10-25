#!/usr/bin/env groovy

def call() {
  pipeline {
    agent any
    stages {
      stage('Initialize') {
        steps {
          script {
            dir(env.SERVICE_DIR) {
              initialize()
            }
          }
        }
      }

      stage('Build') {
        steps {
          script {
            dir(env.SERVICE_DIR) {
              build()
            }
          }
        }
      }

      stage ('Test') {
        steps {
          parallel (
            "Chrome": { echo 'Google Chrome' }
            "Firefox": { echo 'Firefox' }
            "Run npm tests": {
              script {
                dir(env.SERVICE_DIR) {
                  test()
                }
              }
            },
          )
        }
      }

      stage('Approve'){
        steps {
          script {
            dir(env.SERVICE_DIR) {
              approval()
            }
          }
        }
      }

      stage('Deploy'){
        steps {
          script {
            dir(env.SERVICE_DIR) {
              deploy()
            }
          }
        }
      }
    }
    post {
      always {
        slackNotifier(currentBuild.currentResult)
      }
    }
  }
}