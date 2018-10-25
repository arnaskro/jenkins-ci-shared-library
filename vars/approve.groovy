def call() {
  CHOICE = input message: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?", parameters: [choice(name: "Deploy ${env.SERVICE_DIR} to ${env.STAGE}?", choices: 'no\nyes', description: 'Choose "yes" if you want to deploy this build')]

  if (CHOICE == 'no') {
    error 'Deployment cancelled.'
  }
}