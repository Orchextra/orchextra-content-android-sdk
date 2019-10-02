#!groovy

@Library('github.com/red-panda-ci/jenkins-pipeline-library@v2.6.1') _

// Initialize global config
cfg = jplConfig('ocmsdk', 'android', '', [hipchat: '', slack: '#integrations', email: 'qa+ocmsdk@gigigo.com,jose.benito@gigigo.com'])
cfg.commitValidation.enabled = false
cfg.androidPackages = '"build-tools;24.0.3" "platforms;android-24" "build-tools;25.0.0" "platforms;android-25" "build-tools;27.0.3" "platforms;android-27"'
cfg.archivePattern = "ocmsdk/build/outputs/aar/ocmsdk-ocm-release.aar"

pipeline {
    agent none

    stages {
        stage ('Initialize') {p
            agent { label 'docker' }
            steps  {
                jplStart(cfg)
            }
        }
        stage ('Build') {
            agent { label 'docker' }
            steps  {
                jplBuild(cfg)
            }
        }
        stage ('Release confirm') {
            when { expression { env.BRANCH_NAME.startsWith('release/v') || env.BRANCH_NAME.startsWith('hotfix/v') } }
            steps {
                jplPromoteBuild(cfg)
            }
        }
        stage ('Release finish') {
            agent { label 'docker' }
            when { expression { env.BRANCH_NAME.startsWith('release/v') || env.BRANCH_NAME.startsWith('hotfix/v') } }
            steps {
                jplCloseRelease(cfg)
            }
        }
    }

    post {
        always {
            jplPostBuild(cfg)
        }
    }

    options {
        timestamps()
        ansiColor('xterm')
        buildDiscarder(logRotator(artifactNumToKeepStr: '20',artifactDaysToKeepStr: '30'))
        disableConcurrentBuilds()
        skipDefaultCheckout()
        timeout(time: 7, unit: 'DAYS')
    }
}
