#!groovy​
pipeline {

	//TODO: Change to none and specify agent per each step
	agent any 

	tools {
		maven 'Maven 3.5.0'
	}
	environment {
		GIT_COMMITER_NAME = "Gencat Jenkins"
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
	    	steps {
	    		echo "Ciberseguretat: Fortify"
	    	}
	    }

	    stage('Ciberseguretat: ZAP') {
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

       /* stage ('Generació Tag BUILD') {
            //Si el PipeLine ha arribat fins aquí, la versió de codi és prou estable com per mereixer la  generació del tag
             steps {
               script {
	               def pom = readMavenPom file: 'pom.xml'
		      	   //Si la versió es SNAPSHOT o ja existeix tirar-la enrera
	               if(pom == null || pom.version == null || pom.version.contains("SNAPSHOT"))
	                   error "El tag no pot ser buit ni snapshot"

		      	   try {
			           sh("git tag -a ${pom.version} -m 'Jenkins'")
			           withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'JenkinsID', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
			                sh("git config credential.username ${env.GIT_USERNAME}")
			                sh("git config credential.helper '!echo password=\$GIT_PASSWORD; echo'")
			                sh("GIT_ASKPASS=true git push origin --tags")
			           }
			        }
			        catch (Exception ex) {
			        	error "Error generant el tag."
			        }
			        finally {
			        	sh("git config --unset credential.username")
			        	sh("git config --unset credential.helper")
			        }
	         	}
	         }
        } */

        stage ('Generació imatge docker') {
	        agent {
	        	docker { 
	        			 image 'docker:stable-dind'
	        			 args '--privileged' 
	        	}
	    	}
           steps {
	           script {
	           	  dir("src/assembly/docker/app") {
	           	      sh("docker build . -t gencat.azurecr.io/demo-canigo:latest")
	           	  }
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
