pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        echo 'This is through blueocean'
        sleep 10
      }
    }
    stage('Second') {
      steps {
        sh 'pwd'
      }
    }
  }
}