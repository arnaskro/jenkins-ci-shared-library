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
          [
            key: 'test', 
            value: '$.commits[*].modified[?(@=~ /sms-api.*/i)]'
          ],
          [
            key: 'test2', 
            value: '$.commits[*].modified[?(@=~ /'+SERVICE_DIR+'.*/i)]'
          ],
          [
            key: 'test23', 
            value: '$.commits[*].modified[?(@=~ /fadfsd.*/i)]'
          ],
          [
            key: 'env1', 
            defaultValue: SERVICE_DIR
          ],
          [
            key: 'env2', 
            defaultValue: env.SERVICE_DIR
          ]
        ],
        
        causeString: 'Triggered on $ref',
        printContributedVariables: true,
        printPostContent: true,
        silentResponse: false,
        
        token: 'IJ58saMFRP0p',
        
        regexpFilterText: '$hasChanges-$ref',
        regexpFilterExpression: '(1-refs/((tags/.*)|(heads/(master|development))))'
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