#!/usr/bin/env groovy

def call(String buildResult) {
  // Create JSON object
  // Initial variables
  job_title = "${env.JOB_NAME} > ${env.SERVICE} (${env.BUILD_DISPLAY_NAME})"
  color = ""
  status = buildResult

  // Only continue if its a known build result
  if (buildResult == "SUCCESS" || buildResult == "UNSTABLE" || buildResult == "FAILURE" ) {

    // Conditions
    if ( buildResult == "SUCCESS" ) {
      color = "good"
    } else if( buildResult == "UNSTABLE" ) { 
      color = "warning"
    } else if( buildResult == "FAILURE" ) { 
      color = "danger"
      status = "FAIL at ${env.STAGE_NAME}"
    }

    // Create fields
    def field_env = [
      "title": "Environment",
      "value": env.STAGE,
      "short": 1
    ]

    def field_status = [
      "title": "Status",
      "value": status,
      "short": 1
    ]

    def field_commit = [
      "title": "Commit ID",
      "value": env.GIT_COMMIT,
      "short": 0
    ]

    def field_service = [
      "title": "Service",
      "value": env.SERVICE,
      "short": 1
    ]

    def attachment = [
      "fallback": "${status}: ${job_title}",
      "color": color,
      "title": job_title,
      "title_link": env.RUN_DISPLAY_URL,
      "fields": [field_env, field_service, field_commit, field_status]
    ]

    // Convert to json
    def json = new groovy.json.JsonBuilder()
    json attachment
    def attachments = "[${json.toString()}]"

    slackSend(channel: 'temp-notification-dev', attachments: attachments)
  }
}