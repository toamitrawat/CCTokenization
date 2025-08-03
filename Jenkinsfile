pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/toamitrawat/CCTokenization.git'
        EC2_USER = 'ec2-user'  // or ubuntu
        EC2_IP   = '13.234.17.67'   // replace with your EC2 public IP
        JAR_NAME = 'target/tokenization-app.jar'
    }

    parameters {
        booleanParam(name: 'AUTO_DEPLOY', defaultValue: true, description: 'Auto deploy after build')
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout') {
            steps {
                git url: "${GIT_REPO}", branch: 'ec2_deploy', credentialsId: 'github-pat'
            }
        }

        stage('Build') {
            steps {
                sh """
                    mvn clean package -DskipTests
                    cp target/tokenization-0.0.1-SNAPSHOT.jar target/tokenization-app.jar
                """
            }
        }

        stage('Deploy to EC2') {
    when {
        expression { return params.AUTO_DEPLOY == true }
    }
    steps {
        sshagent (credentials: ['ec2-ssh-key']) {
            // Copy JAR to EC2
            sh """
                scp -o StrictHostKeyChecking=no ${JAR_NAME} ${EC2_USER}@${EC2_IP}:/home/${EC2_USER}/
            """

            // Stop old app and start new one
            sh """
    ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_IP} << 'EOF'
        pkill -f tokenization-app.jar || true
        nohup java -jar /home/${EC2_USER}/tokenization-app.jar > app.log 2>&1 &
    EOF
"""

        }
    }
}

    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}
