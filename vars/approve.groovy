def call() {
  slackSend(channel: 'temp-notification-dev', message: "<!here> Waiting for approval to deploy *${env.SERVICE}* to *${env.STAGE}* environment")

  def FEEDBACK = input message: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?", submitterParameter: 'submitter', parameters: [choice(name: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?", choices: 'no\nyes', description: 'Choose "yes" if you want to deploy this build')]

  if (FEEDBACK == 'no') {
    error 'Deployment cancelled.'
    slackSend(channel: 'temp-notification-dev', color: 'danger', message: "Deployment of *${env.SERVICE}* to *${env.STAGE}*. Not approved by *${FEEDBACK.submitter}*")
  } else {
    slackSend(channel: 'temp-notification-dev', color: 'good', message: "Deployment of *${env.SERVICE}* to *${env.STAGE}*. Approved by *${FEEDBACK.submitter}*")
  }
}