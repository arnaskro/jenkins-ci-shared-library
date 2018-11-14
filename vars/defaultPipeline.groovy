#!/usr/bin/env groovy

def call() {
  pipeline {
    agent { label 'build-node' }

    parameters {
      stringParam('has_changes', 'test')
    }

    triggers {
      GenericTrigger(
        genericVariables: [
          [key: 'ref', value: '$.ref'],
          [key: 'before', value: '$.before'],
          [key: 'after', value: '$.after'],
          [
            key: 'has_changes',
            value: '$.testas',
            defaultValue: 'defalt'
          ]
        ],
        
        causeString: 'Triggered on $ref',
        printContributedVariables: true,
        printPostContent: true,
        silentResponse: false,
        
        token: 'IJ58saMFRP0p',
        
        regexpFilterText: '$ref',
        regexpFilterExpression: '(refs/heads/(master|development))'
      )
    }

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