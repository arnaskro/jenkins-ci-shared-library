#!/usr/bin/env groovy

HAS_SERVICE_CHANGES = sh (
    script: "git diff --name-only $GIT_PREVIOUS_COMMIT $GIT_COMMIT $SERVICE_DIR",
    returnStatus: true
) == 0

def call() {
  pipeline {
    agent { label 'build-node' }

    environment {
      INCLUDES_CHANGES_FOR_THE_SERVICE = HAS_SERVICE_CHANGES
    }

    triggers {
      GenericTrigger(
        genericVariables: [
          [key: 'ref', value: '$.ref']
        ],
        
        causeString: 'Triggered on $ref',
        
        token: 'IJ58saMFRP0p',
        
        printContributedVariables: true,
        printPostContent: true,
        
        silentResponse: false,
        
        regexpFilterText: '$ref',
        regexpFilterExpression: '(refs/heads/(master|development))'
      )
    }

    stages {
      stage('Initialize') {
        steps {
          script {
            sh "echo $INCLUDES_CHANGES_FOR_THE_SERVICE"
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