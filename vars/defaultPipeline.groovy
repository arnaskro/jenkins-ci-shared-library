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
      always {
        slackNotifier(currentBuild.currentResult)
      }
    }
  }

  // Functions
  void initialize() {
    echo 'Initializing..'
    def node = tool name: 'nodejs8', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
    env.PATH = "${node}/bin:${env.PATH}"
    env.NODE_ENV = 'test'

    // TODO: figure out the stage automatically
    env.STAGE = 'testing'

    sh 'ls'
    sh 'node -v'
    sh 'npm -v'
    sh 'printenv'
  }

  void build() {
    echo 'Building dependencies..'
    sh 'npm i'
  }

  void test() {
    echo 'Testing..'
    sh 'ls'
    sh 'npm test'
  }

  void approval() {
    CHOICE = input message: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?", parameters: [choice(name: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?", choices: 'no\nyes', description: 'Choose "yes" if you want to deploy this build')]

    if (CHOICE == 'no') {
      error 'Deployment cancelled.'
    }
  }

  void deploy() {
    echo 'Deploying..'
    echo "Service: ${env.SERVICE_DIR}"
    echo "Stage: ${env.STAGE}"
    sh "sls deploy --aws-profile ${env.STAGE} --stage ${env.STAGE}"
  }
}