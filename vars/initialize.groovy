def call() {
  echo 'Initializing..'
  def node = tool name: 'nodejs8', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
  env.PATH = "${node}/bin:${env.PATH}"
  env.NODE_ENV = 'test'
  env.STAGE = 'testing'

  // if (env.GIT_BRANCH == 'master') {
  //   env.STAGE = 'prerel'
  // } else (env.GIT_BRANCH == 'development') {
  //   env.STAGE = 'development'
  // }

  sh 'ls'
  sh 'node -v'
  sh 'npm -v'
  sh 'printenv'
}