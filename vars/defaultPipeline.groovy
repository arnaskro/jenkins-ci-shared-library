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
          [key: 'service_changes', value: '$.commits[*].modified[?(@=~ /'+SERVICE_DIR+'.*/i)]']
        ],
        
        causeString: 'Triggered on $ref',
        printContributedVariables: true,
        printPostContent: true,
        silentResponse: false,
        
        token: 'IJ58saMFRP0p',
        
        // (\[..*\]) - checks if there are any changes 
        // (tags/.*) - checks for tags
        // (heads/(master|development)) - checks for certain branches
        regexpFilterExpression: '((\[..*\])-refs/((tags/.*)|(heads/(master|development))))'
        regexpFilterText: '$service_changes-$ref',
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