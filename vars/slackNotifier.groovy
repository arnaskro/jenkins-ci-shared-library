#!/usr/bin/env groovy
import groovy.json.*

class Node {
   String text
}

def call(String buildResult) {
  // // Create JSON objects
  // JSONArray attachments = new JSONArray();
  // JSONObject attachment = new JSONObject();
  // JSONArray fields = new JSONArray();
  // JSONObject field_environment = new JSONObject();
  // JSONObject field_status = new JSONObject();
  // JSONObject field_commit = new JSONObject();

  // // Initial variables
  // job_title = "${env.JOB_NAME} (${env.BUILD_DISPLAY_NAME})"
  // color = ""
  // status = buildResult

  // // Conditions
  // if ( buildResult == "SUCCESS" ) {
  //   color = "good"
  // } else if( buildResult == "UNSTABLE" ) { 
  //   color = "warning"
  // } else {
  //   color = "danger"
  //   status = "FAIL at ${env.STAGE_NAME}"
  // }

  // // Build up the fields
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
  // attachment.put('fallback', "${status}: ${job_title}");
  // attachment.put('color', color);
  // attachment.put('title', job_title);
  // attachment.put('title_link', env.RUN_DISPLAY_URL);
  // attachment.put('fields', fields);

  // // Add it to the atachments array
  // attachments.add(attachment);

  // slackSend(channel: '@arnas', attachments: attachments.toString())
  test = new Node(text: "Hello")
  slackSend(channel: '@arnas', attachments: new JsonBuilder(test).toPrettyString())
}