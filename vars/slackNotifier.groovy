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

  // Build up the object
  def json = new JsonBuilder()
  def root = json [
    {
      fallback: "${status}: ${job_title}"
      color: color
      title: job_title
      title_link: env.RUN_DISPLAY_URL
    }
  ]

  // field_environment.put('title', "Environment");
  // field_environment.put('value', env.STAGE);
  // field_environment.put('short', 1);

  // field_status.put('title', "Status");
  // field_status.put('value', status);
  // field_status.put('short', 1);

  // field_commit.put('title', "Commit ID");
  // field_commit.put('value', env.GIT_COMMIT);
  // field_commit.put('short', 0);

  // // Add them to the fields array
  // attachments.add(field_environment);
  // attachments.add(field_status);
  // attachments.add(field_commit);

  // // Build up the attachment
  // attachment.put('fields', fields);



  slackSend(channel: '@arnas', attachments: root.toString())
}