pipeline {
    agent {
        label "jenkins-maven-java11"
    }
    environment {
        ORG = 'infobelt'
        APP_NAME = 'spring-boot-jest-ssl'
        CHARTMUSEUM_CREDS = credentials('jenkins-x-chartmuseum')
    }
    stages {
        stage('CI Build and push snapshot') {
            when {
                branch 'PR-*'
            }
            environment {
                PREVIEW_VERSION = "0.0.0-SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER"
                PREVIEW_NAMESPACE = "$APP_NAME-$BRANCH_NAME".toLowerCase()
                HELM_RELEASE = "$PREVIEW_NAMESPACE".toLowerCase()
            }
            steps {
                container('maven') {

                    slackSend(color: 'green', message: "Spring Boot Jest SSL :: Starting PR build [${env.PREVIEW_VERSION}] (${env.BUILD_URL})")

                    sh "mvn versions:set -DnewVersion=$PREVIEW_VERSION"
                    sh "mvn install"
                }
            }
        }
        stage('Build Release') {
            when {
                branch 'master'
            }
            steps {
                container('maven') {

                    slackSend(color: 'good', message: "Spring Boot Jest SSL :: Starting release (${env.BUILD_URL})")

                    // ensure we're not on a detached head
                    sh "git checkout master"
                    sh "git config --global credential.helper store"
                    sh "jx step git credentials"

                    // so we can retrieve the version in later steps
                    sh "echo \$(jx-release-version) > VERSION"

                    script {
                        env.WORKSPACE = pwd()
                        env.VERSION = readFile "${env.WORKSPACE}/VERSION"
                    }

                    slackSend(color: 'good', message: "Spring Boot Jest SSL :: Building ${env.VERSION}")

                    sh "mvn versions:set -DnewVersion=\$(cat VERSION)"
                    sh "jx step tag --version \$(cat VERSION)"
                    sh "mvn clean deploy"

                    sh "git add README.md"
                    sh "git commit -m Update"
                    sh "git push origin master"

                    slackSend(color: 'good', message: "Spring Boot Jest SSL :: Deployed ${env.VERSION}")

                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
        success {
            slackSend(color: 'good', message: "Spring Boot Jest SSL :: Build Success")
        }
        failure {
            slackSend(color: 'danger', message: "Spring Boot Jest SSL :: Build Failed (${env.BUILD_URL})")
        }
    }
}
