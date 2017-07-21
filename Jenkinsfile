#!groovy​
pipeline {
	agent any 
	//agent { 
	//	docker 'maven:3-alpine' 
	//}
	tools {
		maven 'Maven 3.5.0'
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
	    		sh "mvn package -Dmaven.test.skip=true"
	   		}
	    }
        //stage ('Unit Test') {
		//	sh "mvn test -Dmaven.test.ignore"
        //}

        stage ('Anàlisi de codi estàtic') {
        	steps {
	             // requires SonarQube Scanner 2.8+      	
	    		withSonarQubeEnv('SonarQubeServer') {
	    			//TODO: Figure out how to automatically generate values for projecteKey and sources for non maven projects
	      			sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar -Dsonar.dynamic=reuseReports"
	   			}
   			}
        }
        stage ('Commit Test') {          
            steps {
            	//TODO: Definir com es realitzaran aquests test i si la seva execució es controlarà per polítiques
            	echo "Commit test aqui" 
        	}
        }
        stage ('Generació Tag BUILD') {
            //Si el PipeLine ha arribat fins aquí, la versió de codi és prou estable com per mereixer la  generació del tag
            steps {
	          //  withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'MyID', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
	          //      sh("git tag -a some_tag -m 'Jenkins'")
	          //      sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@<REPO> --tags')
	           // } 
	           echo "Generació del tag build"
            }
        }
        stage ('INT') {
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
        stage ('PRE') {
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
		
		stage ('PRO') {
			steps {
				echo "-----------------> Inici: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------"
				echo "-----------------> Fi: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------"
        	}
        }
    
    //   stage ('Smoke Test') {
	//		def userInput3 = input(
	//		    id: 'userInput3', message: 'Continuar quan es rebi confirmació de desplegament a PRO.', parameters: [
			    // [$class: 'TextParameterDefinition', defaultValue: 'yesWeCan', description: 'Commit', name: 'commitTest']
	//		])    
     //   }
        //post {
		 //   always {
		  //    junit '**/target/*.xml' 
		   // }
		   // failure {
		   //   echo 'Failed!'
		   // }
		   // success {
		    //  echo 'Done!'
		   // }
	   // }
    }
}
