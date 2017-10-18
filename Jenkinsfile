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
					    //TODO: Change to publish html
					    junit healthScaleFactor: 1.0, testResults: 'target/surefire-reports/TEST*.xml'	
					    /*publishHTML(target: [
                                reportDir            : 'target/surefire-reports',
                                reportFiles          : 'index.html',
                                reportName           : 'Test unitaris',
                                keepAll              : true,
                                alwaysLinkToLastBuild: true,
                                allowMissing         : false
                        ]) */
					}
				

					stage ('Anàlisi de codi estàtic') {
						withSonarQubeEnv("SonarQubeServer") {
						    sh "mvn sonar:sonar -Dsonar.host.url=$SONAR_HOST_URL" 
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

					 //TODO: Fest aquesta tasca en paralel amb Sonarqube
					 stage("CESICAT: Anàlisi seguretat dependency check") {
	                        //TODO: Opcio d'utilitzar dependencyCheckAnalyzer
	                        //dependencyCheckAnalyzer datadir: '', hintsFile: '', includeCsvReports: false, includeHtmlReports: true, includeJsonReports: false, isAutoupdateDisabled: false, outdir: '', scanpath: '**/viewer-**.war,', skipOnScmChange: false, skipOnUpstreamChange: false, suppressionFile: '', zipExtensions: ''
	                        try {
	                            sh "mvn verify -Powasp-dependencycheck,dev"
	                        }
	                        finally {
	                            publishHTML(target: [
	                                    reportDir            : 'target',
	                                    reportFiles          : 'dependency-check-report.html',
	                                    reportName           : 'OWASP Dependency Check Informe',
	                                    keepAll              : true,
	                                    alwaysLinkToLastBuild: true,
	                                    allowMissing         : false
	                            ])
	                            dependencyCheckPublisher canComputeNew: false, defaultEncoding: '', failedTotalAll: '150', healthy: '', pattern: 'target/dependency-check-report.xml', unHealthy: ''
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
					stage ('Smoke Test INT') {
					 	sh "mvn verify -Dmaven.test.failure.ignore -PsmokeTest,int -Dserver.url=http://bookstore.dev.matxa.es"
					}
					stage('Acceptance Test INT') {
					     sh "mvn verify -Dmaven.test.failure.ignore -PintegrationTest,int -Dserver.url=http://bookstore.dev.matxa.es" 
					}
					stage ('CESICAT: Anàlisi seguretat amb ZAP') {
                            try {
                                sh "mvn -Powasp-zap,dev verify"
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
					stage ('Smoke Test PRE') {
						sh "mvn verify  -Dmaven.test.failure.ignore -PsmokeTest,pre -Dserver.url=http://bookstore.pre.matxa.es"
					}
					stage ('Acceptance Test PRE') { 
					 	sh "mvn verify -Dmaven.test.failure.ignore  -PintegrationTest,pre -Dserver.url=http://bookstore.pre.matxa.es" 
					}
				} 
				container(name: 'performance') {
					stage('Capacity TEST PRE') {
       					 sh "bzt src/test/jmeter/simple-assert.yml -o settings.artifacts-dir=artifacts"
					}

				}

				stage ('Exploratory Test PRE') {
					echo "Exploratory Test PRE"
					input 'Vols promocionar el build a pro?'
				}

				container(name: 'clients') {
					stage ('Desplegament PRO') {
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
						sh "mvn verify -Dmaven.test.failure.ignore -PsmokeTest,pre -Dserver.url=http://bookstore.matxa.es"
					}
				} 

				stage("Arxivar artefactes") {
					archiveArtifacts artifacts: 'target/**/*'
				}
	  		 }
	  	  }
	   }
	}
}


