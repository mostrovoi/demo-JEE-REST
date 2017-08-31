#!groovy​
podTemplate(label: 'docker-maven', containers: [
	containerTemplate(name: 'docker', image: 'docker:1.11', ttyEnabled: true, command: 'cat'),
	containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat')],
  	volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]
) {

node('docker-maven') {

        stage ('Build')  {
        	container("maven"){
	    		sh "mvn clean package -Dmaven.test.failure.ignore=true"
	   		}
	    }

	    stage('Ciberseguretat: Fortify & ZAP') {
	    	echo "Ciberseguretat: Fortify"
	    }


        stage ('Anàlisi de codi estàtic') {
             // requires SonarQube Scanner 2.8+      	
    		withSonarQubeEnv('SonarQubeServer') {
      			sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar -Dsonar.dynamic=reuseReports"
   			}
        }

        stage("Validació de SonarQube Gatekeeper") {
			timeout(time: 5, unit: 'MINUTES') { 
    			def qG = waitForQualityGate()
    			if(qG.status != 'OK') {
    				error "Codi no acompleix els mínims de qualitat : ${qG.status}"
    			}
    		}
        }

       /* stage ('Generació Tag BUILD') {
            //Si el PipeLine ha arribat fins aquí, la versió de codi és prou estable com per mereixer la  generació del tag
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
        } */

        stage ('Generació imatge docker') {
	       	  dir("src/assembly/docker/app") {
	       	      sh("docker build . -t gencat.azurecr.io/demo-canigo:latest")
	       	  }
        }
        stage ('Desplegament INT') {
			echo "-----------------> Inici: EFECTUANT DESPLEGAMENT AUTOMÀTIC A INT <-----------------"
			echo "-----------------> FI: EFECTUANT DESPLEGAMENT AUTOMÀTIC A INT <-----------------"
		}

        stage ('Smoke Test INT') {
         	echo "Smoke test int"
        }
        stage ('Desplegament PRE') {
			echo "-----------------> Inici: EFECTUANT PETICIÓ DESPLEGAMENT A PRE <-----------------"
			echo "-----------------> Fi: EFECTUANT PETICIÓ DESPLEGAMENT A PRE <-----------------"
		}
        stage ('Smoke Test PRE') {
        	echo "Smoke Test de PRE"
        }
        
        stage ('Acceptance Test PRE') {
           	echo "Acceptance Test PRE"
        }

        stage ('Exploratory Test PRE') {
        	echo "Exploratory Test PRE"
        }
        
        stage ('Generació Tag DEFINITIU') {
        	echo "Generació Tag DEFINITIU"
		}
		
		stage ('Desplegament PRO') {
			echo "-----------------> Inici: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------"
			echo "-----------------> Fi: EFECTUANT PETICIÖ DESPLEGAMENT A PRO <-----------------"
	    }   
		stage ('Smoke Test') {
			echo "Per fer"
	    }
	 }
}

