def call() {
  def job_title = "${env.SERVICE} (${env.BUILD_DISPLAY_NAME})"

  slackSend(channel: 'temp-notification-dev', message: "<!here> *<${env.RUN_DISPLAY_URL}|${job_title}>*\nWaiting for approval to deploy *${env.SERVICE}* to *${env.STAGE}* environment")

  def FEEDBACK = input message: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?", submitterParameter: 'submitter', parameters: [choice(name: 'approval', choices: 'no\nyes', description: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?")]

  if (FEEDBACK.approval == 'yes') {
    slackSend(channel: 'temp-notification-dev', color: 'good', message: "Deployment of *${env.SERVICE}* to *${env.STAGE}* was approved by *${FEEDBACK.submitter}*")
  } else {
    slackSend(channel: 'temp-notification-dev', color: 'warning', message: "Deployment of *${env.SERVICE}* to *${env.STAGE}* was not approved by *${FEEDBACK.submitter}*")
    error "Deployment cancelled by ${FEEDBACK.submitter}"
  }
}



