//S'ha de definir la tool Maven amb nom M3 i path que correspongui

def mvnHome
env.TITOL
env.OBSERVACIONS
def repositoryPath = "https://github.com/mostrovoi/demo-canigo.git"


node {
    try{
                    
        env.TITOL = "Petició de desplegament"
        env.OBSERVACIONS = "Observacions de petició de proves"
    	env.STAGE_NAME = "Settings inicials"
        // Global definitions
        // deployUtilities = load "${env.pathTasquesAnt}" + 'deployUtilitiesV2.groovy'
        mvnHome = tool 'M3'
        
        // Inici CHECKOUT
        stage ('Checkout') {
            dir('treball')
            {
                git changelog: false, poll: false, url: "${repositoryPath}", branch: "master" 
            }
        }
        // Fi CHECKOUT    
        
        // Inici BUILD
        stage ('Build') {
            //sh "${mvnHome}/bin/mvn package -Dmaven.test.skip=true -f treball/pom.xml"
        }
        // Fi BUILD
        
        // Inici Unit TEST
        stage ('Unit Test') {
			//sh "${mvnHome}/bin/mvn test -Dmaven.test.ignore -f treball/pom.xml"
        }
        // Fi Unit TEST
        
        // Inici Sonar:ACE
        stage ('Anàlisi de codi estàtic') {
            // TODO: Integrar amb eina anàlisi statics
             println("SonarQubeServer" )
               // requires SonarQube Scanner 2.8+
    	 	
    		withSonarQubeEnv('SonarQubeServer') {
    			//TODO: Figure out how to automatically generate values for projecteKey and sources
    			//Another options is sonar-project.properties file specific to a project
      			sh '${mvnHome}/bin/mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
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
            println("Generacio tag" )
            //TODO: Enllestir aquesta part. Si el PipeLine ha arribat fins aquí, la versió de codi és prou estable com per mereixer la  generació del tag
            
        /*    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'MyID', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
                sh("git tag -a some_tag -m 'Jenkins'")
                sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@<REPO> --tags')
            } */
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
        // Fi Smoke TEST

    } catch (Exception e) {
    	println("-----------------> EXCEPCION <-----------------")
    	error("S'ha produït una excepció al STAGE ${env.STAGE_NAME} \n " + e)
    	//currentBuild.result = 'FAILURE'
    	emailext subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - FAILURE!", to: "oscar.perez_gov.ext@gencat.cat",body: "${e.message}"
    }
}
