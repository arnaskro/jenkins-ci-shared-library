#!/usr/bin/env groovy
import groovy.json.JsonBuilder;

def call(String buildResult) {
  // Create JSON object
  // Initial variables
  job_title = "${env.JOB_NAME} (${env.BUILD_DISPLAY_NAME})"
  color = ""
  status = buildResult

  // Conditions
  if ( buildResult == "SUCCESS" ) {
    color = "good"
  } else if( buildResult == "UNSTABLE" ) { 
    color = "warning"
  } else {
    color = "danger"
    status = "FAIL at ${env.STAGE_NAME}"
  }

  def json = new groovy.json.JsonBuilder()
  json (
    "fallback": "${status}: ${job_title}",
    "color": color,
    "title": job_title,
    "title_link": env.RUN_DISPLAY_URL,
    "fields": (
      "title": "Environment",
      "value": env.STAGE,
      "short": 1
    )
    // , (
    //   "title": "Status",
    //   "value": status,
    //   "short": 1
    // ), (
    //   "title": "Commit ID",
    //   "value": env.GIT_COMMIT,
    //   "short": 0
    // )
  )

  print groovy.json.JsonOutput.prettyPrint(json.toString())
  // slackSend(channel: '@arnas', attachments: root.toString())
}