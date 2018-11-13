#!/usr/bin/env groovy

def call() {
  pipeline {
    agent { label 'build-node' }

    triggers {
      GenericTrigger(
        genericVariables: [
          [key: 'ref', value: '$.ref'],
          [key: 'before', value: '$.before'],
          [key: 'after', value: '$.after'],
          [key: 'hasChanges', value: "${checkChanges(before, after)}"],
          [key: 'testas', value: "hello"],
          [key: 'tests', value: "$.after"]
        ],
        
        causeString: 'Triggered on $ref',
        printContributedVariables: true,
        printPostContent: true,
        silentResponse: false,
        
        token: 'IJ58saMFRP0p',
        
        regexpFilterText: '$hasChanges-$ref',
        regexpFilterExpression: '(1-refs/heads/(master|development))'
      )
    }

    stages {
      stage('Initialize') {
        steps {
          script {
            sh "printenv"
            echo "" + env.hasChanges

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
        when {
          expression { STAGE ==~ /(production|prerel)/ }
        }
        steps {
          script {
            dir(env.SERVICE_DIR) {
              approve()
            }
          }
        }
      }

      stage('Deploy'){
        agent { label 'deploy-node' }
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
        script {
          try {
            slackNotifier(currentBuild.currentResult)
          } catch(e) {
            // Simply ignore the error, because the slack notification always succeed
            echo 'Done!'
          }
        }
      }
    }
  }
}