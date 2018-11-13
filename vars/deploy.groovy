def call() {
  echo 'Deploying..'
  echo "Service: ${env.SERVICE_DIR}"
  echo "Stage: ${env.STAGE}"

  def node = tool name: 'nodejs8', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
  env.PATH = "${node}/bin:${env.PATH}"
  sh 'npm install'

  sh "sls deploy --aws-profile ${env.STAGE} --stage ${env.STAGE}"
}