#!groovy​
pipeline {
	//Making sure that no executor is assigned unnecessarily
	//This forces to assign an agent per each stage
	agent none 
	stages {
        stage ('Build')  {
        	steps {
	        	agent { docker 'maven:3-alpine' }
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
            //TODO: Definir com es realitzaran aquests test i si la seva execució es controlarà per polítiques
            echo "Commit test aqui" 
        }
        stage ('Generació Tag BUILD') {
            //Si el PipeLine ha arribat fins aquí, la versió de codi és prou estable com per mereixer la  generació del tag
            
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'MyID', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
                sh("git tag -a some_tag -m 'Jenkins'")
                sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@<REPO> --tags')
            } 
        }
        stage ('INT') {
            echo "-----------------> Inici: EFECTUANT DESPLEGAMENT AUTOMÀTIC A INT <-----------------"
            echo "-----------------> FI: EFECTUANT DESPLEGAMENT AUTOMÀTIC A INT <-----------------"
        }
        stage ('Smoke Test INT') {
        
        }
        stage ('PRE') {
       		 echo "-----------------> Inici: EFECTUANT PETICIÓ DESPLEGAMENT A PRE <-----------------"
             echo "-----------------> Fi: EFECTUANT PETICIÓ DESPLEGAMENT A PRE <-----------------"
        }
        stage ('Smoke Test PRE') {
        	echo "Smoke Test de PRE"
        }
        
        stage ('Acceptance Test PRE') {
           echo "Acceptance Test PRE"
        }
        // Fi Acceptancy TEST
        
        // Inici Exploratory TEST
        stage ('Exploratory Test PRE') {
        	echo "Exploratory Test PRE"
        }
        // Fi Exploratory TEST
        
        
         // Inici Generació TAG DEFINITIU
        stage ('Generació Tag DEFINITIU') {
        	echo "Generació Tag DEFINITIU"
		}
			// Fi Generació TAG DEFINITIU
        
        // Inici PRO
        stage ('PRO') {
			echo "-----------------> Inici: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------"
			echo "-----------------> Fi: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------"
        }
        // Fi PRO
        
        // Inici Smoke TEST
        stage ('Smoke Test') {
			def userInput3 = input(
			    id: 'userInput3', message: 'Continuar quan es rebi confirmació de desplegament a PRO.', parameters: [
			    // [$class: 'TextParameterDefinition', defaultValue: 'yesWeCan', description: 'Commit', name: 'commitTest']
			])    
        }
    }
}
