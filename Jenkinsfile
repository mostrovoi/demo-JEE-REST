#!usr/bin/env groovy
@Library('github.com/mostrovoi/pipeline-library@master') _
/*properties([
    parameters([
        string(defaultValue: 'jenkinsci/jnlp-slave:2.62-alpine', description: '', name: 'JNLP_IMAGE'),
        //string(defaultValue: 'quay.io', description: '', name: 'REGISTRY_SERVER'),
        //string(defaultValue: 'eric-cartman', description: '', name: 'REGISTRY_USERNAME'),
        //password(defaultValue: 'badkitty', description: '', name: 'REGISTRY_PASSWORD'),
        //string(defaultValue: 'quay.io/eric-cartman', description: '', name: 'REGISTRY_REPO'),
        string(defaultValue: 'http://sonarqube.devops', description: '', name: "SONARQUBE_URL")
    ]),
    pipelineTriggers([])
]) */

clientsTemplate {
	dockerTemplate {
	   mavenTemplate(label: 'maven-and-docker-and-kubectl')  { 	
			node('maven-and-docker-and-kubectl') {
				container(name: 'maven') {
					stage("Checkout") {
						git 'https://github.com/mostrovoi/demo-canigo.git'
					}
					stage("Build") {
					    sh "mvn clean package -Dmaven.test.failure.ignore=true"			
					}
				
					stage('Ciberseguretat: CESICAT') {
						echo "Ciberseguretat: Fortify"
						echo "Ciberseguretat: ZAP"
					}

					stage ('Anàlisi de codi estàtic') {
						withSonarQubeEnv("SonarQubeServer") {
						    sh "mvn sonar:sonar -Dsonar.dynamic=reuseReports -Dsonar.host.url=$SONAR_HOST_URL" 
					    }
					} 

					//TODO: Moure fora del node, no cal un executor assignat
					stage("Validació de SonarQube Gatekeeper") {
						timeout(time: 5, unit: 'MINUTES') { 
							def qG = waitForQualityGate()
							if(qG.status == 'OK')
							  echo "Codi acompleix els mínims de qualitat. Enhorabona!"
							else
								error "Codi no acompleix els mínims de qualitat : ${qG.status}"
					 }
				   }
				} 

			    //TODO: Externalitzar el nom del registre i logica a funcions externes
				container(name: 'docker') {
					stage ('Generació imatge docker') {
						 sh("docker build -t gencat.azurecr.io/demo-canigo:latest -f src/assembly/docker/app/Dockerfile .")
					}						   	
				
				
					stage ('Pujar imatge docker al nostre registre') {
						withCredentials([usernamePassword(credentialsId: 'azureRegistryID', passwordVariable: 'REGISTRY_PASSWORD', usernameVariable: 'REGISTRY_USERNAME')]) { 
						  sh("docker login -u ${REGISTRY_USERNAME} -p ${REGISTRY_PASSWORD} gencat.azurecr.io")
						  sh("docker push gencat.azurecr.io/demo-canigo:latest")
						}
					}						   	
				}

				//TODO: Moure a funcio parametritzable
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
			    } 
			  } */

				container(name: 'clients') {
					stage ('Desplegament INT') {
						deployProject{
							stagedProject = 'demo-canigo:latest'
						    resourceLocation = 'src/assembly/kubernetes/kubernetes.yaml'
						    environment = 'dev'
							registry = 'gencat.azurecr.io'
						}
					}
				}

				container(name: 'maven') {
					stage ('Smoke Test INT') {
					 	//TODO: Maven and run selenium
					 	echo "Smoke test int"
					}
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
	}
}


