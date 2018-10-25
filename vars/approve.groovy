def call() {
  def job_title = "${env.SERVICE} (${env.BUILD_DISPLAY_NAME})"

  slackSend(channel: 'temp-notification-dev', message: "<!here> *<${env.RUN_DISPLAY_URL}|${job_title}>*\nWaiting for approval to deploy *${env.SERVICE}* to *${env.STAGE}* environment")

  def FEEDBACK = input message: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?", submitterParameter: 'submitter', parameters: [choice(name: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?", choices: 'no\nyes', description: 'Choose "yes" if you want to deploy this build')]

  echo FEEDBACK

  if (FEEDBACK == 'yes') {
    slackSend(channel: 'temp-notification-dev', color: 'good', message: "Deployment of *${env.SERVICE}* to *${env.STAGE}*. Approved by *${FEEDBACK.submitter}*")
  } else {
    error 'Deployment cancelled.'
    slackSend(channel: 'temp-notification-dev', color: 'danger', message: "Deployment of *${env.SERVICE}* to *${env.STAGE}*. Not approved by *${FEEDBACK.submitter}*")
  }
}



