def call() {
  echo 'Initializing..'
  def node = tool name: 'nodejs8', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
  env.PATH = "${node}/bin:${env.PATH}"
  env.NODE_ENV = 'test'

  // TODO: figure out the stage automatically
  env.STAGE = 'testing'

  sh 'ls'
  sh 'node -v'
  sh 'npm -v'
  sh 'printenv'
}