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
              approve()
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