pipeline {
    agent any

    tools {
        maven 'maven'
    }

    triggers {
        pollSCM('* * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master'
                    url: 'https://github.com/alexkor05/bank_rest-main.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}