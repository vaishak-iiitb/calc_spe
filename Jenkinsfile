pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '15'))
        timeout(time: 20, unit: 'MINUTES')
    }

    environment {
        DOCKERHUB_USER = 'vaishak-iiitb'
        IMAGE_NAME     = "${DOCKERHUB_USER}/calculator-api"
        IMAGE_TAG      = "${env.BUILD_NUMBER}"
        DOCKER_IMAGE   = "${IMAGE_NAME}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'git log -1 --pretty=format:"%h %an: %s"'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build --pull \
                        -t "$IMAGE_NAME:$IMAGE_TAG" \
                        -t "$IMAGE_NAME:latest" .
                '''
            }
        }

        stage('Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                                                  usernameVariable: 'DH_USER',
                                                  passwordVariable: 'DH_PASS')]) {
                    sh '''
                        echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
                        docker push "$IMAGE_NAME:$IMAGE_TAG"
                        docker push "$IMAGE_NAME:latest"
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker compose pull calculator
                    docker compose up -d --build --remove-orphans --wait --wait-timeout 120
                    docker compose ps

                    # Verify through the proxy from inside the nginx container
                    docker compose exec -T nginx wget -qO- http://127.0.0.1/actuator/health | grep -q '"status":"UP"'
                    docker compose exec -T nginx wget -qO- "http://127.0.0.1/api/v1/add?a=2&b=3" | grep -q '"result":5'
                    echo "Deployment healthy: $IMAGE_NAME:$IMAGE_TAG"
                '''
            }
        }
    }
}
