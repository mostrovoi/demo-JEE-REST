#!groovy​
pipeline {

	agent any 

	tools {
		maven 'Maven 3.5.0'
	}
	environment {
		GIT_COMMITER_NAME = "GenCat Jenkins"
		GIT_COMMITER_EMAIL = "jenkins@jenkins.id"
		MAIL_RECEIVER = "oscar.perez_gov.ext@gencat.cat"
	}
	stages {
		
		stage('Inicialització') {
			steps {
				sh '''
					echo "PATH = ${PATH}"
					echo "M2_HOME = ${M2_HOME}"
				   '''
			}
		}
        stage ('Build')  {
        	steps {
	    		sh "mvn clean package -Dmaven.test.failure.ignore=true"
	   		}
	    }

	    stage('Ciberseguretat: Fortify') {
	    	//TODO: xxx
	    	steps {
	    		echo "Ciberseguretat: Fortify"
	    	}
	    }

	    stage('Ciberseguretat: ZAP') {
	    	//TODO: xxx
	    	steps {
	    		echo "Ciberseguretat : ZAP"
	    	}
	    }

        stage ('Anàlisi de codi estàtic') {
        	steps {
	             // requires SonarQube Scanner 2.8+      	
	    		withSonarQubeEnv('SonarQubeServer') {
	      			sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar -Dsonar.dynamic=reuseReports"
	   			}
   			}
        }

        stage("Validació de SonarQube Gatekeeper") {
        	steps {
        		script {
        			timeout(time: 5, unit: 'MINUTES') { 
	        			def qG = waitForQualityGate()
	        			if(qG.status != 'OK') {
	        				error "Codi no acompleix els mínims de qualitat : ${qG.status}"
	        			}
	        		}
        		}
        	}
        }

        stage ('Generació Tag BUILD') {
            //Si el PipeLine ha arribat fins aquí, la versió de codi és prou estable com per mereixer la  generació del tag
            steps {
               script {
	               def pom = readMavenPom file: 'pom.xml'
		      	   //Si la versió es SNAPSHOT tirar-la enrera

		      	   try {
			           withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'JenkinsID', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
			                sh('git config user.email "jenkins@jenkins.com"')
			                sh('git config user.name "Jenkins"')
			                sh("git config credential.username ${env.GIT_USERNAME}")
			                sh("git config credential.helper "!echo password=\$GIT_PASSWORD; echo'")
			                sh("")
			                sh("git tag -a ${pom.version} -m 'Jenkins'")
			                sh("GIT_ASKPASS=true git push origin --tags')
			           }
			        }
			        finally {
			        	sh("git config --unset user.email")
			        	sh("git config --unset user.name")
			        	sh("git config --unset credential.username")
			        	sh("git config --unset credential.helper")
			        }
					echo "Generació del tag build"
	         	}
	         }
        }

        stage ('Desplegament INT') {
            steps {
	            echo "-----------------> Inici: EFECTUANT DESPLEGAMENT AUTOMÀTIC A INT <-----------------"
	            echo "-----------------> FI: EFECTUANT DESPLEGAMENT AUTOMÀTIC A INT <-----------------"
	        }
        }
        stage ('Smoke Test INT') {
         	steps {
         		echo "Smoke test int"
         	}
        }
        stage ('Desplegament PRE') {
        	steps {
       		 echo "-----------------> Inici: EFECTUANT PETICIÓ DESPLEGAMENT A PRE <-----------------"
             echo "-----------------> Fi: EFECTUANT PETICIÓ DESPLEGAMENT A PRE <-----------------"
        	}
        }
        stage ('Smoke Test PRE') {
         	steps {
        		echo "Smoke Test de PRE"
            } 
        }
        
        stage ('Acceptance Test PRE') {
           steps {
           	   echo "Acceptance Test PRE"
           }
        }

        stage ('Exploratory Test PRE') {
        	steps {
        		echo "Exploratory Test PRE"
        	}
        }
        
        stage ('Generació Tag DEFINITIU') {
        	steps {
        		echo "Generació Tag DEFINITIU"
			}
		}
		
		stage ('Desplegament PRO') {
			steps {
				echo "-----------------> Inici: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------"
				echo "-----------------> Fi: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------"
        	}
        }
	   
    	 stage ('Smoke Test') {
    	 	steps {
    	 		echo "Per fer"
    	 	}
	//		def userInput3 = input(
	//		    id: 'userInput3', message: 'Continuar quan es rebi confirmació de desplegament a PRO.', parameters: [
	//		    // [$class: 'TextParameterDefinition', defaultValue: 'yesWeCan', description: 'Commit', name: 'commitTest']
		  //])    
        }
	} 
    post {
		always {
		   //archive "target/**/*"
		   junit 'target/surefire-reports/*.xml' 
		   deleteDir()
		 }
		 success {
		 	echo "${MAIL_RECEIVER}"
		 	//mail to: "${MAIL_RECEIVER}", subject:"BUILD PASSA: ${currentBuild.fullDisplayName}", body "Tot ok"
		 }
		 failure {
		 	echo "${MAIL_RECEIVER}"
		 	//mail to: "${MAIL_RECEIVER}", subject:"BUILD FALLA: ${currentBuild.fullDisplayName}", body "Nope"
		 }
   }
}
