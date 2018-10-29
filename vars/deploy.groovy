def call() {
  echo 'Deploying..'
  echo "Service: ${env.SERVICE_DIR}"
  echo "Stage: ${env.STAGE}"
  sh "sls deploy --aws-profile ${env.STAGE} --stage ${env.STAGE}"
  echo "Done!"
}