#!groovy

@Library('github.com/red-panda-ci/jenkins-pipeline-library@v2.2.2') _

// Initialize global config
cfg = jplConfig('ocmsdk', 'android', '', [hipchat: '', slack: '#integrations', email: 'qa+ocmsdk@gigigo.com,jose.benito@gigigo.com'])
cfg.commitValidation.enabled = false
cfg.androidPackages = 'build-tools-24.0.3,android-24,build-tools-25.0.0,android-25,build-tools-26.0.2,android-26'

pipeline {
    agent none

    stages {
        stage ('Initialize') {
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
            when { branch 'release/v*' }
            steps {
                jplPromoteBuild(cfg)
            }
        }
        stage ('Release finish') {
            agent { label 'docker' }
            when { branch 'release/v*' }
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
