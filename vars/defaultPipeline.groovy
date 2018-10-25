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
          script {
            dir(env.SERVICE_DIR) {
              test()
            }
          }
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
      success {
        slackNotifier(currentBuild.currentResult)
      }
      failure {
        slackNotifier(currentBuild.currentResult)
      }
      unstable {
        slackNotifier(currentBuild.currentResult)
      }
    }
  }
}