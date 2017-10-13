#!usr/bin/groovy
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
	  performanceTemplate {
	   mavenTemplate(label: 'maven-and-docker-and-kubectl')  { 	
			node('maven-and-docker-and-kubectl') {
				container(name: 'maven') {
					stage("Checkout") {
						//checkout scm
						git 'https://github.com/mostrovoi/demo-canigo.git'
					}
					
					stage("Build") {
					    sh "mvn clean package -Dmaven.test.failure.ignore"
					    //TODO: Extend to wars and non java based
					    archiveArtifacts artifacts: '**/target/*.jar'
					    junit healthScaleFactor: 1.0, testResults: 'target/surefire-reports/TEST*.xml'	
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
							  echo "SONAR: Codi acompleix els mínims de qualitat. Enhorabona!"
							else
								error "SONAR: Codi no acompleix els mínims de qualitat : ${qG.status}"
					 }
				   } 
				} 

			    //TODO: Externalitzar el nom del registre i logica a funcions externes
				container(name: 'docker') {
					stage ('Generació imatge docker') {
						 sh("docker build -t gencat.azurecr.io/demo-canigo:latest -f src/assembly/docker/app/Dockerfile .")
					}						   	
				
				
					stage ('Pujar imatge docker al nostre registre') {
						withCredentials([usernamePassword(credentialsId: 'azureRegistryId', passwordVariable: 'REGISTRY_PASSWORD', usernameVariable: 'REGISTRY_USERNAME')]) { 
						  sh("docker login -u ${REGISTRY_USERNAME} -p ${REGISTRY_PASSWORD} gencat.azurecr.io")
						  sh("docker push gencat.azurecr.io/demo-canigo:latest")
						}
					}						   	
				}

				//TODO: Moure a funcio parametritzable
			    /*stage ('Generació Tag BUILD') {
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
			  
			  	//TODO: Externalitzar valors
				container(name: 'clients') {
					stage ('Desplegament INT') {
						deployProject{
							stagedProject = 'demo-canigo:latest'
						    resourceLocation = 'src/assembly/kubernetes/kubernetes-dev.yaml'
						    environment = 'dev'
							registry = 'gencat.azurecr.io'
						}
					}
				}

				container(name: 'maven') {
					//TODO: Not sure of the real nature of smoke tests
					/*stage ('Smoke Test INT') {
					 	sh "mvn verify -Dmaven.test.failure.ignore -PsmokeTest,dev"
					}*/
					stage('Acceptance Test INT') {
					     sh "mvn verify -Dmaven.test.failure.ignore" 
					}
					stage ('CESICAT: Seguretat amb ZAP') {
                            try {
                                sh "mvn -Psecurity-check verify"
                                publishHTML(target: [
                                        reportDir            : 'target',
                                        reportFiles          : 'dependency-check-report.html',
                                        reportName           : 'OWASP Dependency Check Report',
                                        keepAll              : true,
                                        alwaysLinkToLastBuild: true,
                                        allowMissing         : false
                                ])
                            }
                            finally {
                                archiveArtifacts artifacts: '*/target/zap-reports/*.xml'
                                publishHTML(target: [
                                        reportDir            : 'target/zap-reports',
                                        reportFiles          : 'zapReport.html',
                                        reportName           : 'ZAP Report',
                                        keepAll              : true,
                                        alwaysLinkToLastBuild: true,
                                        allowMissing         : false
                                ])
                                dependencyCheckPublisher canComputeNew: false, defaultEncoding: '', failedTotalAll: '150', healthy: '', pattern: 'target/dependency-check-report.xml', unHealthy: ''
							}
					}
				}

				container(name: 'clients') {
					stage ('Desplegament PRE') {
						deployProject{
							stagedProject = 'demo-canigo:latest'
						    resourceLocation = 'src/assembly/kubernetes/kubernetes-pre.yaml'
						    environment = 'pre'
							registry = 'gencat.azurecr.io'
						}
					}
				}

				container(name: 'maven') {
					/*stage ('Smoke Test PRE') {
						sh "mvn verify -PsmokeTest,dev"
					}*/
					stage ('Acceptance Test PRE') {
					 	sh "mvn verify -Dmaven.test.failure.ignore" 
					}
				} 
				container(name: 'performance') {
					stage('Capacity TEST PRE') {
       					 sh "bzt src/test/jmeter/simple-assert.yml -o settings.artifacts-dir=artifacts"
					}
					stage ('Exploratory Test PRE') {
						echo "Exploratory Test PRE"
					}

				}

				//TODO: Moure fora del node (flyweight executor) fer stash/untash
				container(name: 'clients') {
					stage ('Desplegament PRO') {
						input 'Vols promocionar el build a pro?'
						deployProject{
							stagedProject = 'demo-canigo:latest'
						    resourceLocation = 'src/assembly/kubernetes/kubernetes.yaml'
						    environment = 'pro'
							registry = 'gencat.azurecr.io'
						}
					}   
				 } 


				container(name: 'maven') {
					stage ('Generació Tag DEFINITIU') {
						echo "Generació Tag DEFINITIU"
					}

					stage ('Smoke Test PRO') {
						sh "mvn verify -PsmokeTest,dev"
					}
				} 


				stage("post-proc") {
					archiveArtifacts artifacts: 'target/**/*'
					junit 'target/surefire-reports/*.xml'
				}
	  		 }
	  	  }
	   }
	}
}


