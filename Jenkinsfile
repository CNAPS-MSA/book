pipeline {
    agent any 
    
    environment {
      PROJECT = "cnaps-project-286804"
      APP_NAME = "book"
      CLUSTER = "jenkins-cd"
      CLUSTER_ZONE = "asia-northeast3-a"
      IMAGE_TAG = "gcr.io/${PROJECT}/${APP_NAME}:1.0.${env.BUILD_NUMBER}"
      JENKINS_CRED = "${PROJECT}"
    }
    
    stages {
        stage('Stage 0') {
            steps {
                echo 'Hello world!' 
            }
        }
        stage('Checkout') {
            steps {
                checkout scm
                sh "rm -rf src/test"
            }
        }
        stage('Build and Push') {
            steps {
                echo "${IMAGE_TAG}"
                sh "./mvnw package -Pprod -DskipTests jib:build -Dimage=${IMAGE_TAG}"
            }
        }
        stage('Deploy Production') {
          // Production branch
          when { branch 'master' }
          steps{
            container('kubectl') {
            // Change deployed image in canary to the one we just built
              sh "kubectl set image deployment/gateway ${APP_NAME}=${APP_NAME}:1.0 --recode"
            }
          }
        }
    }
}
