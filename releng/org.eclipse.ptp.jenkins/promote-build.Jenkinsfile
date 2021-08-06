pipeline {
  agent any
  parameters {
    booleanParam(defaultValue: true, description: 'Do a dry run of the build. All commands will be echoed.First run with this on, then when you are sure it is right, choose rebuild in the passing job and uncheck this box', name: 'DRY_RUN')
    string(defaultValue: 'updates/photon', description: 'The relative path in PTP downloads area to publish promoted build to (e.g. updates/photon, builds/photon/milestones/2019-09-M1, builds/remote/photon/milestones/2019-09-M1)', name: 'PTP_PUBLISH_LOCATION')
    choice(choices: ['ptp', 'remote', 'photran'], description: 'The PTP project name being promoted from (e.g. ptp, remote). The CI job will be $PROJECT-build', name: 'PROJECT')
    string(defaultValue: '12345', description: 'The CI build number being promoted from', name: 'PTP_BUILD_NUMBER')
  }
  stages {
    stage('Upload') {
      steps {
        sshagent ( ['projects-storage.eclipse.org-bot-ssh']) {
          git branch: 'master', url: 'https://git.eclipse.org/r/ptp/org.eclipse.ptp.git'
          sh './releng/org.eclipse.ptp.jenkins/promote-build.sh'
        }
      }
    }
  }
}
