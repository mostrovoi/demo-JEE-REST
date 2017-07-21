#!groovy​
pipeline {
	//Making sure that no executor is assigned unnecessarily
	//This forces to assign an agent per each stage
	agent none 
	stages {
        stage ('Build')  {
	        agent { docker 'maven:3-alpine' }
	    	sh "mvn package -Dmaven.test.skip=true"
	    }
        //stage ('Unit Test') {
		//	sh "mvn test -Dmaven.test.ignore"
        //}

        stage ('Anàlisi de codi estàtic') {
             // requires SonarQube Scanner 2.8+
    		withSonarQubeEnv('SonarQubeServer') {
    			//TODO: Figure out how to automatically generate values for projecteKey and sources for non maven projects
      			sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar -Dsonar.dynamic=reuseReports"
   			}
        }
        // Fi Sonar:ACE
        
        // Inici Commit TEST
        stage ('Commit Test') {          
            //TODO: Definir com es realitzaran aquests test i si la seva execució es controlarà per polítiques
            println("Commit test aqui" )
        }
        // Fi Commit TEST
        
        // Inici Generació TAG BUILD
        stage ('Generació Tag BUILD') {
            //Si el PipeLine ha arribat fins aquí, la versió de codi és prou estable com per mereixer la  generació del tag
            
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'MyID', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
                sh("git tag -a some_tag -m 'Jenkins'")
                sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@<REPO> --tags')
            } 
        }
        // Fi Generació TAG BUILD
        
        
        // Inici INT    
        stage ('INT') {
            println("-----------------> Inici: EFECTUANT DESPLEGAMENT AUTOMÀTIC A INT <-----------------")
            println("-----------------> FI: EFECTUANT DESPLEGAMENT AUTOMÀTIC A INT <-----------------")
        }
        // Fi INT
        
        // Inici Smoke TEST
        stage ('Smoke Test INT') {
        
        }
        // Fi Smoke TEST
          
        // Inici PRE
        stage ('PRE') {
       		 println("-----------------> Inici: EFECTUANT PETICIÓ DESPLEGAMENT A PRE <-----------------")
             println("-----------------> Fi: EFECTUANT PETICIÓ DESPLEGAMENT A PRE <-----------------")
        }
        // Fi PRE
        
        // Inici Smoke TEST
        stage ('Smoke Test PRE') {
        	println("Smoke Test de PRE")
        }
        // Fi Smoke TEST
        
        // Inici Acceptance TEST
        stage ('Acceptance Test PRE') {
           println("Acceptance Test PRE")
        }
        // Fi Acceptancy TEST
        
        // Inici Exploratory TEST
        stage ('Exploratory Test PRE') {
        	println("Exploratory Test PRE")
        }
        // Fi Exploratory TEST
        
        
         // Inici Generació TAG DEFINITIU
        stage ('Generació Tag DEFINITIU') {
        	println("Generació Tag DEFINITIU")
		}
			// Fi Generació TAG DEFINITIU
        
        // Inici PRO
        stage ('PRO') {
			println("-----------------> Inici: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------")
			println("-----------------> Fi: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------")
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
